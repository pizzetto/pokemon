package com.gmurari.pokemon.data.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.gmurari.pokemon.data.local.PokemonLocalService
import com.gmurari.pokemon.data.local.entity.PokemonWithRelations
import com.gmurari.pokemon.data.remote.PokemonRemoteService
import com.gmurari.pokemon.data.repository.toPokemonInfoEntity
import com.gmurari.pokemon.data.repository.toPokemonSpeciesEntity
import com.gmurari.pokemon.data.repository.toPokemonTypeCrossRefList
import com.gmurari.pokemon.data.repository.toPokemonTypeEntityList
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
internal class PokemonRemoteMediator(
    private val searchString: String,
    private val pokemonLocalService: PokemonLocalService,
    private val pokemonRemoteService: PokemonRemoteService
): RemoteMediator<Int, PokemonWithRelations>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonWithRelations>
    ): MediatorResult {

        return try {

            // I load the Id of the last item retrieved from the pokemon list table. If it's null,
            // I start load from the beginning.
            val lastRetrievedId = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )

                    lastItem.pokemon.id
                }
            }

            // I load the pokemon list from local database to get the list of pokemon
            // whose I need to download the info.
            val localPokemonList = pokemonLocalService.getPokemonList(
                searchString,
                lastRetrievedId,
                limit = state.config.pageSize
            ).first()


            if (loadType == LoadType.REFRESH) {
                pokemonLocalService.clearPokemonInfo()
            }

            coroutineScope {
                val deferredPokemonInfos = localPokemonList.map { pokemonListItem ->
                    async {
                        pokemonRemoteService.getPokemonInfo(pokemonListItem.name)
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
            }

            MediatorResult.Success(
                endOfPaginationReached = localPokemonList.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}