package com.gmurari.pokemon.domain.usecase

import com.gmurari.pokemon.domain.repository.AsyncOp
import com.gmurari.pokemon.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DownloadPokemonListUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    operator fun invoke(): Flow<AsyncOp<Unit>> =
        pokemonRepository.downloadPokemonList()
}