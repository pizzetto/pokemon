package com.gmurari.pokemon.presentation.welcome.state

import androidx.compose.runtime.Immutable
import com.gmurari.pokemon.domain.repository.Percentage

@Immutable
data class WelcomeUiState(
    val isLoading: Boolean = false,
    val loadingProgress: Percentage = 0.0,
    val error: String? = null
)
