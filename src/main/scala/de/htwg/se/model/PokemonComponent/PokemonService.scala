package de.htwg.se.model.PokemonComponent
import de.htwg.se.model.PokemonComponent.PokemonBaseImpl.PokemonFactory 
import de.htwg.se.model.PlayerComponent.IPlayer

object PokemonService {
   def createPlayer(name: String, pokemon: Vector[String]): IPlayer = 
      PokemonFactory.createPlayer(name, pokemon)

   def createRandomEnemy(): IPlayer = 
      PokemonFactory.createRandomEnemy()

   def getPokemon(name: String): IPokemon = {
    PokemonFactory.getPokemon(name)
  }
}