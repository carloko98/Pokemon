package de.htwg.se.model.PokemonComponent.PokemonBaseImpl

import de.htwg.se.model.PokemonComponent.IPokemon
import de.htwg.se.model.PokemonComponent.Attack
import de.htwg.se.model.PokemonComponent.PokemonType._ // Für 'Normal' im Fallback
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PlayerComponent.PlayerBaseImpl.Player // Zugriff auf die Implementierung zum Erstellen

object PokemonFactory {

  // Rückgabetyp: IPokemon (Interface)
  def getPokemon(name: String): IPokemon = {
    PokemonDBS.get(name) match {
      case Some(prototype) => 
        prototype.copy() 
        
      case None => 
        // Fallback
        Pokemon("MissingNo", Normal, 100, 100, Vector(Attack("Verzweifler", 10, Normal)))
    }
  }

  // Rückgabetyp: IPlayer (Interface)
  def createPlayer(name: String, pokemonNames: Vector[String]): IPlayer = {
    // Hier wird das Interface IPokemon erwartet, getPokemon liefert IPokemon
    val team = pokemonNames.map(name => getPokemon(name))
    // Player Konstruktor erwartet vermutlich Vector[IPokemon], das passt.
    Player(name, team)
  }
  
  def createRandomEnemy(): IPlayer = {
    createPlayer("Team Rocket Rüpel", Vector("Bisaflor", "Zubat"))
  }
}