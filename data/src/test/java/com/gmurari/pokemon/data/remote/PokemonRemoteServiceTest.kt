package com.gmurari.pokemon.data.remote

import com.gmurari.pokemon.data.remote.api.PokemonApi
import com.gmurari.pokemon.data.remote.dto.PokemonInfoDto
import com.gmurari.pokemon.data.remote.dto.PokemonListDto
import com.gmurari.pokemon.data.remote.dto.PokemonSpeciesDto
import com.gmurari.pokemon.data.remote.impl.PokemonRemoteServiceImpl
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.junit.runner.RunWith
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@RunWith(MockitoJUnitRunner::class)
class PokemonRemoteServiceImplTest {

    @Mock
    private lateinit var pokemonApi: PokemonApi

    private lateinit var pokemonRemoteService: PokemonRemoteService

    @Before
    fun setUp() {
        pokemonRemoteService = PokemonRemoteServiceImpl(pokemonApi)
    }

    @Test
    fun `getPokemonInfo should return PokemonInfoDto when API call is successful`() = runTest {
        // Arrange
        val pokemonName = "bulbasaur"
        val mockResponse = Response.success(MockPokemonRemoteData.mockPokemonInfoDto)

        `when`(pokemonApi.getPokemonInfo(pokemonName)).thenReturn(mockResponse)

        // Act
        val result = pokemonRemoteService.getPokemonInfo(pokemonName)

        // Assert
        assertEquals(MockPokemonRemoteData.mockPokemonInfoDto, result)
        verify(pokemonApi).getPokemonInfo(pokemonName)
    }

    @Test
    fun `getPokemonInfo should throw exception when API call is not successful`() = runTest {
        // Arrange
        val pokemonName = "bulbasaur"
        val mockResponse = Response.error<PokemonInfoDto>(404, mock())

        `when`(pokemonApi.getPokemonInfo(pokemonName)).thenReturn(mockResponse)

        // Act & Assert
        assertFailsWith<Exception>("Failed to fetch info for $pokemonName") {
            pokemonRemoteService.getPokemonInfo(pokemonName)
        }
        verify(pokemonApi).getPokemonInfo(pokemonName)
    }

    @Test
    fun `getPokemonList should return PokemonListDto when API call is successful`() = runTest {
        // Arrange
        val limit = 10
        val offset = 0
        val mockResponse = Response.success(MockPokemonRemoteData.mockPokemonListDto)

        `when`(pokemonApi.getPokemonList(limit, offset)).thenReturn(mockResponse)

        // Act
        val result = pokemonRemoteService.getPokemonList(limit, offset)

        // Assert
        assertEquals(MockPokemonRemoteData.mockPokemonListDto, result)
        verify(pokemonApi).getPokemonList(limit, offset)
    }

    @Test
    fun `getPokemonList should throw exception when API call is not successful`() = runTest {
        // Arrange
        val limit = 10
        val offset = 0
        val mockResponse = Response.error<PokemonListDto>(404, mock())

        `when`(pokemonApi.getPokemonList(limit, offset)).thenReturn(mockResponse)

        // Act & Assert
        assertFailsWith<Exception>("Failed to fetch Pokemon list") {
            pokemonRemoteService.getPokemonList(limit, offset)
        }
        verify(pokemonApi).getPokemonList(limit, offset)
    }

    @Test
    fun `getPokemonSpecie should return PokemonSpeciesDto when API call is successful`() = runTest {
        // Arrange
        val pokemonName = "bulbasaur"
        val mockResponse = Response.success(MockPokemonRemoteData.mockPokemonSpeciesDto)

        `when`(pokemonApi.getPokemonSpecieByName(pokemonName)).thenReturn(mockResponse)

        // Act
        val result = pokemonRemoteService.getPokemonSpecie(pokemonName)

        // Assert
        assertEquals(MockPokemonRemoteData.mockPokemonSpeciesDto, result)
        verify(pokemonApi).getPokemonSpecieByName(pokemonName)
    }

    @Test
    fun `getPokemonSpecie should throw exception when API call is not successful`() = runTest {
        // Arrange
        val pokemonName = "bulbasaur"
        val mockResponse = Response.error<PokemonSpeciesDto>(404, mock())

        `when`(pokemonApi.getPokemonSpecieByName(pokemonName)).thenReturn(mockResponse)

        // Act & Assert
        assertFailsWith<Exception>("Failed to fetch species for $pokemonName") {
            pokemonRemoteService.getPokemonSpecie(pokemonName)
        }
        verify(pokemonApi).getPokemonSpecieByName(pokemonName)
    }
}