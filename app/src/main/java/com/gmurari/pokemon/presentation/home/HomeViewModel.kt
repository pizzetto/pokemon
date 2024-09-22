package com.gmurari.pokemon.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmurari.pokemon.domain.usecase.GetPokemonListUseCase
import com.gmurari.pokemon.presentation.home.event.HomeUiEvent
import com.gmurari.pokemon.presentation.home.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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

    init {
        viewModelScope.launch {
            _state.map { it.searchString to it.page }
                .distinctUntilChanged()
                .debounce(300)
                .flatMapLatest { (search, page) ->
                    getPokemonListUseCase(
                        search,
                        limit = PAGE_SIZE,
                        page = page
                    )
                }
                .collect {
                    _state.value = _state.value.copy(
                        canPaginate = it.isNotEmpty(),
                        pokemonList = _state.value.pokemonList + it,
                    )
                }
        }
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnSearchStringChange -> {
                _state.update {
                    it.copy(
                        searchString = event.searchString,
                        page = INITIAL_PAGE,
                        pokemonList = emptyList(),
                        canPaginate = false
                    )
                }
            }

            is HomeUiEvent.OnPageChange -> {
                _state.update {
                    it.copy(
                        page = event.page
                    )
                }
            }
        }
    }

    companion object {
        const val PAGE_SIZE = 20
        const val INITIAL_PAGE = 1
    }
}