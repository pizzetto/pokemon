package com.gmurari.pokemon.presentation.home_with_pager.event

import com.gmurari.pokemon.presentation.home.event.HomeUiEvent

sealed interface  HomeWithPagerEvent {
    data class OnSearchStringChange(val searchString: String) : HomeWithPagerEvent
}