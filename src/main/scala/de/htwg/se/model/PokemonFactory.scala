package de.htwg.se.model

import de.htwg.se.model.PokemonType._
import de.htwg.se.model.PlayerComponent.IPlayer

object PokemonFactory {

  // Rückgabetyp: PokemonInterface
  def getPokemon(name: String): IPokemon = {
    PokemonDBS.get(name) match {
      case Some(prototype) => 
        prototype.copy() 
        
      case None => 
        // Fallback
        Pokemon("MissingNo", Normal, 100, 100, Vector(Attack("Verzweifler", 10, Normal)))
    }
  }

  // Rückgabetyp: PlayerInterface
  def createPlayer(name: String, pokemonNames: Vector[String]): IPlayer = {
    val team = pokemonNames.map(name => getPokemon(name))
    Player(name, team)
  }
  
  def createRandomEnemy(): IPlayer = {
    createPlayer("Team Rocket Rüpel", Vector("Bisaflor", "Zubat"))
  }
}