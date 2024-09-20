package com.gmurari.pokemon.data.repository

import androidx.room.withTransaction
import com.gmurari.pokemon.data.local.PokemonDatabase
import com.gmurari.pokemon.data.remote.PokemonRemoteService
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
    private val pokemonDatabase: PokemonDatabase,
    private val pokemonRemoteService: PokemonRemoteService
): PokemonRepository {

    override fun getPokemonList(
        searchString: String,
        limit: Int,
        offset: Int
    ): Flow<List<Pokemon>> =

        pokemonDatabase.dao.getPokemonList(searchString, offset, limit).map { list ->
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

            pokemonDatabase.withTransaction {
                pokemonDatabase.dao.clearPokemonInfo()
                pokemonDatabase.dao.clearPokemonTypes()
                pokemonDatabase.dao.clearPokemonTypeCrossRef()
            }

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

                    pokemonDatabase.withTransaction {
                        pokemonDatabase.dao.insertPokemonInfo(pokemonInfoDto.toPokemonInfoEntity())

                        val pokemonTypes = pokemonInfoDto.toPokemonTypeEntityList()
                        pokemonDatabase.dao.insertPokemonTypes(pokemonTypes)

                        val pokemonTypeCrossRef = pokemonInfoDto.toPokemonTypeCrossRefList()
                        pokemonDatabase.dao.insertPokemonTypeCrossRefs(pokemonTypeCrossRef)

                        downloadedPokemons++
                    }

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

    companion object {
        private const val DOWNLOAD_LIST_LIMIT = 10000
    }
}