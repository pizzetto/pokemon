package com.gmurari.pokemon.data.remote

import com.gmurari.pokemon.data.remote.dto.PokemonInfoDto
import com.gmurari.pokemon.data.remote.dto.PokemonListDto
import com.gmurari.pokemon.data.remote.dto.PokemonSpeciesDto

internal interface PokemonRemoteService {
    suspend fun getPokemonInfo(pokemonName: String): PokemonInfoDto
    suspend fun getPokemonList(limit: Int, offset: Int = 0): PokemonListDto
    suspend fun getPokemonSpecie(id: Int): PokemonSpeciesDto
}