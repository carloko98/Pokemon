package de.htwg.se.model.PlayerComponent

trait IPlayer {
  def name: String
  def team: Vector[PokemonInterface] 
  def currentPokemonIndex: Int
  def items: Vector[String]

  def activePokemon: PokemonInterface

  def updatePokemon(p: PokemonInterface): PlayerInterface 
  def isActiveFainted: Boolean
  def isDefeated: Boolean
  def nextAlivePokemonIndex: Option[Int]
  
  def switchActivePokemon(index: Int): PlayerInterface
}