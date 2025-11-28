package de.htwg.se.controller.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model._
import de.htwg.se.model.PokemonType._

class PlayerAttackStateSpec extends AnyWordSpec with Matchers {

  def createScenario(playerType: PokemonType, enemyType: PokemonType, attackType: PokemonType): GameState = {
    val attack = Attack("TestHieb", 10, attackType)
    val playerPoke = Pokemon("Hero", playerType, 100, 100, Vector(attack))
    val enemyPoke = Pokemon("Villain", enemyType, 100, 100, Vector(attack))
    
    val player = Player("Ash", Vector(playerPoke))
    val enemy = Player("Gary", Vector(enemyPoke))
    
    GameState(player, enemy)
  }

  "The PlayerAttackState" should {

    "handle invalid attack index (too high/low)" in {
      val gs = createScenario(Normal, Normal, Normal)
      val state = PlayerAttackState(gs, WildBattleLogic)
      
      val nextState = state.handle("99")
      
      nextState shouldBe a [PlayerAttackState]
      nextState.gameState.msg2 should be("Ungueltiger Angriff Index!")
    }

    "handle garbage input (not a number, not 'f')" in {
      val gs = createScenario(Normal, Normal, Normal)
      val state = PlayerAttackState(gs, WildBattleLogic)
      
      val nextState = state.handle("blabla")
      
      nextState shouldBe a [PlayerAttackState]
      nextState.gameState.msg2 should startWith("Wähle Attacke")
    }

    "handle 'Super Effective' attack message (> 1.0)" in {
      val gs = createScenario(Water, Fire, Water)
      val state = PlayerAttackState(gs, WildBattleLogic)
      
      val nextState = state.handle("1")
      
      nextState shouldBe a [EnemyAttackState] 
      nextState.gameState.msg2 should include("(Sehr effektiv!)")
    }

    "handle 'No Effect' attack message (0.0)" in {
      val gs = createScenario(Electric, Ground, Electric)
      val state = PlayerAttackState(gs, WildBattleLogic)
      
      val nextState = state.handle("1")
      
      nextState shouldBe a [EnemyAttackState]
      nextState.gameState.msg2 should include("(Keine Wirkung!)")
    }

    "handle 'Not Very Effective' attack message (< 1.0)" in {
      val gs = createScenario(Fire, Water, Fire) 
      val state = PlayerAttackState(gs, WildBattleLogic)
      val nextState = state.handle("1")
      
      nextState shouldBe a [EnemyAttackState]
      
      nextState.gameState.msg2 should include("(Nicht sehr effektiv...)")
    }
    
    "prevent fleeing in Trainer battles" in {
       val gs = createScenario(Normal, Normal, Normal)
       val state = PlayerAttackState(gs, TrainerBattleLogic)
       
       val nextState = state.handle("f")
       
       nextState shouldBe a [PlayerAttackState] 
       nextState.gameState.msg2 should include("Flucht unmöglich")
    }
  }
}