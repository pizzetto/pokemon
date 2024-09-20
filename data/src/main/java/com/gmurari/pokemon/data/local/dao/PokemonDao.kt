package com.gmurari.pokemon.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.gmurari.pokemon.data.local.entity.PokemonInfoEntity
import com.gmurari.pokemon.data.local.entity.PokemonListItemEntity
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

    @Upsert
    suspend fun insertPokemonList(pokemonList: List<PokemonListItemEntity>)

    @Transaction
    @Query(
        """
    SELECT * FROM pokemon_info 
    WHERE 
        name LIKE :search || '%'OR :search = '' 
        OR id IN (SELECT pokemonId FROM pokemon_type_cross_ref WHERE typeName LIKE '%' || :search || '%') 
    LIMIT :limit OFFSET :offset
    """
    )
    fun getPokemonInfoList(search: String, offset: Int, limit: Int): Flow<List<PokemonWithRelations>>

    @Query(
        """
            SELECT * FROM pokemon_info
    WHERE 
        name LIKE :search || '%'OR :search = '' 
        OR id IN (SELECT pokemonId FROM pokemon_type_cross_ref WHERE typeName LIKE '%' || :search || '%') 
        """
    )
    fun pagingSource(search: String): PagingSource<Int, PokemonWithRelations>

    @Query(
        """
            SELECT * FROM pokemon_list 
    WHERE 
        name LIKE :search || '%'OR :search = '' 
        OR id IN (SELECT pokemonId FROM pokemon_type_cross_ref WHERE typeName LIKE '%' || :search || '%') 
        LIMIT :limit OFFSET :offset
        """
    )
    fun getPokemonList(search: String, offset: Int, limit: Int): Flow<List<PokemonListItemEntity>>

    @Transaction
    @Query("SELECT * FROM pokemon_info WHERE id = :id")
    fun getPokemon(id: Int): Flow<PokemonWithRelations>

    @Query("DELETE FROM pokemon_list")
    fun clearPokemonList()

    @Query("DELETE FROM pokemon_info")
    fun clearPokemonInfo()

    @Query("DELETE FROM pokemon_type")
    fun clearPokemonTypes()

    @Query("DELETE FROM pokemon_type_cross_ref")
    fun clearPokemonTypeCrossRef()

}