package com.gmurari.pokemon.domain.repository

import androidx.paging.PagingData
import com.gmurari.pokemon.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

typealias Percentage = Double

interface PokemonRepository {

    fun getPokemonList(searchString: String = "", limit: Int = 0, offset: Int = 20): Flow<List<Pokemon>>

    fun getPokemonPaging(searchString: String = ""): Flow<PagingData<Pokemon>>

    fun downloadAllPokemonData(): Flow<AsyncOp<Unit>>

    fun downloadPokemonList(): Flow<AsyncOp<Unit>>
}