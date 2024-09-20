package com.gmurari.pokemon.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.gmurari.pokemon.R
import com.gmurari.pokemon.presentation.home.event.HomeUiEvent
import com.gmurari.pokemon.presentation.home.sections.PokemonItem
import com.gmurari.pokemon.presentation.home.state.HomeUiState
import com.gmurari.pokemon.ui.theme.dimens

@Composable
fun HomeScreen(
    state: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.dimens.medium)
    ) {
        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.pokemon))
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.box))
                }
            }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium))
        TextField(
            value = state.searchString,
            onValueChange = {
                onEvent(HomeUiEvent.OnSearchStringChange(it))
            },
            placeholder = {
                Text(text = stringResource(R.string.search_name_or_type))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_name_or_type)
                )
            },
            singleLine = true
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.large),
            contentPadding = PaddingValues(
                top = MaterialTheme.dimens.large,
                bottom = MaterialTheme.dimens.large
            ),
        ) {
            itemsIndexed(state.pokemonList, key = { _, pokemon -> pokemon.id }) { _, pokemon ->
                PokemonItem(pokemon = pokemon)
                HorizontalDivider(modifier = Modifier.padding(horizontal = MaterialTheme.dimens.medium).fillMaxWidth())
            }
        }
    }

}
