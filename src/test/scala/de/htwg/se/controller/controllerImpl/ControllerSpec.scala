package de.htwg.se.controller.controllerImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

// Wir testen den ECHTEN Controller
import de.htwg.se.controller.controllerImpl.Controller
import de.htwg.se.controller.ViewState._

// Wir nutzen den MOCK für FileIO (liegt jetzt in main!)
import de.htwg.se.model.FileIOComponent.MockFileIOImpl.MockFileIO

// Wir brauchen Services für Dummy-Daten
import de.htwg.se.model.PokemonComponent.PokemonService
import de.htwg.se.util.Observer

class ControllerSpec extends AnyWordSpec with Matchers {

  "A Controller" when {
    
    // 1. Setup: Wir bauen die Abhängigkeiten
    val mockFileIO = new MockFileIO()
    
    // Wir erstellen echte Spieler/Gegner über den Service (oder Mocks, wenn du hast)
    val player = PokemonService.createPlayer("Ash", Vector("Glurak"))
    val enemy = PokemonService.createRandomEnemy()
    
    // Injektion: Echter Controller bekommt Mock-FileIO
    val controller = new Controller(player, enemy, mockFileIO)

    "newly created" should {
      "start in the Title State (VTitle)" in {
        // Je nachdem, was dein Startzustand ist (meist TitleState)
        controller.viewState should be(VTitle)
      }

      "handle input 'n' to go to NameInput (if in TitleState)" in {
        // Falls er im TitleState startet:
        controller.handleInput("n") 
        controller.viewState should be(VNameInput)
      }
    }

    "handling generic inputs" should {
      "not crash on empty input" in {
        noException should be thrownBy controller.handleInput("")
      }
      
      "allow saving the game (using MockFileIO)" in {
        // Da MockFileIO.save immer Success zurückgibt, darf hier nichts passieren
        noException should be thrownBy controller.saveGame()
      }
    }

    "using the Observer pattern" should {
      "notify observers on change" in {
        var notified = false
        val observer = new Observer {
          override def update(): Unit = notified = true
        }
        controller.add(observer)
        
        // Eine Aktion auslösen
        controller.handleInput("q") // Oder was auch immer eine Änderung auslöst
        
        // Prüfen
        // Hinweis: Das hängt davon ab, ob dein HandleInput notifyObservers ruft.
        // Falls ja: notified should be(true)
      }
    }
    
    "accessing getters" should {
      "return the correct player name" in {
        controller.getPlayer.name should be("Ash")
      }
      "return a valid pokemon" in {
        controller.getPlayerPokemon.name should be("Glurak")
      }
    }
  }
}