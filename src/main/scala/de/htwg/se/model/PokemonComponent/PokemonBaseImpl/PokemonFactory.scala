package de.htwg.se.model.PokemonComponent.PokemonBaseImpl

import de.htwg.se.model.PokemonComponent.IPokemon
import de.htwg.se.model.PokemonComponent.Attack
import de.htwg.se.model.PokemonComponent.PokemonType
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PlayerComponent.PlayerBaseImpl.Player

object PokemonFactory {

  // Holt ein spezifisches Pokemon (oder MissingNo)
  def getPokemon(name: String): IPokemon = {
    PokemonDBS.get(name) match {
      case Some(prototype) => prototype.copy()
      case None => 
        // Fallback, falls JSON-Name nicht gefunden wird
        Pokemon(
            name = "MissingNo", 
            id = 0,                         // <--- NEU (Dummy ID)
            pType = PokemonType.Normal, 
            secondaryType = None,           // <--- NEU (Kein zweiter Typ)
            maxHp = 100, 
            currentHp = 100, 
            attacks = Vector(Attack("Verzweifler", 10, PokemonType.Normal)),
            spriteUrl = ""                  // <--- NEU (Kein Bild)
        )
    }
  }

  // Erstellt einen Spieler mit expliziten Pokemon-Namen
  def createPlayer(name: String, pokemonNames: Vector[String]): IPlayer = {
    val team = pokemonNames.map(name => getPokemon(name))
    Player(name, team)
  }
  

  def createRandomPlayer(name: String): IPlayer = {
    val p1 = PokemonDBS.getRandom
    val p2 = PokemonDBS.getRandom
    Player(name, Vector(p1, p2))
  }

  def createWildEnemy(): IPlayer = {
    val wildMon = PokemonDBS.getRandom
    Player("Wildes " + wildMon.name, Vector(wildMon))
  }

  // Erstellt einen Trainer Gegner (2 Pokemon)
  def createTrainerEnemy(): IPlayer = {
    val p1 = PokemonDBS.getRandom
    val p2 = PokemonDBS.getRandom
    Player("Team Rocket Rüpel", Vector(p1, p2))
  }
}