package com.gmurari.pokemon.domain.usecase

import com.gmurari.pokemon.domain.repository.AsyncOp
import com.gmurari.pokemon.domain.repository.PokemonRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DownloadPokemonListUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    operator fun invoke(): Flow<AsyncOp<Unit>> = flow {
        delay(1000)
        emit(AsyncOp.Loading(true, 0.0))
        emit(AsyncOp.Success(Unit))
        emit(AsyncOp.Loading(false, 100.0))
    }
        //pokemonRepository.downloadPokemonList()
}