package com.gmurari.pokemon.data.repository

import com.gmurari.pokemon.data.local.dao.PokemonDao
import com.gmurari.pokemon.data.remote.api.PokemonApi
import com.gmurari.pokemon.data.util.percentOf
import com.gmurari.pokemon.domain.model.Pokemon
import com.gmurari.pokemon.domain.repository.AsyncOp
import com.gmurari.pokemon.domain.repository.Percentage
import com.gmurari.pokemon.domain.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PokemonRepositoryImpl @Inject constructor(
    private val pokemonDao: PokemonDao,
    private val pokemonApi: PokemonApi
): PokemonRepository {

    override fun getPokemonList(
        searchString: String,
        limit: Int,
        offset: Int
    ): Flow<List<Pokemon>> =

        pokemonDao.getPokemonList(searchString, limit, offset).map { list ->
            list.map {
                it.toPokemon()
            }
        }
            .onStart { emit(emptyList()) }


    /**
     * Download the list of Pokemon from the API and save it to the database.
     *
     * It can be refactored further to separate the download from the saving to the database,
     * to improve the maintainability and testability of the code.
     */
    override fun downloadPokemonList(): Flow<AsyncOp<Unit>> = flow {
        try {
            emit(AsyncOp.Loading(true, 0.0))

            val pokemonList = pokemonApi.getPokemonList(DOWNLOAD_LIST_LIMIT)
            if (!pokemonList.isSuccessful) throw Exception("Failed to fetch Pokemon list")
            val pokemonListDto = pokemonList.body() ?: throw Exception("Pokemon list response body is null")

            pokemonDao.clearPokemonInfo()
            pokemonDao.clearPokemonTypes()
            pokemonDao.clearPokemonTypeCrossRef()

            val totalPokemons = pokemonListDto.results.size
            var downloadedPokemons = 0

            coroutineScope {
                val deferredPokemonInfos = pokemonListDto.results.map { pokemonListItemDto ->
                    async {
                        val pokemonInfo = pokemonApi.getPokemonInfo(pokemonListItemDto.name)
                        if (!pokemonInfo.isSuccessful) throw Exception("Failed to fetch info for ${pokemonListItemDto.name}")
                        pokemonInfo.body() ?: throw Exception("Pokemon info response body is null for ${pokemonListItemDto.name}")
                    }
                }

                deferredPokemonInfos.forEach { deferred ->
                    val pokemonInfoDto = deferred.await()
                    pokemonDao.insertPokemonInfo(pokemonInfoDto.toPokemonInfoEntity())

                    val pokemonTypes = pokemonInfoDto.toPokemonTypeEntityList()
                    pokemonDao.insertPokemonTypes(pokemonTypes)

                    val pokemonTypeCrossRef = pokemonInfoDto.toPokemonTypeCrossRefList()
                    pokemonDao.insertPokemonTypeCrossRefs(pokemonTypeCrossRef)

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

    companion object {
        private const val DOWNLOAD_LIST_LIMIT = 10000
    }
}