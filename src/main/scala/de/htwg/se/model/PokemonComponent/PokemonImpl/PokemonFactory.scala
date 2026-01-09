package de.htwg.se.model.PokemonComponent.PokemonImpl

import de.htwg.se.model.PokemonComponent.{IntPokemon, IntAttack, IntPokemonType, PokemonType}
import de.htwg.se.model.PokemonComponent.PokemonType._
import de.htwg.se.model.PlayerComponent.IntPlayer
import de.htwg.se.model.PlayerComponent.PlayerImpl.Player


object PokemonFactory {

  
  def getPokemon(name: String): IntPokemon = {
    PokemonDBS.get(name) match {
      case Some(prototype) => 
        prototype.asInstanceOf[Pokemon].copy() 
        
      case None => 
        // Fallback
        Pokemon("MissingNo", Normal, 100, 100, Vector(Attack("Verzweifler", 10, Normal)))
    }
  }

  // Rückgabetyp: IntPlayer
  def createPlayer(name: String, pokemonNames: Vector[String]): IntPlayer = {
    val team = pokemonNames.map(name => getPokemon(name))
    Player(name, team)
  }

  def createRandomEnemy(): IntPlayer = {
    createPlayer("Team Rocket Rüpel", Vector("Bisaflor", "Zubat"))
  }
}