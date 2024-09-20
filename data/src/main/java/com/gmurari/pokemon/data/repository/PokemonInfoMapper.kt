package com.gmurari.pokemon.data.repository

import com.gmurari.pokemon.data.local.entity.PokemonInfoEntity
import com.gmurari.pokemon.data.local.entity.PokemonListItemEntity
import com.gmurari.pokemon.data.local.entity.PokemonTypeCrossRef
import com.gmurari.pokemon.data.local.entity.PokemonTypeEntity
import com.gmurari.pokemon.data.local.entity.PokemonWithRelations
import com.gmurari.pokemon.data.remote.dto.PokemonInfoDto
import com.gmurari.pokemon.data.remote.dto.PokemonListDto
import com.gmurari.pokemon.domain.model.Pokemon

internal fun PokemonInfoDto.toPokemonInfoEntity(): PokemonInfoEntity =
    PokemonInfoEntity(
        id = id,
        name = name,
        height = height,
        weight = weight,
        imageUrl = sprites.frontDefault,
        speciesDescription = "" //TODO
    )

internal fun PokemonInfoDto.toPokemonTypeEntityList(): List<PokemonTypeEntity> =
    types.map {
        PokemonTypeEntity(
            name = it.type.name,
            url = it.type.url
        )
    }

internal fun PokemonInfoDto.toPokemonTypeCrossRefList(): List<PokemonTypeCrossRef> =
    types.map {
        PokemonTypeCrossRef(
            pokemonId = id,
            typeName = it.type.name
        )
    }

internal fun PokemonListDto.toPokemonListItemEntity(): List<PokemonListItemEntity> =
    results.map {
        PokemonListItemEntity(
            name = it.name,
            url = it.url
        )
    }

internal fun PokemonWithRelations.toPokemon(): Pokemon =
    Pokemon(
        id = pokemon.id,
        name = pokemon.name,
        height = pokemon.height,
        weight = pokemon.weight,
        types = types.map { it.name },
        imageUrl = pokemon.imageUrl,
        speciesDescription = pokemon.speciesDescription
    )