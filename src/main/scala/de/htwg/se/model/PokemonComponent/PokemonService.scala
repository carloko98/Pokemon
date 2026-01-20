package de.htwg.se.model.PokemonComponent

import de.htwg.se.model.PokemonComponent.PokemonBaseImpl.PokemonFactory 
import de.htwg.se.model.PlayerComponent.IPlayer

object PokemonService {
  
   // Bestehende Methoden
   def createRandomPlayer(name: String): IPlayer = 
      PokemonFactory.createRandomPlayer(name)

   def createWildEnemy(): IPlayer = 
      PokemonFactory.createWildEnemy()

   def createRandomEnemy(): IPlayer = 
      PokemonFactory.createWildEnemy()

   def createTrainerEnemy(): IPlayer = 
      PokemonFactory.createTrainerEnemy()
      
   // Hilfsmethode, falls du mal ein einzelnes Pokemon brauchst
   def getPokemon(name: String): IPokemon = {
      PokemonFactory.getPokemon(name)
   }
}