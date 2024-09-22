package com.gmurari.pokemon.domain.model

data class Pokemon(
    val id: Int, // 1
    val name: String, // "bulbasaur"
    val height: Int, // 7
    val weight: Int, // 69
    val imageUrl: String?,
    val types: List<String>,
    val speciesDescription: String?
)
