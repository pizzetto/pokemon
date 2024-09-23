package com.gmurari.pokemon.data

import com.gmurari.pokemon.data.local.entity.PokemonInfoEntity
import com.gmurari.pokemon.data.local.entity.PokemonListItemEntity
import com.gmurari.pokemon.data.local.entity.PokemonSpeciesEntity
import com.gmurari.pokemon.data.local.entity.PokemonTypeCrossRef
import com.gmurari.pokemon.data.local.entity.PokemonTypeEntity

object PokemonMockLocalData {

    val pokemonInfoEntity = PokemonInfoEntity(
        id = 1,
        name = "bulbasaur",
        height = 7,
        weight = 69,
        imageUrl = null,
        speciesName = "bulbasaur"
    )

    val pokemonTypeEntity = PokemonTypeEntity(
        name = "grass",
        url = "https://pokeapi.co/api/v2/type/12/"
    )

    val pokemonTypeCrossRef = PokemonTypeCrossRef(
        pokemonId = 1,
        typeName = "grass"
    )

    val pokemonSpeciesEntity = PokemonSpeciesEntity(
        id = 1,
        name = "bulbasaur",
        description = "Seed Pokémon"
    )

    val pokemonListItemEntity = PokemonListItemEntity(
        id = 1,
        name = "bulbasaur",
        url = "https://pokeapi.co/api/v2/pokemon/1/"
    )
}
