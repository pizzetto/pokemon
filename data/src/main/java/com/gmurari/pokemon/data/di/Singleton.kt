package com.gmurari.pokemon.data.di

import com.gmurari.pokemon.data.local.PokemonLocalService
import com.gmurari.pokemon.data.local.impl.PokemonLocalServiceImpl
import com.gmurari.pokemon.data.remote.PokemonRemoteService
import com.gmurari.pokemon.data.remote.impl.PokemonRemoteServiceImpl
import com.gmurari.pokemon.data.repository.PokemonRepositoryImpl
import com.gmurari.pokemon.domain.repository.PokemonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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

    /**
     * Provide the implementation of [PokemonRemoteService].
     */
    @Binds
    @Singleton
    abstract fun providePokemonRemoteService(service: PokemonRemoteServiceImpl): PokemonRemoteService

    /**
     * Provide the implementation of [PokemonLocalService].
     */
    @Binds
    @Singleton
    abstract fun providePokemonLocalService(service: PokemonLocalServiceImpl): PokemonLocalService
}
