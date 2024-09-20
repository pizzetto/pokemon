package com.gmurari.pokemon.presentation.home.state

enum class ListState {
    IDLE,
    LOADING,
    PAGINATING,
    ERROR,
    PAGINATION_EXHAUST,
}