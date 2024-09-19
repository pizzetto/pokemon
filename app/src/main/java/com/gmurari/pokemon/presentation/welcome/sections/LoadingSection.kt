package com.gmurari.pokemon.presentation.welcome.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.gmurari.pokemon.R
import com.gmurari.pokemon.domain.repository.Percentage
import com.gmurari.pokemon.ui.theme.dimens

@Composable
fun LoadingSection(
    modifier: Modifier = Modifier,
    loadingProgress: Percentage,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            progress = { loadingProgress.toFloat() },
            trackColor = ProgressIndicatorDefaults.circularDeterminateTrackColor,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.medium))
        Text(text = stringResource(R.string.loading))
    }
}