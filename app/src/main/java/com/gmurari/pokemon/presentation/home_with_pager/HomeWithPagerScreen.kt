package com.gmurari.pokemon.presentation.home_with_pager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.gmurari.pokemon.R
import com.gmurari.pokemon.domain.model.Pokemon
import com.gmurari.pokemon.presentation.home.sections.PokemonItem
import com.gmurari.pokemon.presentation.home_with_pager.event.HomeWithPagerEvent
import com.gmurari.pokemon.presentation.home_with_pager.sections.PokemonListProgressIndicator
import com.gmurari.pokemon.ui.theme.dimens

@Composable
fun HomeWithPagerScreen(
    modifier: Modifier = Modifier,
    pokemonList: LazyPagingItems<Pokemon>,
    searchString: String,
    onEvent: (HomeWithPagerEvent) -> Unit
) {
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
            value = searchString,
            onValueChange = {
                onEvent(HomeWithPagerEvent.OnSearchStringChange(it))
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
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium),
            contentPadding = PaddingValues(
                top = MaterialTheme.dimens.medium,
                bottom = MaterialTheme.dimens.medium
            ),
        ) {
            items(
                pokemonList.itemCount,
                key = pokemonList.itemKey { it.id }
            ) {
                val pokemon = pokemonList[it]
                if (pokemon != null) {
                    PokemonItem(pokemon = pokemon)
                    HorizontalDivider(modifier = Modifier
                        .padding(horizontal = MaterialTheme.dimens.medium)
                        .fillMaxWidth())
                }

            }
            // Gestione degli stati di caricamento (facoltativa)
            pokemonList.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item { PokemonListProgressIndicator() }
                    }

                    loadState.append is LoadState.Loading -> {
                        item { PokemonListProgressIndicator() }
                    }

                    loadState.refresh is LoadState.Error -> {
                        val e = pokemonList.loadState.refresh as LoadState.Error
                        item {
                            Text(
                                text = stringResource(R.string.error, e.error.localizedMessage ?: "Unknown error"),
                                color = Color.Red,
                                modifier = Modifier.padding(MaterialTheme.dimens.medium)
                            )
                        }
                    }

                    loadState.append is LoadState.Error -> {
                        val e = pokemonList.loadState.append as LoadState.Error
                        item {
                            Text(
                                text = stringResource(R.string.error, e.error.localizedMessage ?: "Unknown error"),
                                color = Color.Red,
                                modifier = Modifier.padding(MaterialTheme.dimens.medium)
                            )
                        }
                    }
                }
            }
        }
    }
}