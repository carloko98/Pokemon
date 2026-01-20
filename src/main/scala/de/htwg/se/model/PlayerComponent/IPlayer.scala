package de.htwg.se.model.PlayerComponent

import de.htwg.se.model.PokemonComponent.IPokemon

trait IPlayer {
  def name: String
  def team: Vector[IPokemon]
  def currentPokemonIndex: Int 
  def items: Vector[String]    
  
  def activePokemon: IPokemon
  def isActiveFainted: Boolean
  def isDefeated: Boolean      
  def nextAlivePokemonIndex: Option[Int] 


  def updatePokemon(newPokemon: IPokemon): IPlayer
  def switchActivePokemon(index: Int): IPlayer 
  def addPokemon(p: IPokemon): IPlayer         
  def withTeam(newTeam: Vector[IPokemon]): IPlayer
}