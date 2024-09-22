package com.gmurari.pokemon.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.gmurari.pokemon.data.local.PokemonLocalService
import com.gmurari.pokemon.data.remote.PokemonRemoteService
import com.gmurari.pokemon.data.remote.mediator.PokemonRemoteMediator
import com.gmurari.pokemon.data.util.percentOf
import com.gmurari.pokemon.domain.model.Pokemon
import com.gmurari.pokemon.domain.repository.AsyncOp
import com.gmurari.pokemon.domain.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PokemonRepositoryImpl @Inject constructor(
    private val pokemonRemoteService: PokemonRemoteService,
    private val pokemonLocalService: PokemonLocalService
): PokemonRepository {

    override fun getPokemonList(
        searchString: String,
        limit: Int,
        offset: Int
    ): Flow<List<Pokemon>> = pokemonLocalService.getPokemonInfoList(searchString, offset, limit)
        .map { list ->
            list.map {
                it.toPokemon()
            }
        }


    override fun downloadAllPokemonData(): Flow<AsyncOp<Unit>> = flow {
        try {
            emit(AsyncOp.Loading(true, 0.0))

            val pokemonList = pokemonRemoteService.getPokemonList(DOWNLOAD_LIST_LIMIT)

            pokemonLocalService.clearPokemonInfo()

            val totalPokemons = pokemonList.results.size
            var downloadedPokemons = 0

            coroutineScope {
                val deferredPokemonInfos = pokemonList.results.map { pokemonListItemDto ->
                    async {
                        pokemonRemoteService.getPokemonInfo(pokemonListItemDto.name)
                    }
                }

                deferredPokemonInfos
                    .awaitAll()
                    .onEach {
                        pokemonLocalService.storePokemonInfo(
                            it.toPokemonInfoEntity(),
                            it.toPokemonTypeEntityList(),
                            it.toPokemonTypeCrossRefList()
                        )
                        downloadedPokemons++

                        val currentProgress = downloadedPokemons percentOf totalPokemons
                        emit(AsyncOp.Loading<Unit>(true, currentProgress))
                    }
                    .map { it.species.name }
                    .distinct()
                    .map { specieName ->
                        async { pokemonRemoteService.getPokemonSpecie(specieName) }
                    }
                    .awaitAll()
                    .forEach {
                        pokemonLocalService.storePokemonSpecies(it.toPokemonSpeciesEntity())
                    }

                emit(AsyncOp.Success(Unit))
            }
        } catch (e: Exception) {
            emit(AsyncOp.Error(e))
        } finally {
            emit(AsyncOp.Loading(false, 0.0))
        }
    }.flowOn(Dispatchers.IO)

    override fun downloadPokemonList(): Flow<AsyncOp<Unit>> = flow<AsyncOp<Unit>> {
        try {
            emit(AsyncOp.Loading(true, 0.0))

            pokemonLocalService.clearPokemonInfo()

            val pokemonList = pokemonRemoteService.getPokemonList(DOWNLOAD_LIST_LIMIT)
            pokemonLocalService.storePokemonListItems(pokemonList.toPokemonListItemEntity())

            emit(AsyncOp.Success(Unit))

        } catch (e: Exception) {
            emit(AsyncOp.Error(e))
        } finally {
            emit(AsyncOp.Loading(false, 0.0))
        }
    }.flowOn(Dispatchers.IO)

    @OptIn(ExperimentalPagingApi::class)
    override fun getPokemonPaging(searchString: String): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                pokemonLocalService.getPokemonPagingSource(searchString)
            },
            remoteMediator = PokemonRemoteMediator(
                searchString,
                pokemonLocalService,
                pokemonRemoteService
            )
        ).flow.map { list ->
            list.map {
                it.toPokemon()
            }
        }
    }

    companion object {
        private const val DOWNLOAD_LIST_LIMIT = 10000
        private const val PAGE_SIZE = 20
    }
}