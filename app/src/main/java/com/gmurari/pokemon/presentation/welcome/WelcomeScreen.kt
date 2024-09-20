package com.gmurari.pokemon.presentation.welcome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gmurari.pokemon.presentation.welcome.sections.ErrorSection
import com.gmurari.pokemon.presentation.welcome.sections.LoadingSection
import com.gmurari.pokemon.presentation.welcome.state.WelcomeUiState
import com.gmurari.pokemon.ui.theme.dimens

@Composable
fun WelcomeScreen(
    state: WelcomeUiState,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize().padding(MaterialTheme.dimens.medium),
        contentAlignment = Alignment.Center
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