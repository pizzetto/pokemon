package com.gmurari.pokemon.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_type")
data class PokemonTypeEntity(
    @PrimaryKey
    val name: String,
    val url: String
)
