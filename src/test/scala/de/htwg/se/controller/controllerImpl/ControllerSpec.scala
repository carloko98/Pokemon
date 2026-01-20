package de.htwg.se.controller.controllerImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.controller.ViewState._
import de.htwg.se.model.FileIOComponent.MockFileIOImpl.MockFileIO
import de.htwg.se.model.PokemonComponent.PokemonService
import de.htwg.se.util.Observer

class ControllerSpec extends AnyWordSpec with Matchers {

  "A Controller" when {
    val mockFileIO = new MockFileIO()
    val player = PokemonService.createPlayer("Ash", Vector("Charizard"))
    val enemy = PokemonService.createRandomEnemy()
    
    // Erstellt für jeden Test eine frische Instanz
    def createController = new Controller(player, enemy, mockFileIO)

    "handling basic state transitions" should {
      "navigate from Title to NameInput and then to Menu" in {
        val controller = createController
        controller.viewState should be(VTitle)
        controller.handleInput("n")
        controller.viewState should be(VNameInput)
        controller.handleInput("Ash")
        controller.viewState should be(VMenu)
      }
    }

    "managing game persistence" should {
      "navigate to SelectProfileState and handle the back button correctly" in {
        val controller = createController
        controller.handleInput("l") 
        controller.viewState should be(VSelectProfile)
        controller.handleInput("b") 
        controller.viewState should be(VTitle)
      }

      "successfully simulate loading a game" in {
        val controller = createController
        controller.handleInput("l")
        controller.handleInput("Ash") 
        controller.viewState should be(VMenu)
      }
    }

    "conducting a battle" should {
      "support Undo and Redo operations during a fight" in {
        val controller = createController
        
        // Schrittweise Navigation zum Kampf
        controller.handleInput("n")   // Zu NameInput
        controller.handleInput("Ash") // Zu Menu
        controller.handleInput("w")   // Zu PlayerAtk
        
        controller.viewState should be(VPlayerAtk)
        
        val initialEnemyHp = controller.getEnemyPokemon.currentHp
        
        // Angriff ausführen -> Wechselt zu EnemyAtk
        controller.handleInput("1")
        controller.viewState should be(VEnemyAtk)
        
        // Undo -> Zurück zu PlayerAtk
        controller.handleInput("z")
        controller.viewState should be(VPlayerAtk)
        controller.getEnemyPokemon.currentHp should be(initialEnemyHp)
        
        // Redo -> Wieder zu EnemyAtk
        controller.handleInput("y")
        controller.viewState should be(VEnemyAtk)
      }

      "handle Pokemon switching within the battle" in {
        val controller = createController
        
        // Navigation zum Kampf
        controller.handleInput("n")
        controller.handleInput("Ash")
        controller.handleInput("w")
        
        // Im Kampf 'w' drücken öffnet SwitchPokemonState
        controller.handleInput("w") 
        controller.viewState should be(VSwitchPokemon)
        
        // 'z' im Switch-State geht zurück zum Kampf
        controller.handleInput("z")
        controller.viewState should be(VPlayerAtk)
      }
    }

    "visiting the PokéCenter" should {
      "navigate to the PokéCenter and back" in {
        val controller = createController
        controller.handleInput("n")
        controller.handleInput("Ash")
        
        controller.handleInput("c") // PokéCenter betreten
        controller.viewState should be(VPokeCenter)
        
        controller.handleInput("back") // Zurück zum Menu
        controller.viewState should be(VMenu)
      }
    }

    "providing UI information" should {
      "return correct values" in {
        val controller = createController
        controller.handleInput("n")
        controller.handleInput("Ash")
        
        val (m1, m2) = controller.getMessage
        m1 shouldBe a [String]
        controller.getPlayerPokemon should not be null
        controller.isBattleOver should be(false)
        controller.getAvailableSaves shouldBe a [List[_]]
      }
    }

    "using the Observer pattern" should {
      "notify observers" in {
        val controller = createController
        var notified = false
        controller.add(new Observer {
          override def update(): Unit = notified = true
        })
        controller.handleInput("n")
        notified should be(true)
      }
    }
  }
}