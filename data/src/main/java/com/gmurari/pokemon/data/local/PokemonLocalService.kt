package com.gmurari.pokemon.data.local

import androidx.paging.PagingSource
import com.gmurari.pokemon.data.local.entity.PokemonInfoEntity
import com.gmurari.pokemon.data.local.entity.PokemonListItemEntity
import com.gmurari.pokemon.data.local.entity.PokemonSpeciesEntity
import com.gmurari.pokemon.data.local.entity.PokemonTypeCrossRef
import com.gmurari.pokemon.data.local.entity.PokemonTypeEntity
import com.gmurari.pokemon.data.local.entity.PokemonWithRelations
import kotlinx.coroutines.flow.Flow

internal interface PokemonLocalService {

    fun getPokemonInfoList(search: String, offset: Int, limit: Int): Flow<List<PokemonWithRelations>>

    fun getPokemonList(search: String, lastRetrievedId: Int, limit: Int): Flow<List<PokemonListItemEntity>>

    suspend fun storePokemonInfo(pokemonInfoEntity: PokemonInfoEntity,
                                 pokemonTypeEntities: List<PokemonTypeEntity>,
                                 pokemonTypeCrossRefEntities: List<PokemonTypeCrossRef>)

    suspend fun storePokemonListItems(pokemonListItems: List<PokemonListItemEntity>)

    suspend fun storePokemonSpecies(pokemonSpecies: PokemonSpeciesEntity)

    fun getPokemonPagingSource(search: String): PagingSource<Int, PokemonWithRelations>

    suspend fun clearPokemonInfo()
}