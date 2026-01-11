package de.htwg.se.controller.MockControllerImpl

// Imports
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

  // --- IController Implementierung ---

  override def viewState: ViewState = _viewState
  override def handleInput(input: String): Unit = {} // Tut nichts
  override def getMessage: (String, String) = _msg
  override def isBattleOver: Boolean = false
  override def getAvailableSaves: List[String] = List("MockSave1", "MockSave2")

  override def undo(): Unit = {}
  override def redo(): Unit = {}
  override def saveGame(): Unit = {}
  override def loadGame(name: String): Unit = {}

  // --- Dummy Objekte (Hier lag das Problem) ---

  // 1. Ein Dummy Pokemon
  private val dummyPokemon = new IPokemon {
    override def name: String = "TestMon"
    override def currentHp: Int = 100
    override def maxHp: Int = 100
    override def pType: PokemonType = null 
    override def attacks: Vector[Attack] = Vector.empty
    override def withHp(newHp: Int): IPokemon = this
    override def isFainted: Boolean = false
    override def toString: String = "TestMon"
  }

  // 2. Ein Dummy Player, der ALLE Methoden von deinem Interface implementiert
  private val dummyPlayer = new IPlayer {
    override def name: String = "TestPlayer"
    override def team: Vector[IPokemon] = Vector(dummyPokemon)
    
    // Alte Methoden
    override def activePokemon: IPokemon = dummyPokemon
    override def updatePokemon(p: IPokemon): IPlayer = this
    override def isActiveFainted: Boolean = false
    
    // --- NEUE METHODEN (STUBS) ---
    
    // Gibt einfach 0 zurück
    override def currentPokemonIndex: Int = 0 
    
    // Gibt leere Liste zurück
    override def items: Vector[String] = Vector.empty 
    
    // Sagt immer "Nein, nicht besiegt"
    override def isDefeated: Boolean = false 
    
    // Sagt "Kein nächstes Pokemon da"
    override def nextAlivePokemonIndex: Option[Int] = None 
    
    // Tut so, als würde gewechselt, gibt aber einfach sich selbst zurück
    override def switchActivePokemon(index: Int): IPlayer = this 
    
    // Tut so, als würde hinzugefügt, gibt aber sich selbst zurück
    override def addPokemon(p: IPokemon): IPlayer = this 
  }

  // Rückgabe der Dummies
  override def getPlayer: IPlayer = dummyPlayer
  override def getEnemy: IPlayer = dummyPlayer
  override def getPlayerPokemon: IPokemon = dummyPokemon
  override def getEnemyPokemon: IPokemon = dummyPokemon
}