package de.htwg.se.controller

import de.htwg.se.util.Observable
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PokemonComponent.IPokemon

trait IController extends Observable {

  def handleInput(input: String): Unit
  def undo(): Unit
  def redo(): Unit
  def saveGame(): Unit
  def loadGame(name: String): Unit

  //  Statt internem ControllerState nur den ViewState zurückgeben
  def viewState: ViewState
  
  def getAvailableSaves: List[String]
  def getPlayer: IPlayer
  def getEnemy: IPlayer
  def getPlayerPokemon: IPokemon
  def getEnemyPokemon: IPokemon
  def isBattleOver: Boolean
  def getMessage: (String, String)
}