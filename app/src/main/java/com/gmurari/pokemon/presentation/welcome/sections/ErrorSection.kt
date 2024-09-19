package com.gmurari.pokemon.presentation.welcome.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gmurari.pokemon.R
import com.gmurari.pokemon.ui.theme.PokemonTheme
import com.gmurari.pokemon.ui.theme.dimens

@Composable
fun ErrorSection(
    modifier: Modifier = Modifier,
    errorMessage: String?,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_error_outline_24),
            contentDescription = errorMessage,
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium))
        Text(text = errorMessage ?: stringResource(R.string.unknown_error))
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.large))
        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorSectionPreview() {
    PokemonTheme {
        ErrorSection(errorMessage = "Error message") {}
    }
}