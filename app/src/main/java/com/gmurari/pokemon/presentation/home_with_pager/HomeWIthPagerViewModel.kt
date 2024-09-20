package com.gmurari.pokemon.presentation.home_with_pager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gmurari.pokemon.domain.model.Pokemon
import com.gmurari.pokemon.domain.repository.PokemonRepository
import com.gmurari.pokemon.presentation.home_with_pager.event.HomeWithPagerEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeWIthPagerViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
): ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Esponi i dati paginati come Flow<PagingData>
    val pagingDataFlow: Flow<PagingData<Pokemon>> = _searchQuery
        .debounce(300) // Aggiungi un debounce per evitare chiamate eccessive
        .distinctUntilChanged()
        .flatMapLatest { query ->
            pokemonRepository.getPokemonPaging(query)
                .cachedIn(viewModelScope)
        }

    fun onEvent(event: HomeWithPagerEvent) {
        when(event) {
            is HomeWithPagerEvent.OnSearchStringChange -> {
                _searchQuery.value = event.searchString
            }
        }
    }
}