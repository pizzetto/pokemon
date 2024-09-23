# Pokemon List Sample app
An Android Appication that show a list of Pokemon from PokeAPI

## Notes
I have created two different app flows, one that uses Paging3 library and remote mediator. This flow 
does not allows users to filter Pokemons by type

To use the first flow one has to call 
```
PokemonRepository.downloadPokemonList()
```

And then navigate to **HomeWithPager**

The secondo flow download all data at once before displaying the Pokemon list in order to let the 
user search even by Pokemon type.
Here is also used a custom paginator


To use the second flow one has to call
```
PokemonRepository.downloadAllPokemonData()
```

And then navigate to **Home**