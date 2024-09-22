package com.gmurari.pokemon.presentation.home_with_pager.sections

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gmurari.pokemon.ui.theme.dimens

@Composable
fun PokemonListProgressIndicator() {
    CircularProgressIndicator(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize(align = Alignment.Center)
        .padding(MaterialTheme.dimens.medium)
    )
}