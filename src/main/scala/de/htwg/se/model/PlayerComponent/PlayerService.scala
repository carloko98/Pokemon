package de.htwg.se.model.PlayerComponent

import de.htwg.se.model.PlayerComponent.PlayerBaseImpl.Player
import de.htwg.se.model.PokemonComponent.IPokemon

object PlayerService {
  
  def buildPlayer(name: String, team: Vector[IPokemon]): IPlayer = {
    Player(name, team) 
  }
}