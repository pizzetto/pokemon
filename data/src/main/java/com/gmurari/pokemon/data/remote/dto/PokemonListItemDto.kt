package com.gmurari.pokemon.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class PokemonListItemDto(
    val name: String, // "bulbasaur"
    val url: String // "https://pokeapi.co/api/v2/pokemon/1/"
)
