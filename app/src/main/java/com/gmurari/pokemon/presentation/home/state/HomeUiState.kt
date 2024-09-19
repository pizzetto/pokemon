package com.gmurari.pokemon.presentation.home.state

import com.gmurari.pokemon.domain.model.Pokemon

data class HomeUiState(
    val pokemonList: List<Pokemon> = emptyList()
)
