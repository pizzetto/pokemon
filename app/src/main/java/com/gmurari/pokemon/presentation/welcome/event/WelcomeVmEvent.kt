package com.gmurari.pokemon.presentation.welcome.event

sealed interface WelcomeVmEvent {
    data object DownloadCompleted : WelcomeVmEvent
}