package de.htwg.se.controller.controllerImpl.state

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.model.GameStateComponent.GameState
import de.htwg.se.model.BattleLogicComponent.IBattleLogic
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PokemonComponent.{IPokemon, Attack, PokemonType}

class EnemyAttackStateSpec extends AnyWordSpec with Matchers {

  // --- MOCK IMPLEMENTIERUNGEN ---

  class BattleLogicMock extends IBattleLogic {
    override def getLossMessage(name: String): String = s"$name hat verloren"
    override def getWinMessage(name: String): String = s"$name hat gewonnen"
    override def isFleeingAllowed: Boolean = true
  }

  case class MockPokemon(
      name: String,
      currentHp: Int,
      pType: PokemonType,
      secondaryType: Option[PokemonType] = None, // NEU: Muss rein
      maxHp: Int = 100,
      id: Int = 0,                               // NEU: Muss rein
      attacks: Vector[Attack] = Vector.empty,
      spriteUrl: String = ""                     // NEU: Muss rein
  ) extends IPokemon {
    override def withHp(newHp: Int): IPokemon = copy(currentHp = newHp.max(0).min(maxHp))
    override def isFainted: Boolean = currentHp <= 0
    override def toString: String = s"$name (HP: $currentHp/$maxHp)"
  }

  case class MockPlayer(
      activePokemon: IPokemon,
      name: String = "TestPlayer",
      nextIndex: Option[Int] = None
  ) extends IPlayer {
    override def updatePokemon(p: IPokemon): IPlayer = copy(activePokemon = p)
    override def isActiveFainted: Boolean = activePokemon.isFainted
    
    override def team: Vector[IPokemon] = Vector(activePokemon)
    override def withTeam(newTeam: Vector[IPokemon]): IPlayer = this 
    override def currentPokemonIndex: Int = 0
    override def items: Vector[String] = Vector.empty
    override def isDefeated: Boolean = nextIndex.isEmpty && isActiveFainted
    override def nextAlivePokemonIndex: Option[Int] = nextIndex
    override def switchActivePokemon(index: Int): IPlayer = this
    override def addPokemon(p: IPokemon): IPlayer = this
  }

  "EnemyAttackState" should {
    val logic = new BattleLogicMock()

    "transition to PlayerAttackState and deal damage" in {
      val attack = Attack("Punch", 10, PokemonType.Normal)
      val playerPoke = MockPokemon("PlayerMon", 100, PokemonType.Normal)
      val enemyPoke = MockPokemon("EnemyMon", 100, PokemonType.Normal, attacks = Vector(attack))
      
      val gs = GameState(MockPlayer(playerPoke), MockPlayer(enemyPoke), false, "", "")
      val state = EnemyAttackState(gs, logic)

      val result = state.handle("").asInstanceOf[PlayerAttackState]
      result.gameState.player.activePokemon.currentHp should be (90)
    }

    "handle effectiveness correctly (Double Damage)" in {
      val attack = Attack("Burn", 10, PokemonType.Fire)
      val playerPoke = MockPokemon("GrassMon", 100, PokemonType.Grass)
      val enemyPoke = MockPokemon("FireMon", 100, PokemonType.Fire, attacks = Vector(attack))
      
      val gs = GameState(MockPlayer(playerPoke), MockPlayer(enemyPoke), false, "", "")
      val result = EnemyAttackState(gs, logic).handle("").asInstanceOf[PlayerAttackState]

      result.gameState.player.activePokemon.currentHp should be (80)
      result.gameState.msg2 should include ("Sehr effektiv!")
    }

    "transition to MenuState when ALL pokemon are fainted" in {
      val attack = Attack("Kill", 100, PokemonType.Normal)
      val playerPoke = MockPokemon("WeakMon", 10, PokemonType.Normal)
      val enemyPoke = MockPokemon("StrongMon", 100, PokemonType.Normal, attacks = Vector(attack))
      
      // nextIndex = None simuliert: Kein Pokémon mehr übrig
      val gs = GameState(MockPlayer(playerPoke, "Ash", None), MockPlayer(enemyPoke), false, "", "")
      val result = EnemyAttackState(gs, logic).handle("")

      result shouldBe a [MenuState]
      result.asInstanceOf[MenuState].gameState.msg1 should be ("VERLOREN!")
    }

    "auto-switch pokemon if active faints but others are alive" in {
      val attack = Attack("Kill", 100, PokemonType.Normal)
      val playerPoke = MockPokemon("FaintingMon", 10, PokemonType.Normal)
      val enemyPoke = MockPokemon("StrongMon", 100, PokemonType.Normal, attacks = Vector(attack))
      
      // nextIndex = Some(1) simuliert: Es gibt noch ein Pokémon an Index 1
      val gs = GameState(MockPlayer(playerPoke, "Ash", Some(1)), MockPlayer(enemyPoke), false, "", "")
      val result = EnemyAttackState(gs, logic).handle("")

      // Es sollte NICHT MenuState sein, sondern PlayerAttackState (weil gewechselt wurde)
      result shouldBe a [PlayerAttackState]
      result.asInstanceOf[PlayerAttackState].gameState.msg1 should include ("wurde besiegt!")
    }
  }
}