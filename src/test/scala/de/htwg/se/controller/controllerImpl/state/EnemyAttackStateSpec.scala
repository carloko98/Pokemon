package de.htwg.se.controller.controllerImpl.state

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.model.GameStateComponent.GameState
import de.htwg.se.model.BattleLogicComponent.IBattleLogic
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PokemonComponent.{IPokemon, Attack, PokemonType}

class EnemyAttackStateSpec extends AnyWordSpec with Matchers {

  // --- MOCK IMPLEMENTIERUNGEN ---

  // 1. Mock für BattleLogic
  class BattleLogicMock extends IBattleLogic {
    override def getLossMessage(name: String): String = s"$name hat verloren"
    override def getWinMessage(name: String): String = s"$name hat gewonnen"
    override def isFleeingAllowed: Boolean = true
  }

  // 2. Mock für Pokemon (KORRIGIERT: Keine nicht-existenten Overrides)
  case class MockPokemon(
      currentHp: Int,
      maxHp: Int = 100,
      attacks: Vector[Attack],
      pType: PokemonType 
  ) extends IPokemon {
    override def name: String = "TestMon"
    override def withHp(newHp: Int): IPokemon = copy(currentHp = newHp)
    override def isFainted: Boolean = currentHp <= 0
    override def toString: String = name
  }

  // 3. Mock für Player
  case class MockPlayer(
      activePokemon: IPokemon,
      name: String = "TestPlayer"
  ) extends IPlayer {
    override def updatePokemon(p: IPokemon): IPlayer = copy(activePokemon = p)
    override def isActiveFainted: Boolean = activePokemon.isFainted
    
    override def team: Vector[IPokemon] = Vector(activePokemon)
    override def currentPokemonIndex: Int = 0
    override def items: Vector[String] = Vector.empty
    override def isDefeated: Boolean = false
    override def nextAlivePokemonIndex: Option[Int] = None
    override def switchActivePokemon(index: Int): IPlayer = this
    override def addPokemon(p: IPokemon): IPlayer = this
  }

  "EnemyAttackState" should {

    val logic = new BattleLogicMock()

    "transition to PlayerAttackState and deal normal damage (Effectiveness 1.0)" in {
      // Normal vs Normal = 1.0
      val atkType = PokemonType.Normal
      val defType = PokemonType.Normal
      
      val attack = Attack("Punch", 10, atkType)
      
      val playerPoke = MockPokemon(100, 100, Vector.empty, defType)
      val enemyPoke = MockPokemon(100, 100, Vector(attack), atkType)
      
      val gs = GameState(MockPlayer(playerPoke), MockPlayer(enemyPoke), battleOver = false, "", "")
      val state = EnemyAttackState(gs, logic)

      val result = state.handle("")

      result shouldBe a [PlayerAttackState]
      val nextState = result.asInstanceOf[PlayerAttackState]
      
      // 10 Schaden * 1.0 = 10 -> 90 HP übrig
      nextState.gameState.player.activePokemon.currentHp should be (90)
      nextState.gameState.msg2 should include ("10 Schaden!")
      nextState.gameState.msg2 should not include "(" 
    }

    "show 'Sehr effektiv!' message when effectiveness > 1.0" in {
      // Fire vs Grass = 2.0
      val atkType = PokemonType.Fire
      val defType = PokemonType.Grass
      
      val attack = Attack("FireHit", 10, atkType)
      
      val playerPoke = MockPokemon(100, 100, Vector.empty, defType)
      val enemyPoke = MockPokemon(100, 100, Vector(attack), atkType)
      
      val gs = GameState(MockPlayer(playerPoke), MockPlayer(enemyPoke), battleOver = false, "", "")
      val state = EnemyAttackState(gs, logic)

      val result = state.handle("").asInstanceOf[PlayerAttackState]
      
      // 10 Schaden * 2.0 = 20 -> 80 HP übrig
      result.gameState.player.activePokemon.currentHp should be (80)
      result.gameState.msg2 should include ("(Sehr effektiv!)")
    }

    "show 'Nicht sehr effektiv...' message when effectiveness is between 0 and 1" in {
      // Fire vs Water = 0.5
      val atkType = PokemonType.Fire
      val defType = PokemonType.Water
      
      val attack = Attack("WeakHit", 10, atkType)
      
      val playerPoke = MockPokemon(100, 100, Vector.empty, defType)
      val enemyPoke = MockPokemon(100, 100, Vector(attack), atkType)
      
      val gs = GameState(MockPlayer(playerPoke), MockPlayer(enemyPoke), battleOver = false, "", "")
      val state = EnemyAttackState(gs, logic)

      val result = state.handle("").asInstanceOf[PlayerAttackState]
      
      // 10 Schaden * 0.5 = 5 -> 95 HP übrig
      result.gameState.player.activePokemon.currentHp should be (95)
      result.gameState.msg2 should include ("(Nicht sehr effektiv...)")
    }

    "show 'Keine Wirkung!' message when effectiveness is 0.0" in {
      // Normal vs Ghost = 0.0
      val atkType = PokemonType.Normal
      val defType = PokemonType.Ghost
      
      val attack = Attack("NoHit", 10, atkType)
      
      val playerPoke = MockPokemon(100, 100, Vector.empty, defType)
      val enemyPoke = MockPokemon(100, 100, Vector(attack), atkType)
      
      val gs = GameState(MockPlayer(playerPoke), MockPlayer(enemyPoke), battleOver = false, "", "")
      val state = EnemyAttackState(gs, logic)

      val result = state.handle("").asInstanceOf[PlayerAttackState]
      
      // 10 Schaden * 0.0 = 0 -> 100 HP übrig
      result.gameState.player.activePokemon.currentHp should be (100)
      result.gameState.msg2 should include ("(Keine Wirkung!)")
    }

    "transition to MenuState (Game Over) when player faints" in {
      // Fire vs Grass (2.0)
      val atkType = PokemonType.Fire
      val defType = PokemonType.Grass
      
      val attack = Attack("KillHit", 100, atkType) // Basis 100 * 2.0 = 200 Schaden
      
      val playerPoke = MockPokemon(10, 100, Vector.empty, defType) // Spieler hat nur 10 HP
      val enemyPoke = MockPokemon(100, 100, Vector(attack), atkType)
      
      val gs = GameState(MockPlayer(playerPoke), MockPlayer(enemyPoke), battleOver = false, "", "")
      val state = EnemyAttackState(gs, logic)

      val result = state.handle("")

      result shouldBe a [MenuState]
      val looseState = result.asInstanceOf[MenuState]
      
      looseState.gameState.battleOver should be (true)
      looseState.gameState.msg1 should be ("VERLOREN!")
    }
  }
}