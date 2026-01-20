package de.htwg.se.controller

import de.htwg.se.util.Observable
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PokemonComponent.IPokemon

trait IController extends Observable {
  
  def viewState: ViewState
  def handleInput(input: String): Unit
  def getMessage: (String, String)
  def isBattleOver: Boolean
  def getAvailableSaves: List[String]
  def undo(): Unit
  def redo(): Unit
  def saveGame(): Unit
  def loadGame(name: String): Unit
  def getPlayer: IPlayer
  def getEnemy: IPlayer
  def getPlayerPokemon: IPokemon
  def getEnemyPokemon: IPokemon
}