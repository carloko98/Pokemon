package de.htwg.se.model.PlayerComponent.PlayerImpl

import de.htwg.se.model.PlayerComponent.IntPlayer
import de.htwg.se.model.PokemonComponent.IntPokemon as Pokemon

case class Player(
    name: String,
    team: Vector[Pokemon] = Vector.empty,
    currentPokemonIndex: Int = 0,
    items: Vector[String] = Vector.empty
) extends IntPlayer {

  override def activePokemon: Pokemon = {
    if (team.isEmpty) {
      throw new IllegalStateException("Kein Pokémon im Team vorhanden")
    }
    team(currentPokemonIndex)
  }

  override def updatePokemon(newPokemon: Pokemon): Player = {
    if (team.isEmpty) {
      throw new IllegalStateException("Kein Pokémon zum Updaten vorhanden")
    }
    val newTeam = team.updated(currentPokemonIndex, newPokemon)
    copy(team = newTeam)
  }

  override def switchActivePokemon(index: Int): Player = {
    if (index < 0 || index >= team.size) {
      throw new IllegalArgumentException(
        s"Ungültiger Pokémon-Index: $index (Team-Größe: ${team.size})"
      )
    }
    copy(currentPokemonIndex = index)
  }

  override def addPokemon(pokemon: Pokemon): Player =
    copy(team = team :+ pokemon)

  override def isActiveFainted: Boolean =
    activePokemon.isFainted

  override def isDefeated: Boolean =
    team.forall(_.isFainted)

  override def nextAlivePokemonIndex: Option[Int] = {
    val index = team.indexWhere(!_.isFainted)
    if (index >= 0) Some(index) else None
  }

  override def toString: String = name
}