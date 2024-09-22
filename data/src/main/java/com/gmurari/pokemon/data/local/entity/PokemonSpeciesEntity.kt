package com.gmurari.pokemon.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_species")
data class PokemonSpeciesEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String?

)
