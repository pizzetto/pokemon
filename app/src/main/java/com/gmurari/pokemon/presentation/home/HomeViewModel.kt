package com.gmurari.pokemon.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmurari.pokemon.domain.model.Pokemon
import com.gmurari.pokemon.domain.usecase.GetPokemonListUseCase
import com.gmurari.pokemon.presentation.home.event.HomeUiEvent
import com.gmurari.pokemon.presentation.home.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase
): ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    private val _searchString = MutableStateFlow("")
    val searchString = _searchString.asStateFlow()

    private val _offset = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val pokemonList = searchString
        .debounce(300) // To prevent too many requests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")
        /*.flatMapLatest { search ->
            _offset.value = 0 // Reset offset on new search
            getPokemonListFlow(search)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())*/

    private fun getPokemonListFlow(search: String): Flow<List<Pokemon>> = flow {
        val currentOffset = _offset.value
        getPokemonListUseCase(search, limit = 20, offset = currentOffset)
    }
    /*init {
        viewModelScope.launch {
            getPokemonListUseCase("", 20, 0).collect { pokemonList ->
                _state.value = _state.value.copy(
                    pokemonList = pokemonList
                )
            }
        }
    }*/

    init {
        viewModelScope.launch {
            _searchString
                .debounce(300)
                .onEach {
                    _offset.value = 0
                }
                .flatMapLatest { search ->
                    getPokemonListFlow(search)
                }
                .collect {
                    _state.value = _state.value.copy(
                        pokemonList = it
                    )
                }
        }
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnSearchStringChange -> {
                _state.update {
                    it.copy(
                        searchString = event.searchString
                    )
                }
            }
        }
    }
}