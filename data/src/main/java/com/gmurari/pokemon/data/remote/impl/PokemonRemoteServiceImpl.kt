package com.gmurari.pokemon.data.remote.impl

import com.gmurari.pokemon.data.remote.PokemonRemoteService
import com.gmurari.pokemon.data.remote.api.PokemonApi
import com.gmurari.pokemon.data.remote.dto.PokemonInfoDto
import com.gmurari.pokemon.data.remote.dto.PokemonListDto
import com.gmurari.pokemon.data.remote.dto.PokemonSpeciesDto
import javax.inject.Inject

internal class PokemonRemoteServiceImpl @Inject constructor(
    private val pokemonApi: PokemonApi
) : PokemonRemoteService {
    override suspend fun getPokemonInfo(pokemonName: String): PokemonInfoDto {
        val pokemonInfo = pokemonApi.getPokemonInfo(pokemonName)
        if (!pokemonInfo.isSuccessful) throw Exception("Failed to fetch info for $pokemonName")
        return pokemonInfo.body()
            ?: throw Exception("Pokemon info response body is null for $pokemonName")
    }

    override suspend fun getPokemonList(limit: Int): PokemonListDto {
        val pokemonList = pokemonApi.getPokemonList(limit)
        if (!pokemonList.isSuccessful) throw Exception("Failed to fetch Pokemon list")
        return pokemonList.body() ?: throw Exception("Pokemon list response body is null")
    }

    override suspend fun getPokemonSpecie(id: Int): PokemonSpeciesDto {
        val pokemonSpecies = pokemonApi.getPokemonSpecieById(id)
        if (!pokemonSpecies.isSuccessful) throw Exception("Failed to fetch species for $id")
        return pokemonSpecies.body()
            ?: throw Exception("Pokemon species response body is null for $id")
    }
}