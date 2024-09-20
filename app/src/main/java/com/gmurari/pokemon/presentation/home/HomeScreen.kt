package com.gmurari.pokemon.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.gmurari.pokemon.R
import com.gmurari.pokemon.presentation.home.event.HomeUiEvent
import com.gmurari.pokemon.presentation.home.sections.PokemonItem
import com.gmurari.pokemon.presentation.home.state.HomeUiState
import com.gmurari.pokemon.presentation.home.state.ListState
import com.gmurari.pokemon.ui.theme.dimens

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit
) {
    val lazyColumnListState = rememberLazyListState()
    val shouldStartPaginate = remember(
        key1 = state.canPaginate
    ) {
        derivedStateOf {
            state.canPaginate &&
                    (lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -9) >=
                    (lazyColumnListState.layoutInfo.totalItemsCount - 6)
        }
    }

    LaunchedEffect(key1 = shouldStartPaginate.value) {
        if (shouldStartPaginate.value)
            onEvent(HomeUiEvent.OnPageChange(state.page + 1))
    }

    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            text = buildAnnotatedString {
                append(stringResource(R.string.pokemon))
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.box))
                }
            }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(MaterialTheme.dimens.large),
            value = state.searchString,
            onValueChange = {
                onEvent(HomeUiEvent.OnSearchStringChange(it))
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.search_name_or_type),
                    color = Color.LightGray
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_name_or_type),
                    tint = Color.LightGray
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.colors().copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            state = lazyColumnListState,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium),
            contentPadding = PaddingValues(
                top = MaterialTheme.dimens.medium,
                bottom = MaterialTheme.dimens.medium
            ),
        ) {
            items(items = state.pokemonList, key = { it.id }) { pokemon ->
                PokemonItem(pokemon = pokemon)
                HorizontalDivider(modifier = Modifier
                    .padding(horizontal = MaterialTheme.dimens.medium)
                    .fillMaxWidth())
            }

            when(state.listState) {
                ListState.LOADING,
                ListState.PAGINATING -> {
                    item (
                        key = state.listState,
                    ) {
                        CircularProgressIndicator()
                    }
                }
                else -> {}
            }
        }
    }

}
