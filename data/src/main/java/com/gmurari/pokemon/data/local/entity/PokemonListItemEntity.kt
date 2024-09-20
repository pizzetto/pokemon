package com.gmurari.pokemon.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_list")
data class PokemonListItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String, // "bulbasaur"
    val url: String // "https://pokeapi.co/api/v2/pokemon/1/"
)
