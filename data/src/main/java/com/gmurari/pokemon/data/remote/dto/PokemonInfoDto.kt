package com.gmurari.pokemon.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonInfoDto(
    val id: Int, // 1
    val name: String, // "bulbasaur"
    val height: Int, // 7
    val weight: Int, // 69
    val species: PokemonInfoSpeciesDto,
    val types: List<PokemonInfoTypeDto>,
)
