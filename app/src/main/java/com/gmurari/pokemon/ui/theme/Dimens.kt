package com.gmurari.pokemon.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
class Dimens(
    val xsmall: Dp = DimensDefaults.ExtraSmall,
    val small: Dp = DimensDefaults.Small,
    val medium: Dp = DimensDefaults.Medium,
    val large: Dp = DimensDefaults.Large,
    val xlarge: Dp = DimensDefaults.ExtraLarge,
    val xxlarge: Dp = DimensDefaults.ExtraExtraLarge
)

object DimensDefaults {
    val ExtraSmall: Dp = 2.dp
    val Small: Dp = 4.dp
    val Medium: Dp = 8.dp
    val Large: Dp = 16.dp
    val ExtraLarge: Dp = 24.dp
    val ExtraExtraLarge: Dp = 48.dp
}

internal val LocalDimens = staticCompositionLocalOf { Dimens() }

val MaterialTheme.dimens: Dimens
    @Composable
    @ReadOnlyComposable
    get() = LocalDimens.current