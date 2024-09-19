package com.gmurari.pokemon.presentation.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmurari.pokemon.domain.repository.AsyncOp
import com.gmurari.pokemon.domain.usecase.DownloadPokemonListUseCase
import com.gmurari.pokemon.presentation.welcome.event.WelcomeVmEvent
import com.gmurari.pokemon.presentation.welcome.state.WelcomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val downloadPokemonListUseCase: DownloadPokemonListUseCase,
): ViewModel() {

    private val _state = MutableStateFlow(WelcomeUiState())
    val state = _state.asStateFlow()

    private val welcomeVmEventChannel = Channel<WelcomeVmEvent>()
    val event = welcomeVmEventChannel.receiveAsFlow()

    private var downloadJob: Job? = null

    init {
        viewModelScope.launch {
            downloadPokemonList()
        }
    }

    private suspend fun downloadPokemonList() {

        downloadPokemonListUseCase().collect { asyncOp ->
            when (asyncOp) {
                is AsyncOp.Error -> {
                    _state.value = _state.value.copy(
                        error = asyncOp.error.localizedMessage
                    )
                }
                is AsyncOp.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = asyncOp.isLoading,
                        loadingProgress = asyncOp.progress
                    )
                }
                is AsyncOp.Success -> {
                    welcomeVmEventChannel.trySend(WelcomeVmEvent.DownloadCompleted)
                }
            }
        }
    }

    fun onEvent(event: WelcomeUiEvent) {
        when (event) {
            WelcomeUiEvent.OnRetryDownload -> {
                if (!_state.value.isLoading) {

                    downloadJob?.cancel()
                    downloadJob = viewModelScope.launch {
                        // Debounce download
                        delay(300.milliseconds)
                        downloadPokemonList()
                    }
                }
            }
        }
    }
}