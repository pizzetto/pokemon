package com.gmurari.pokemon.data.local.impl

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.gmurari.pokemon.data.local.PokemonLocalService
import com.gmurari.pokemon.data.local.db.PokemonDatabase
import com.gmurari.pokemon.data.local.entity.PokemonInfoEntity
import com.gmurari.pokemon.data.local.entity.PokemonListItemEntity
import com.gmurari.pokemon.data.local.entity.PokemonTypeCrossRef
import com.gmurari.pokemon.data.local.entity.PokemonTypeEntity
import com.gmurari.pokemon.data.local.entity.PokemonWithRelations
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PokemonLocalServiceImpl @Inject constructor(
    private val pokemonDatabase: PokemonDatabase
): PokemonLocalService {

    override fun getPokemonInfoList(
        search: String,
        offset: Int,
        limit: Int
    ): Flow<List<PokemonWithRelations>> =
        pokemonDatabase.dao.getPokemonInfoList(search, offset, limit)

    override fun getPokemonList(
        search: String,
        offset: Int,
        limit: Int
    ): Flow<List<PokemonListItemEntity>> =
        pokemonDatabase.dao.getPokemonList(search, offset, limit)

    override fun getPokemon(id: Int): Flow<PokemonWithRelations> =
        pokemonDatabase.dao.getPokemon(id)

    override suspend fun storePokemonInfo(
        pokemonInfoEntity: PokemonInfoEntity,
        pokemonTypeEntities: List<PokemonTypeEntity>,
        pokemonTypeCrossRefEntities: List<PokemonTypeCrossRef>
    ) {
        pokemonDatabase.withTransaction {
            pokemonDatabase.dao.insertPokemonInfo(pokemonInfoEntity)
            pokemonDatabase.dao.insertPokemonTypes(pokemonTypeEntities)
            pokemonDatabase.dao.insertPokemonTypeCrossRefs(pokemonTypeCrossRefEntities)
        }
    }

    override suspend fun storePokemonListItems(pokemonListItems: List<PokemonListItemEntity>) {
        pokemonDatabase.dao.insertPokemonList(pokemonListItems)
    }

    override suspend fun clearPokemonInfo() {
        pokemonDatabase.withTransaction {
            pokemonDatabase.dao.clearPokemonInfo()
            pokemonDatabase.dao.clearPokemonTypes()
            pokemonDatabase.dao.clearPokemonTypeCrossRef()
        }
    }

    override fun getPokemonPagingSource(search: String): PagingSource<Int, PokemonWithRelations> =
        pokemonDatabase.dao.pagingSource(search)
}