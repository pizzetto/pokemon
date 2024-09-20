package com.gmurari.pokemon.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
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

    /**
     * Download the list of Pokemon from the API and save it to the database.
     *
     * It can be refactored further to separate the download from the saving to the database,
     * to improve the maintainability and testability of the code.
     *
     * I chosen to download all pokemon first, to enable the user to search for a part of the name or
     * part of type name. I know it's slow and not ideal.
     * Otherwise i would have used a RemoteMediator to download the pokemon as they are scrolled
     */
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

                deferredPokemonInfos.forEach { deferred ->
                    val pokemonInfoDto = deferred.await()

                    pokemonLocalService.storePokemonInfo(
                        pokemonInfoDto.toPokemonInfoEntity(),
                        pokemonInfoDto.toPokemonTypeEntityList(),
                        pokemonInfoDto.toPokemonTypeCrossRefList()
                    )
                    downloadedPokemons++

                    val currentProgress = downloadedPokemons percentOf totalPokemons
                    emit(AsyncOp.Loading<Unit>(true, currentProgress))
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