package com.gmurari.pokemon.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.gmurari.pokemon.data.local.PokemonLocalService
import com.gmurari.pokemon.data.local.db.PokemonDatabase
import com.gmurari.pokemon.data.local.impl.PokemonLocalServiceImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.*
import kotlin.test.assertEquals

class PokemonLocalServiceTest {

    // Usato per eseguire le operazioni di Room sincronicamente nel thread principale
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var pokemonDatabase: PokemonDatabase
    private lateinit var pokemonLocalService: PokemonLocalService

    @Before
    fun setUp() {
        // Crea un database in memoria per i test
        pokemonDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PokemonDatabase::class.java
        ).allowMainThreadQueries().build()

        // Inizializza il servizio locale
        pokemonLocalService = PokemonLocalServiceImpl(pokemonDatabase)
    }

    @After
    fun tearDown() {
        // Chiudi il database al termine di ogni test
        pokemonDatabase.close()
    }

    @Test
    fun store_and_retrieve_PokemonInfoEntity_and_relations() = runTest {
        // Arrange
        val pokemonInfoEntity = PokemonMockLocalData.pokemonInfoEntity
        val pokemonTypeEntity = PokemonMockLocalData.pokemonTypeEntity
        val crossRef = PokemonMockLocalData.pokemonTypeCrossRef
        val pokemonSpeciesEntity = PokemonMockLocalData.pokemonSpeciesEntity

        // Act: salva i dati nel database
        pokemonLocalService.storePokemonInfo(pokemonInfoEntity, listOf(pokemonTypeEntity), listOf(crossRef))
        pokemonLocalService.storePokemonSpecies(pokemonSpeciesEntity)

        // Assert: recupera i dati e verifica che siano corretti
        val retrievedPokemon = pokemonLocalService.getPokemonInfoList("", 0, 10).first()

        assertEquals(1, retrievedPokemon.size)
        assertEquals(pokemonInfoEntity, retrievedPokemon[0].pokemon)
        assertEquals(listOf(pokemonTypeEntity), retrievedPokemon[0].types)
        assertEquals(pokemonSpeciesEntity, retrievedPokemon[0].species)
    }

    @Test
    fun store_and_retrieve_PokemonListItemEntity() = runTest {
        // Arrange
        val pokemonListItem = PokemonMockLocalData.pokemonListItemEntity

        // Act: salva l'elemento nella lista di Pokémon
        pokemonLocalService.storePokemonListItems(listOf(pokemonListItem))

        // Assert: recupera l'elemento dalla lista
        val retrievedList = pokemonLocalService.getPokemonList("", 0, 10).first()

        assertEquals(1, retrievedList.size)
        assertEquals(pokemonListItem, retrievedList[0])
    }

    @Test
    fun clear_all_PokemonInfo_data() = runTest {
        // Arrange
        val pokemonInfoEntity = PokemonMockLocalData.pokemonInfoEntity
        val pokemonTypeEntity = PokemonMockLocalData.pokemonTypeEntity
        val crossRef = PokemonMockLocalData.pokemonTypeCrossRef

        pokemonLocalService.storePokemonInfo(pokemonInfoEntity, listOf(pokemonTypeEntity), listOf(crossRef))

        // Act: pulisci i dati
        pokemonLocalService.clearPokemonInfo()

        // Assert: verifica che i dati siano stati rimossi
        val retrievedPokemon = pokemonLocalService.getPokemonInfoList("", 0, 10).first()

        assertEquals(0, retrievedPokemon.size)
    }

    @Test
    fun test_paging_source() = runTest {
        // Arrange
        val pokemonInfoEntity = PokemonMockLocalData.pokemonInfoEntity
        val pokemonTypeEntity = PokemonMockLocalData.pokemonTypeEntity
        val pokeonSpeciesEntity = PokemonMockLocalData.pokemonSpeciesEntity
        val crossRef = PokemonMockLocalData.pokemonTypeCrossRef

        pokemonLocalService.storePokemonSpecies(pokeonSpeciesEntity)
        pokemonLocalService.storePokemonInfo(pokemonInfoEntity, listOf(pokemonTypeEntity), listOf(crossRef))

        // Act: recupera il PagingSource
        val pagingSource = pokemonLocalService.getPokemonPagingSource("")

        // Assert: verifica che il PagingSource restituisca i dati corretti
        val pagingResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        assert(pagingResult is PagingSource.LoadResult.Page)
        val page = pagingResult as PagingSource.LoadResult.Page
        assertEquals(1, page.data.size)
        assertEquals(pokemonInfoEntity, page.data[0].pokemon)
    }
}
