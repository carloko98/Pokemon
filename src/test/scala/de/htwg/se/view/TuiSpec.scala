package de.htwg.se.view

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import java.io.ByteArrayOutputStream
import de.htwg.se.controller.ViewState
import de.htwg.se.controller.ControllerMockImpl.MockController

class TuiSpec extends AnyWordSpec with Matchers {

  // 1. Definiere eine lokale Klasse, die testSaves offiziell hat
  class TuiMockController extends MockController {
    var testSaves: List[String] = List.empty
    override def getAvailableSaves: List[String] = testSaves
  }

  // 2. Nutze diese Klasse (Der Typ ist jetzt TuiMockController)
  val controller = new TuiMockController()
  
  val tui = new Tui(controller)

  def captureOutput(block: => Unit): String = {
    val stream = new ByteArrayOutputStream()
    Console.withOut(stream) { block }
    stream.toString
  }

  "A Tui" should {

    "render Title screen correctly" in {
      controller.setViewState(ViewState.VTitle)
      val out = captureOutput { tui.update() }
      out should include ("POKEMON SCALA EDITION")
      out should include ("n. Neues Spiel")
    }

    "render Menu screen correctly" in {
      controller.setViewState(ViewState.VMenu)
      val out = captureOutput { tui.render() }
      out should include ("HAUPTMENUE")
      out should include ("s. Wilden Kampf starten")
    }

    "render NameInput screen correctly" in {
      controller.setViewState(ViewState.VNameInput)
      val out = captureOutput { tui.render() }
      out should include ("Wie heisst du, Trainer?")
    }

    "render SelectProfile screen correctly (empty)" in {
      controller.setViewState(ViewState.VSelectProfile)
      // Das hier funktioniert jetzt, weil controller vom Typ TuiMockController ist
      controller.testSaves = List.empty
      val out = captureOutput { tui.render() }
      out should include ("Keine Spielstaende gefunden")
    }

    "render SelectProfile screen correctly (with saves)" in {
      controller.setViewState(ViewState.VSelectProfile)
      controller.testSaves = List("Save1", "Save2")
      val out = captureOutput { tui.render() }
      out should include ("Verfuegbare Profile")
      out should include ("Save1")
      out should include ("Save2")
    }

    "render PlayerAtk screen correctly" in {
      controller.setViewState(ViewState.VPlayerAtk)
      val out = captureOutput { tui.render() }
      out should include ("Kampf-Aktion waehlen")
      out should include ("TestMon") 
    }

    "render EnemyAtk screen correctly" in {
      controller.setViewState(ViewState.VEnemyAtk)
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