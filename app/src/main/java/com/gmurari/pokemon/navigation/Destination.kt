package com.gmurari.pokemon.navigation

import kotlinx.serialization.Serializable

/**
 * All navigation destinations.
 */
object Destination {
    @Serializable
    object Welcome

    @Serializable
    object Home

    @Serializable
    object HomeWithPager
}