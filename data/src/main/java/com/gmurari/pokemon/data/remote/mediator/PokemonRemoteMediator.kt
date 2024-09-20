package com.gmurari.pokemon.data.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.gmurari.pokemon.data.local.PokemonLocalService
import com.gmurari.pokemon.data.local.entity.PokemonWithRelations
import com.gmurari.pokemon.data.remote.PokemonRemoteService
import com.gmurari.pokemon.data.repository.toPokemonInfoEntity
import com.gmurari.pokemon.data.repository.toPokemonTypeCrossRefList
import com.gmurari.pokemon.data.repository.toPokemonTypeEntityList
import kotlinx.coroutines.async
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

            val page = when(loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if(lastItem == null) {
                        1
                    } else {
                        (lastItem.pokemon.id / state.config.pageSize) + 1
                    }
                }
            }
            val offset = (page - 1) * state.config.pageSize


            // The network load method takes an optional after=<user.id>
            // parameter. For every page after the first, pass the last user
            // ID to let it continue from where it left off. For REFRESH,
            // pass null to load the first page.
            /*val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                // In this example, you never need to prepend, since REFRESH
                // will always load the first page in the list. Immediately
                // return, reporting end of pagination.
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )

                    // You must explicitly check if the last item is null when
                    // appending, since passing null to networkService is only
                    // valid for initial load. If lastItem is null it means no
                    // items were loaded after the initial REFRESH and there are
                    // no more items to load.

                    lastItem.pokemon.id
                }
            }*/

            val localPokemonList = pokemonLocalService.getPokemonList(
                searchString,
                offset,
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

                deferredPokemonInfos.forEach { deferred ->
                    val pokemonInfoDto = deferred.await()

                    pokemonLocalService.storePokemonInfo(
                        pokemonInfoDto.toPokemonInfoEntity(),
                        pokemonInfoDto.toPokemonTypeEntityList(),
                        pokemonInfoDto.toPokemonTypeCrossRefList()
                    )
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = localPokemonList.size < state.config.pageSize
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return super.initialize()
    }

}