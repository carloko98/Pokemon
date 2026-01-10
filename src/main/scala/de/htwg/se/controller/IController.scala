package de.htwg.se.controller

import de.htwg.se.util.Observable
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PokemonComponent.IPokemon

// Neuer Enum/Sealed Trait für den View-Status (Entkopplung von Logik-States)
sealed trait ViewState
case object TitleState extends ViewState
case object MenuState extends ViewState
case object PlayerAttackState extends ViewState
case object EnemyAttackState extends ViewState
case object NameInputState extends ViewState
case object SelectProfileState extends ViewState

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