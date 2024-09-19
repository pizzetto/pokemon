package com.gmurari.pokemon.data.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PokemonWithRelations(
    @Embedded val pokemon: PokemonInfoEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "name",
        associateBy = Junction(
            value = PokemonTypeCrossRef::class,
            parentColumn = "pokemonId",
            entityColumn = "typeName"
        )
    )
    val types: List<PokemonTypeEntity>
)