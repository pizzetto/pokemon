package com.gmurari.pokemon.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_info")
data class PokemonInfoEntity(
    @PrimaryKey
    val id: Int, // 1
    val name: String, // "bulbasaur"
    val height: Int, // 7
    val weight: Int, // 69
    val imageUrl: String?,
    val speciesDescription: String
)
