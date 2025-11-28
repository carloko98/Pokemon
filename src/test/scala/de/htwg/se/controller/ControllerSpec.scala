package de.htwg.se.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model._
import de.htwg.se.controller.state._
import java.io.File

class ControllerSpec extends AnyWordSpec with Matchers {

  "A Controller" should {
    val pokemon = Pokemon("TestMon", PokemonType.Fire, 100, 100, Vector(Attack("Hit", 10, PokemonType.Normal)))
    val player = Player("TestControllerUser", Vector(pokemon))
    val enemy = Player("Enemy", Vector(pokemon))
    
    val controller = new Controller(player, enemy)

    "start in TitleState" in {
      controller.state shouldBe a [TitleState]
    }

    "provide correct getters delegating to the current state" in {
      controller.getPlayer shouldBe player
      controller.getEnemy shouldBe enemy
      
      controller.getPlayerPokemon shouldBe pokemon
      controller.getEnemyPokemon shouldBe pokemon
      
      controller.isBattleOver shouldBe false
    }

    "handle 'save' input globally" in {
      controller.handleInput("save")
      
      val file = new File("save_TestControllerUser.xml")
      file.exists() should be(true)
      
      file.delete()
    }

    "handle navigation 'b' (Back) in SelectProfileState" in {
      val currentGameState = controller.state.gameState
      controller.state = SelectProfileState(currentGameState)
      
      controller.handleInput("b")
      
      controller.state shouldBe a [TitleState]
    }

    "handle loading a non-existent profile (Error Case)" in {
      val currentGameState = controller.state.gameState
      controller.state = SelectProfileState(currentGameState)
      
      controller.handleInput("GibtsNicht")
      
      controller.state shouldBe a [SelectProfileState]
      controller.state.gameState.msg2 should include ("nicht gefunden")
    }

    "handle loading an existing profile (Success Case)" in {
      controller.saveGame()
      
      controller.state = SelectProfileState(controller.state.gameState)
      
      controller.handleInput("TestControllerUser")
      
      controller.state shouldBe a [MenuState]
      controller.state.gameState.msg1 should include ("geladen")
      
      new File("save_TestControllerUser.xml").delete()
    }

    "trigger Auto-Save when transition from Battle to Menu happens" in {
      val battleState = PlayerAttackState(controller.state.gameState, WildBattleLogic)
      controller.state = battleState
      
      controller.handleInput("f")
      controller.state shouldBe a [MenuState]
      
      val file = new File("save_TestControllerUser.xml")
      file.exists() should be(true)
      
      file.delete()
    }
    
    "delegate getAvailableSaves to FileIO" in {
      controller.saveGame()
      
      val saves = controller.getAvailableSaves
      saves should contain ("TestControllerUser")
      
      new File("save_TestControllerUser.xml").delete()
    }
    "return the correct message tuple" in {
      val testGameState = controller.state.gameState.copy(msg1 = "Hallo", msg2 = "Welt")
      controller.state = MenuState(testGameState)
      controller.getMessage should be(("Hallo", "Welt"))
    }
  }
}