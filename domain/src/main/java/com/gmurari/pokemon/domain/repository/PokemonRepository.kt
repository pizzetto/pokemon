package com.gmurari.pokemon.domain.repository

import androidx.paging.PagingData
import com.gmurari.pokemon.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

typealias Percentage = Double

interface PokemonRepository {

    /**
     * Get the list of Pokemon from the database.
     */
    fun getPokemonList(searchString: String = "", limit: Int = 0, offset: Int = 20): Flow<List<Pokemon>>

    /**
     * Get the list of Pokemon from the database as a PagingSource.
     */
    fun getPokemonPaging(searchString: String = ""): Flow<PagingData<Pokemon>>

    /**
     * Download the list of Pokemon from the API and save it to the database.
     *
     * I chosen here to download all pokemon first, to enable the user to search for a part of the name or
     * part of type name. I know it's slow and not ideal.
     * Otherwise i would have used a RemoteMediator to download the pokemon as they are scrolled
     */
    fun downloadAllPokemonData(): Flow<AsyncOp<Unit>>

    /**
     * Download the list of Pokemon from the API and save it to the database.
     *
     * Here only the name list is saved and used to enable the user to filter by Pokemon name.
     */
    fun downloadPokemonList(): Flow<AsyncOp<Unit>>
}