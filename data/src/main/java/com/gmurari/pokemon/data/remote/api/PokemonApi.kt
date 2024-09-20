package com.gmurari.pokemon.data.remote.api

import com.gmurari.pokemon.data.remote.dto.PokemonInfoDto
import com.gmurari.pokemon.data.remote.dto.PokemonListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface PokemonApi {

    @GET("pokemon")
    suspend fun getPokemonList(@Query("limit") limit: Int): Response<PokemonListDto>

    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(@Path("name") name: String): Response<PokemonInfoDto>

    companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2/"
    }
}