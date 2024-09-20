package com.gmurari.pokemon.presentation.home.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.gmurari.pokemon.domain.model.Pokemon
import com.gmurari.pokemon.ui.theme.dimens

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PokemonTypes(
    modifier: Modifier = Modifier,
    pokemon: Pokemon
) {
    val currentLocale = Locale.current

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small)
    ) {
        pokemon.types.forEach {
            Text(
                text = it.capitalize(currentLocale),
                modifier = Modifier
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(MaterialTheme.dimens.small)
                    )
                    .padding(
                        horizontal = MaterialTheme.dimens.medium,
                        vertical = MaterialTheme.dimens.small
                    ),
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
        }
    }
}