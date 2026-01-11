package de.htwg.se.view

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import java.io.ByteArrayOutputStream
import de.htwg.se.controller.{IController, ViewState}
import de.htwg.se.model.PokemonComponent.{IPokemon, PokemonType, Attack}
import de.htwg.se.model.PlayerComponent.PlayerBaseImpl.Player

class TuiSpec extends AnyWordSpec with Matchers {

  case class MockPokemon(
      name: String,
      pType: PokemonType = PokemonType.Normal,
      currentHp: Int = 100,
      maxHp: Int = 100,
      attacks: Vector[Attack] = Vector(Attack("Hit", 10, PokemonType.Normal))
  ) extends IPokemon {
      override def withHp(hp: Int) = copy(currentHp = hp)
      override def isFainted = currentHp <= 0
      override def toString = name
  }

  class MockController extends IController {
    var handledInput: String = ""
    var currentViewState: ViewState = ViewState.VTitle
    var message: (String, String) = ("", "")
    var saves: List[String] = List.empty

    override def viewState: ViewState = currentViewState
    override def handleInput(input: String): Unit = handledInput = input
    override def getMessage: (String, String) = message
    override def getAvailableSaves: List[String] = saves
    
    override def getPlayerPokemon: IPokemon = MockPokemon("PlayerMon")
    override def getEnemyPokemon: IPokemon = MockPokemon("EnemyMon")

    override def add(observer: de.htwg.se.util.Observer): Unit = {}
    override def notifyObservers(): Unit = {}
    override def remove(observer: de.htwg.se.util.Observer): Unit = {}
    override def saveGame(): Unit = {}
    override def loadGame(name: String): Unit = {}
    override def undo(): Unit = {}
    override def redo(): Unit = {}
    
    override def getPlayer = Player("Test", Vector.empty)
    override def getEnemy = Player("Test", Vector.empty)
    override def isBattleOver = false
  }

  "A Tui" should {
    val controller = new MockController()
    val tui = new Tui(controller)

    def captureOutput(block: => Unit): String = {
      val stream = new ByteArrayOutputStream()
      Console.withOut(stream) { block }
      stream.toString
    }

    "render Title screen correctly" in {
      controller.currentViewState = ViewState.VTitle
      val out = captureOutput { tui.update() }
      out should include ("POKEMON SCALA EDITION")
      out should include ("n. Neues Spiel")
    }

    "render Menu screen correctly" in {
      controller.currentViewState = ViewState.VMenu
      val out = captureOutput { tui.render() }
      out should include ("HAUPTMENUE")
      out should include ("s. Wilden Kampf starten")
    }

    "render NameInput screen correctly" in {
      controller.currentViewState = ViewState.VNameInput
      val out = captureOutput { tui.render() }
      out should include ("Wie heisst du, Trainer?")
    }

    "render SelectProfile screen correctly (empty)" in {
      controller.currentViewState = ViewState.VSelectProfile
      controller.saves = List.empty
      val out = captureOutput { tui.render() }
      out should include ("Keine Spielstaende gefunden")
    }

    "render SelectProfile screen correctly (with saves)" in {
      controller.currentViewState = ViewState.VSelectProfile
      controller.saves = List("Save1", "Save2")
      val out = captureOutput { tui.render() }
      out should include ("Verfuegbare Profile")
      out should include ("Save1")
      out should include ("Save2")
    }

    "render PlayerAtk screen correctly" in {
      controller.currentViewState = ViewState.VPlayerAtk
      val out = captureOutput { tui.render() }
      out should include ("Kampf-Aktion waehlen")
      out should include ("PlayerMon")
      out should include ("EnemyMon")
    }

    "render EnemyAtk screen correctly" in {
      controller.currentViewState = ViewState.VEnemyAtk
      val out = captureOutput { tui.render() }
      out should include ("Druecke Enter fuer Gegnerzug")
      out should not include ("Kampf-Aktion waehlen")
    }

    "print intro" in {
       val out = captureOutput { tui.intro() }
       out should include ("... lade Texturen ...")
    }
  }
}