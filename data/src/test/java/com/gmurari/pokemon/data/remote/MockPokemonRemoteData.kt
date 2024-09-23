package com.gmurari.pokemon.data.remote

import com.gmurari.pokemon.data.remote.dto.FlavorTextEntryDto
import com.gmurari.pokemon.data.remote.dto.LanguageDto
import com.gmurari.pokemon.data.remote.dto.PokemonInfoDto
import com.gmurari.pokemon.data.remote.dto.PokemonInfoSpeciesDto
import com.gmurari.pokemon.data.remote.dto.PokemonInfoSpritesDto
import com.gmurari.pokemon.data.remote.dto.PokemonInfoTypeDetailDto
import com.gmurari.pokemon.data.remote.dto.PokemonInfoTypeDto
import com.gmurari.pokemon.data.remote.dto.PokemonListDto
import com.gmurari.pokemon.data.remote.dto.PokemonListItemDto
import com.gmurari.pokemon.data.remote.dto.PokemonSpeciesDto

internal object MockPokemonRemoteData {

    val mockPokemonInfoDto = PokemonInfoDto(
        id = 1,
        name = "bulbasaur",
        height = 7,
        weight = 69,
        species = PokemonInfoSpeciesDto(
            name = "bulbasaur",
            url = "https://pokeapi.co/api/v2/pokemon-species/1/"
        ),
        sprites = PokemonInfoSpritesDto(
            frontDefault = "https://pokeapi.co/api/v2/sprite/bulbasaur/front_default.png"
        ),
        types = listOf(
            PokemonInfoTypeDto(
                slot = 1,
                type = PokemonInfoTypeDetailDto(
                    name = "grass",
                    url = "https://pokeapi.co/api/v2/type/12/"
                )
            ),
            PokemonInfoTypeDto(
                slot = 2,
                type = PokemonInfoTypeDetailDto(
                    name = "poison",
                    url = "https://pokeapi.co/api/v2/type/4/"
                )
            )
        )
    )

    val mockPokemonListDto = PokemonListDto(
        count = 1118,
        results = listOf(
            PokemonListItemDto("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
            PokemonListItemDto("ivysaur", "https://pokeapi.co/api/v2/pokemon/2/")
        )
    )

    val mockPokemonSpeciesDto = PokemonSpeciesDto(
        id = 1,
        name = "bulbasaur",
        flavorTextEntries = listOf(
            FlavorTextEntryDto(
                flavorText = "A strange seed was planted on its back at birth.",
                language = LanguageDto(
                    name = "en",
                    url = "https://pokeapi.co/api/v2/language/9/"
                )
            )
        )
    )
}