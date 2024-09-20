package com.gmurari.pokemon.presentation.home.state

import com.gmurari.pokemon.domain.model.Pokemon

data class HomeUiState(
    val pokemonList: List<Pokemon> = emptyList(),
    val searchString: String = "",
    val page: Int = 0,
    val canPaginate: Boolean = false,
    val listState: ListState = ListState.IDLE
)
