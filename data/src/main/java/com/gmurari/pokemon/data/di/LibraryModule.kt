package com.gmurari.pokemon.data.di

import android.app.Application
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.gmurari.pokemon.data.local.PokemonLocalService
import com.gmurari.pokemon.data.local.db.PokemonDatabase
import com.gmurari.pokemon.data.remote.PokemonRemoteService
import com.gmurari.pokemon.data.remote.api.PokemonApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object LibraryModule {

    @Provides
    @Singleton
    fun providePokemonDatabase(app: Application): PokemonDatabase {

        return Room.databaseBuilder(
            app,
            PokemonDatabase::class.java,
            PokemonDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePokemonApi(@Named("baseUrl") baseUrl: String, interceptor: Interceptor): PokemonApi {

        val moshi = Moshi.Builder()
            .build()

        val client = OkHttpClient.Builder()
            // Si possono aggiungere interceptor ed eventuale gestione della cache (se necessario)
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create()
    }

    @Provides
    @Singleton
    @Named("baseUrl")
    fun provideBaseUrl(): String {
        return PokemonApi.BASE_URL
    }

    /**
     * Provide the implementation of [PokemonDao].
     */
    @Provides
    @Singleton
    fun providePokemonDao(pokemonDatabase: PokemonDatabase) = pokemonDatabase.dao


    /**
     * Provide the implementation of [Interceptor].
     */
    @Provides
    @Singleton
    fun provideOkHttpInterceptor(): Interceptor {

        //Eventualmente distinguere l'interceptor per debug e release
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return logger
    }
}