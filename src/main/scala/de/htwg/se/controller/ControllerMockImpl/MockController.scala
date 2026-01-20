package de.htwg.se.controller.ControllerMockImpl

import de.htwg.se.controller.{IController, ViewState}
import de.htwg.se.util.{Observable, Observer}
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PokemonComponent.{IPokemon, Attack, PokemonType}

class MockController extends IController {

  var _viewState: ViewState = ViewState.VMenu
  var _msg: (String, String) = ("TestTitel", "TestInfo")
  
  def setViewState(vs: ViewState): Unit = {
    _viewState = vs
    notifyObservers()
  }

  override def viewState: ViewState = _viewState
  override def handleInput(input: String): Unit = {}
  override def getMessage: (String, String) = _msg
  override def isBattleOver: Boolean = false
  override def getAvailableSaves: List[String] = List("MockSave1", "MockSave2")

  override def undo(): Unit = {}
  override def redo(): Unit = {}
  override def saveGame(): Unit = {}
  override def loadGame(name: String): Unit = {}

  override def add(o: Observer): Unit = {}
  override def remove(o: Observer): Unit = {}
  override def notifyObservers(): Unit = {}

  private val dummyPokemon = new IPokemon {
    override def name: String = "TestMon"
    override def id: Int = 1
    override def currentHp: Int = 100
    override def maxHp: Int = 100
    override def pType: PokemonType = PokemonType.Normal 
    override def secondaryType: Option[PokemonType] = None
    override def spriteUrl: String = ""
    override def attacks: Vector[Attack] = Vector(Attack("TestAttack", 10, PokemonType.Normal))
    override def withHp(newHp: Int): IPokemon = this
    override def isFainted: Boolean = false
    override def toString: String = "TestMon"
  }

  private val dummyPlayer = new IPlayer {
    override def name: String = "TestPlayer"
    override def team: Vector[IPokemon] = Vector(dummyPokemon)
    override def activePokemon: IPokemon = dummyPokemon
    override def updatePokemon(p: IPokemon): IPlayer = this
    override def isActiveFainted: Boolean = false
    override def currentPokemonIndex: Int = 0 
    override def items: Vector[String] = Vector.empty 
    override def isDefeated: Boolean = false 
    override def nextAlivePokemonIndex: Option[Int] = None 
    override def switchActivePokemon(index: Int): IPlayer = this 
    override def addPokemon(p: IPokemon): IPlayer = this 
    override def withTeam(newTeam: Vector[IPokemon]): IPlayer = this
  }

  override def getPlayer: IPlayer = dummyPlayer
  override def getEnemy: IPlayer = dummyPlayer
  override def getPlayerPokemon: IPokemon = dummyPokemon
  override def getEnemyPokemon: IPokemon = dummyPokemon
  
}