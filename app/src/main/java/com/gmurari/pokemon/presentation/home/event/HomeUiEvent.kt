package com.gmurari.pokemon.presentation.home.event

sealed interface HomeUiEvent {
    data class OnSearchStringChange(val searchString: String) : HomeUiEvent
}