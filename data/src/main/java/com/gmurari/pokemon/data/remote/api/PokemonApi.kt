package com.gmurari.pokemon.data.remote.api

import com.gmurari.pokemon.data.remote.dto.PokemonInfoDto
import com.gmurari.pokemon.data.remote.dto.PokemonListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface PokemonApi {

    @GET("pokemon?limit={limit}")
    fun getPokemonList(@Query("limit") limit: Int): Response<PokemonListDto>

    @GET("pokemon/{name}")
    fun getPokemonInfo(@Query("name") name: String): Response<PokemonInfoDto>

}