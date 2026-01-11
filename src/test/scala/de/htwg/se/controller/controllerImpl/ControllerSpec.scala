package de.htwg.se.controller.controllerImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.controller.controllerImpl.Controller
import de.htwg.se.controller.ViewState._
import de.htwg.se.model.FileIOComponent.MockFileIOImpl.MockFileIO
import de.htwg.se.model.PokemonComponent.PokemonService
import de.htwg.se.util.Observer

class ControllerSpec extends AnyWordSpec with Matchers {

  "A Controller" when {
    
    // 1. Setup
    val mockFileIO = new MockFileIO()
    val player = PokemonService.createPlayer("Ash", Vector("Glurak"))
    val enemy = PokemonService.createRandomEnemy()
    val controller = new Controller(player, enemy, mockFileIO)

    "newly created" should {
      "start in the Title State (VTitle)" in {
        controller.viewState should be(VTitle)
      }

      "handle input 'n' to go to NameInput (if in TitleState)" in {
        controller.handleInput("n") 
        controller.viewState should be(VNameInput)
      }
    }

    "handling generic inputs" should {
      "not crash on empty input" in {
        noException should be thrownBy controller.handleInput("")
      }
      
      "allow saving the game (using MockFileIO)" in {
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
        
        // Da wir im NameInputState sind, geben wir "Ash" ein.
        // Das löst einen State-Wechsel zu MenuState aus -> notifyObservers() wird gerufen.
        // Und es setzt den Spielernamen wieder auf "Ash" (wichtig für den nächsten Test!)
        controller.handleInput("Ash") 
        
        notified should be(true)
        controller.viewState should be(VMenu)
      }
    }
    
    "accessing getters" should {
      "return the correct player name" in {
        controller.getPlayer.name should be("Ash")
      }
      "return a valid pokemon" in {
        // Da ein neuer Player erstellt wurde, prüfen wir, ob er ein Pokemon hat
        controller.getPlayerPokemon.name should not be empty
      }
    }
  }
}