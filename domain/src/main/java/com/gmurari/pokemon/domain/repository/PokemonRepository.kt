package com.gmurari.pokemon.domain.repository

import com.gmurari.pokemon.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

typealias Percentage = Double

interface PokemonRepository {

    fun getPokemonList(searchString: String = "", limit: Int = 0, offset: Int = 20): Flow<List<Pokemon>>

    fun downloadAllPokemonData(): Flow<AsyncOp<Unit>>
}