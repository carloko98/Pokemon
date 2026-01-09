package de.htwg.se.model.PlayerComponent

import de.htwg.se.model.PokemonComponent.IntPokemon

trait IntPlayer {
  def name: String
  def team: Vector[IntPokemon]
  def currentPokemonIndex: Int
  def items: Vector[String]

  def activePokemon: IntPokemon
  def updatePokemon(newPokemon: IntPokemon): IntPlayer
  def switchActivePokemon(index: Int): IntPlayer
  def addPokemon(pokemon: IntPokemon): IntPlayer

  def isActiveFainted: Boolean
  def isDefeated: Boolean
  def nextAlivePokemonIndex: Option[Int]

  override def toString: String = name
}