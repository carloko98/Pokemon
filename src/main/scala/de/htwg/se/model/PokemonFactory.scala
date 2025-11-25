package de.htwg.se.model

import de.htwg.se.model.PokemonType._

object PokemonFactory {

  def getPokemon(name: String): Pokemon = {
    PokemonDBS.get(name) match {
      case Some(prototype) => 
        prototype.copy() 
        
      case None => 
        // Fallback / Null Object Pattern für unbekannte Namen
        Pokemon("MissingNo", Normal, 100, 100, Vector(Attack("Verzweifler", 10, Normal)))
    }
  }

  
  def createPlayer(name: String, pokemonNames: Vector[String]): Player = {
    val team = pokemonNames.map(name => getPokemon(name))
    Player(name, team)
  }
  
  // spaeter zu random machen
  def createRandomEnemy(): Player = {
    createPlayer("Team Rocket Rüpel", Vector("Rattfratz", "Zubat"))
  }
}