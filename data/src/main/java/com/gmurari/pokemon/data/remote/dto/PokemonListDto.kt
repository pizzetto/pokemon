package com.gmurari.pokemon.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class PokemonListDto(
    val count: Int,
    val results: List<PokemonListItemDto>
)
