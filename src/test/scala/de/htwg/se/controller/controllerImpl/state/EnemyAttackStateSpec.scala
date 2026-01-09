package de.htwg.se.controller.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model._
import de.htwg.se.model.PokemonType._
import de.htwg.se.controller.controllerImpl.state.{EnemyAttackState, MenuState, PlayerAttackState}

class EnemyAttackStateSpec extends AnyWordSpec with Matchers {

  "The EnemyAttackState" should {
    
    def createScenario(playerType: PokemonType, enemyType: PokemonType, enemyAttackType: PokemonType, playerHp: Int = 100): GameState = {
      val attack = Attack("TestAttack", 10, enemyAttackType)
      val playerPoke = Pokemon("PlayerPoke", playerType, 100, playerHp, Vector(attack))
      val enemyPoke = Pokemon("EnemyPoke", enemyType, 100, 100, Vector(attack))
      
      val player = Player("Ash", Vector(playerPoke))
      val enemy = Player("Gary", Vector(enemyPoke))
      
      GameState(player, enemy)
    }

    "handle a 'Super Effective' attack (> 1.0)" in {
      val gs = createScenario(Fire, Water, Water)
      val state = EnemyAttackState(gs, WildBattleLogic)
      
      val nextState = state.handle("")
      
      nextState shouldBe a [PlayerAttackState]
      nextState.gameState.msg2 should include ("(Sehr effektiv!)")
    }

    "handle a 'Not Very Effective' attack (< 1.0)" in {
      val gs = createScenario(Water, Fire, Fire)
      val state = EnemyAttackState(gs, WildBattleLogic)
      
      val nextState = state.handle("")
      
      nextState shouldBe a [PlayerAttackState]
      nextState.gameState.msg2 should include ("(Nicht sehr effektiv...)")
    }

    "handle a 'No Effect' attack (0.0)" in {
      val gs = createScenario(Ground, Electric, Electric)
      val state = EnemyAttackState(gs, WildBattleLogic)
      
      val nextState = state.handle("")
      
      nextState shouldBe a [PlayerAttackState]
      nextState.gameState.msg2 should include ("(Keine Wirkung!)")
    }

    "switch to MenuState if the player is defeated" in {
      val gs = createScenario(Normal, Normal, Normal, playerHp = 1)
      val state = EnemyAttackState(gs, WildBattleLogic)
      
      val nextState = state.handle("")
      
      nextState shouldBe a [MenuState]
      nextState.gameState.battleOver should be(true)
      nextState.gameState.msg1 should be("VERLOREN!")
    }
  }
}