package com.gmurari.pokemon.presentation.welcome

sealed interface WelcomeUiEvent {
    data object OnRetryDownload : WelcomeUiEvent
}