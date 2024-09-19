package com.gmurari.pokemon.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmurari.pokemon.domain.usecase.GetPokemonListUseCase
import com.gmurari.pokemon.presentation.home.state.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase
): ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getPokemonListUseCase("", 0, 20).collect { pokemonList ->
                _state.value = _state.value.copy(
                    pokemonList = pokemonList
                )
            }
        }
    }
}