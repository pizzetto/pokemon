package com.gmurari.pokemon.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonInfoTypeDetailDto(
    val name: String, // "grass"
    val url: String // "https://pokeapi.co/api/v2/type/12/"
)
