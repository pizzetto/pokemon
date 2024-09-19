package com.gmurari.pokemon.domain.repository

import com.gmurari.pokemon.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

typealias Percentage = Double

interface PokemonRepository {

    //TODO spiegare che i valori di default li posso spostare in costanti
    fun getPokemonList(searchString: String = "", limit: Int = 0, offset: Int = 20): Flow<List<Pokemon>>

    fun downloadPokemonList(): Flow<AsyncOp<Unit>>
}