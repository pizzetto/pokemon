package com.gmurari.pokemon.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "pokemon_type_cross_ref",
    foreignKeys = [
        ForeignKey(
            entity = PokemonInfoEntity::class,
            parentColumns = ["id"],
            childColumns = ["pokemonId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PokemonTypeEntity::class,
            parentColumns = ["name"],
            childColumns = ["typeName"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PokemonTypeCrossRef(
    val pokemonId: Int,
    val typeName: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)