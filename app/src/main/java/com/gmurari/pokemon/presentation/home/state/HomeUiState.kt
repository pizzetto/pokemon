package com.gmurari.pokemon.presentation.home.state

import com.gmurari.pokemon.domain.model.Pokemon
import com.gmurari.pokemon.presentation.home.HomeViewModel.Companion.INITIAL_PAGE

data class HomeUiState(
    val pokemonList: List<Pokemon> = emptyList(),
    val searchString: String = "",
    val page: Int = INITIAL_PAGE,
    val canPaginate: Boolean = false,
    val listState: ListState = ListState.IDLE
)
