package de.htwg.se.model.PlayerComponent.MockPlayerImpl

import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PokemonComponent.IPokemon
import de.htwg.se.model.PokemonComponent.MockPokemonImpl.MockPokemon

case class MockPlayer(
    name: String = "MockPlayer",
    team: Vector[IPokemon] = Vector(MockPokemon()),
    currentPokemonIndex: Int = 0,
    items: Vector[String] = Vector.empty
) extends IPlayer {
  
  override def activePokemon: IPokemon = team(currentPokemonIndex)
  
  override def updatePokemon(p: IPokemon): IPlayer = copy(team = team.updated(currentPokemonIndex, p))
  
  override def switchActivePokemon(index: Int): IPlayer = copy(currentPokemonIndex = index)
  
  override def addPokemon(p: IPokemon): IPlayer = copy(team = team :+ p)
  
  override def withTeam(newTeam: Vector[IPokemon]): IPlayer = this
  
  override def isActiveFainted: Boolean = activePokemon.isFainted
  
  override def isDefeated: Boolean = team.forall(_.isFainted)
  
  override def nextAlivePokemonIndex: Option[Int] = team.indexWhere(!_.isFainted) match {
    case -1 => None
    case i => Some(i)
  }
}