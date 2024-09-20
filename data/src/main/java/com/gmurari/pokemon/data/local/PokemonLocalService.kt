package com.gmurari.pokemon.data.local

import com.gmurari.pokemon.data.local.entity.PokemonInfoEntity
import com.gmurari.pokemon.data.local.entity.PokemonListItemEntity
import com.gmurari.pokemon.data.local.entity.PokemonTypeCrossRef
import com.gmurari.pokemon.data.local.entity.PokemonTypeEntity
import com.gmurari.pokemon.data.local.entity.PokemonWithRelations
import kotlinx.coroutines.flow.Flow

internal interface PokemonLocalService {

    fun getPokemonList(search: String, offset: Int, limit: Int): Flow<List<PokemonWithRelations>>

    fun getPokemon(id: Int): Flow<PokemonWithRelations>

    suspend fun storePokemonInfo(pokemonInfoEntity: PokemonInfoEntity,
                                 pokemonTypeEntities: List<PokemonTypeEntity>,
                                 pokemonTypeCrossRefEntities: List<PokemonTypeCrossRef>)

    suspend fun storePokemonListItems(pokemonListItems: List<PokemonListItemEntity>)

    suspend fun clearDatabase()
}