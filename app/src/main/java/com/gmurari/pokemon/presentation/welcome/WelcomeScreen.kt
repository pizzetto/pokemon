package com.gmurari.pokemon.presentation.welcome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gmurari.pokemon.presentation.welcome.sections.ErrorSection
import com.gmurari.pokemon.presentation.welcome.sections.LoadingSection
import com.gmurari.pokemon.presentation.welcome.state.WelcomeUiState

@Composable
fun WelcomeScreen(
    state: WelcomeUiState,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (state.isLoading) {
            LoadingSection(loadingProgress = state.loadingProgress)
        } else if (state.error != null) {
            ErrorSection(
                errorMessage = state.error,
                onRetry = onRetry
            )
        }
    }
}