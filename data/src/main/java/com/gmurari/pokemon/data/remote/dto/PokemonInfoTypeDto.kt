package com.gmurari.pokemon.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonInfoTypeDto(
    val slot: Int, // 1
    val type: PokemonInfoTypeDetailDto
)
