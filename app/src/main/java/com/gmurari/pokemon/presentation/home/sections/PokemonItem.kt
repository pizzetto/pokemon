package com.gmurari.pokemon.presentation.home.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.gmurari.pokemon.domain.model.Pokemon
import com.gmurari.pokemon.ui.theme.PokemonTheme
import com.gmurari.pokemon.ui.theme.dimens

@Composable
fun PokemonItem(
    modifier: Modifier = Modifier,
    pokemon: Pokemon
) {
    val currentLocale = Locale.current

    Row(modifier = modifier
        .height(IntrinsicSize.Max)
        .padding(MaterialTheme.dimens.medium)
    ) {
        AsyncImage(
            model = pokemon.imageUrl,
            contentDescription = pokemon.name,
            modifier = Modifier
                .weight(1f)
                .height(150.dp)
        )
        Spacer(modifier = Modifier.width(MaterialTheme.dimens.large))
        Column(
            modifier = Modifier
                .weight(3f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = pokemon.name.capitalize(currentLocale),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium))
            PokemonTypes(modifier = Modifier.fillMaxWidth(), pokemon = pokemon)
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium))
            Text(
                text = pokemon.speciesDescription,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PokemonItemPreview() {
    PokemonTheme {
        PokemonItem(
            pokemon = Pokemon(
                id = 1,
                name = "Bulbasaur",
                height = 7,
                weight = 69,
                imageUrl = "",
                types = listOf("grass", "poison"),
                speciesDescription = ""
            )
        )
    }
}