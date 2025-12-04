package de.htwg.se.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model._
import de.htwg.se.controller.state._
import de.htwg.se.util.Observer
import java.io.File

class ControllerSpec extends AnyWordSpec with Matchers {

 
  def setupTest() = {
    val pokemon = PokemonFactory.getPokemon("Glurak")
    val p1 = Player("Ash", Vector(pokemon))
    val p2 = Player("Gary", Vector(pokemon))
    val controller = new Controller(p1, p2)
    val observer = new Observer { override def update(): Unit = {} }
    controller.add(observer)
    (controller, pokemon, p2)
  }

  "A Controller" should {

    "cover explicit Save via handleInput (Line 59)" in {
       val (controller, _, _) = setupTest()
       controller.setState(MenuState(controller.state.gameState))
       controller.handleInput("save")
       
       val f = new File("save_Ash.xml")
       f.exists() should be(true)
       f.delete()
    }


    "cover SelectProfileState inputs (Lines 55-56)" in {
      val (controller, _, _) = setupTest()
      controller.setState(SelectProfileState(controller.state.gameState))


      controller.handleInput("b")
      controller.state shouldBe a [TitleState]


      controller.setState(SelectProfileState(controller.state.gameState))
      controller.handleInput("GibtEsNicht") 
      controller.getMessage._2 should include ("nicht laden")
    }

    "cover default handleInput else branch (Line 60)" in {
      val (controller, _, _) = setupTest()
      controller.setState(MenuState(controller.state.gameState))

      controller.handleInput("s")
      controller.state shouldBe a [PlayerAttackState]
    }

    "cover AttackCommand internal Undo/Redo" in {
      val (controller, _, _) = setupTest()
      controller.setState(PlayerAttackState(controller.state.gameState, de.htwg.se.model.WildBattleLogic))
      controller.handleInput("1") 
      controller.handleInput("undo")
      controller.handleInput("redo")
    }

    "cover Battle-Over Auto-Transition and Save" in {
      val (controller, pokemon, p2) = setupTest()
      val deadEnemy = p2.copy(team = Vector(pokemon.withHp(0)))
      val endGameState = GameState(controller.getPlayer, deadEnemy, battleOver = true)
      
      controller.setState(PlayerAttackState(endGameState, de.htwg.se.model.WildBattleLogic))
      controller.handleInput("99") 
      
      controller.state shouldBe a [MenuState]
      new File("save_Ash.xml").delete()
    }

    "cover Save Failure" in {
      val (controller, pokemon, p2) = setupTest()
      val badPlayer = Player("In/va/lid", Vector(pokemon))
      val badController = new Controller(badPlayer, p2)
      badController.saveGame() 
    }

    "cover Load Game Success" in {
      val (controller, pokemon, p2) = setupTest()
      val filename = "save_LoadSuccessUser.xml"
      val savePlayer = Player("LoadSuccessUser", Vector(pokemon))
      val tempController = new Controller(savePlayer, p2)
      tempController.saveGame()

      controller.loadGame("LoadSuccessUser")

      controller.state shouldBe a [MenuState]
      controller.getPlayer.name should be ("LoadSuccessUser")
      new File(filename).delete()
    }
    
    "cover all Getters" in {
       val (controller, _, _) = setupTest()
       controller.getAvailableSaves
       controller.isBattleOver
       controller.getPlayerPokemon
       controller.getEnemyPokemon
       controller.getEnemy
       controller.getPlayer
    }
  }
}