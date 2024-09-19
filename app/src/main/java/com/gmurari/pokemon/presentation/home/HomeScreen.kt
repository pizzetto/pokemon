package com.gmurari.pokemon.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gmurari.pokemon.domain.model.Pokemon
import com.gmurari.pokemon.presentation.home.state.HomeUiState
import com.gmurari.pokemon.ui.theme.dimens

@Composable
fun HomeScreen(
    state: HomeUiState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.large),
        contentPadding = PaddingValues(
            top = MaterialTheme.dimens.large,
            bottom = MaterialTheme.dimens.large
        )
    ) {
        itemsIndexed(state.pokemonList, key = { _, pokemon -> pokemon.id }) { _, pokemon ->
            PokemonItem(pokemon = pokemon)
        }
    }
}

@Composable
fun PokemonItem(
    modifier: Modifier = Modifier,
    pokemon: Pokemon
) {
    Text(text = pokemon.name)
}