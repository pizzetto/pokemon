package com.gmurari.pokemon.domain.usecase

import com.gmurari.pokemon.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    operator fun invoke(searchString: String, limit: Int, page: Int) =
        pokemonRepository.getPokemonList(searchString, limit, (page - 1) * limit)

}