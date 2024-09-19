package com.gmurari.pokemon.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.gmurari.pokemon.domain.repository.PokemonRepository
import com.gmurari.pokemon.data.repository.PokemonRepositoryImpl
import javax.inject.Singleton

/**
 * Module to inject/provide repositories.
 */
@InstallIn(SingletonComponent::class)
@Module
internal abstract class Singleton {

    /**
     * Provide the implementation of [PokemonRepository].
     */
    @Binds
    @Singleton
    abstract fun provideMessageRepository(repository: PokemonRepositoryImpl): PokemonRepository
}
