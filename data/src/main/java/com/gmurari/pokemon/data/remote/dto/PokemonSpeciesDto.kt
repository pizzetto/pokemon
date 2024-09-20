package com.gmurari.pokemon.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class PokemonSpeciesDto(
    val id: Int,
    val name: String,
    @Json(name = "flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntryDto>
)
