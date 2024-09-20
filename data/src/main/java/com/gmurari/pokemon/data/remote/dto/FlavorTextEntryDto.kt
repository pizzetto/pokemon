package com.gmurari.pokemon.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FlavorTextEntryDto(
    @Json(name = "flavor_text")
    val flavorText: String,
    val language: LanguageDto,
)
