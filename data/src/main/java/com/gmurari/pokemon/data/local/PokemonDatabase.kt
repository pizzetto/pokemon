package com.gmurari.pokemon.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gmurari.pokemon.data.local.dao.PokemonDao
import com.gmurari.pokemon.data.local.entity.PokemonInfoEntity
import com.gmurari.pokemon.data.local.entity.PokemonTypeCrossRef
import com.gmurari.pokemon.data.local.entity.PokemonTypeEntity

@Database(
    entities = [
        PokemonInfoEntity::class,
        PokemonTypeEntity::class,
        PokemonTypeCrossRef::class
    ],
    version = 1
)
internal abstract class PokemonDatabase: RoomDatabase() {
    abstract val dao: PokemonDao

    companion object {
        const val DATABASE_NAME = "pokemon_db"
    }
}