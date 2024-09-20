package com.gmurari.pokemon.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonInfoSpritesDto(
    @Json(name = "front_default")
    val frontDefault: String?
)
