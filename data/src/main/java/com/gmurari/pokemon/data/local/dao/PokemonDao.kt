package com.gmurari.pokemon.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.gmurari.pokemon.data.local.entity.PokemonInfoEntity
import com.gmurari.pokemon.data.local.entity.PokemonTypeCrossRef
import com.gmurari.pokemon.data.local.entity.PokemonTypeEntity
import com.gmurari.pokemon.data.local.entity.PokemonWithRelations
import kotlinx.coroutines.flow.Flow

@Dao
internal interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPokemonInfo(pokemonInfo: PokemonInfoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonTypeCrossRefs(crossRefs: List<PokemonTypeCrossRef>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPokemonTypes(pokemonTypes: List<PokemonTypeEntity>)

    @Transaction
    @Query("SELECT * FROM pokemon_info WHERE LOWER(name) LIKE '%' || LOWER(:search) || '%' LIMIT :limit OFFSET :offset")
    fun getPokemonList(search: String, offset: Int, limit: Int): Flow<List<PokemonWithRelations>>

    @Query("DELETE FROM pokemon_info")
    fun clearPokemonInfo()

    @Query("DELETE FROM pokemon_type")
    fun clearPokemonTypes()

    @Query("DELETE FROM pokemon_type_cross_ref")
    fun clearPokemonTypeCrossRef()

}