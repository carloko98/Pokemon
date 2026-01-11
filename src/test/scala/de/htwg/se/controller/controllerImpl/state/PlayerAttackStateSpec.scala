package de.htwg.se.controller.controllerImpl.state

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.model.GameStateComponent.GameState
import de.htwg.se.model.BattleLogicComponent.IBattleLogic
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PokemonComponent.{IPokemon, Attack, PokemonType}

class PlayerAttackStateSpec extends AnyWordSpec with Matchers {

  // Mock für die BattleLogic
  class BattleLogicMock(allowFlee: Boolean = true) extends IBattleLogic {
    override def getLossMessage(name: String): String = s"$name hat verloren"
    override def getWinMessage(name: String): String = s"$name hat gewonnen"
    override def isFleeingAllowed: Boolean = allowFlee
  }

  // KORRIGIERT: MockPokemon ohne die nicht existierenden Methoden (attack, defense etc.)
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

  // MockPlayer (implementiert alle Methoden, die IPlayer verlangt)
  case class MockPlayer(
      activePokemon: IPokemon,
      name: String = "TestPlayer"
  ) extends IPlayer {
    override def updatePokemon(p: IPokemon): IPlayer = copy(activePokemon = p)
    override def isActiveFainted: Boolean = activePokemon.isFainted
    
    // Weitere Methoden von IPlayer
    override def team: Vector[IPokemon] = Vector(activePokemon)
    override def currentPokemonIndex: Int = 0
    override def items: Vector[String] = Vector.empty
    override def isDefeated: Boolean = false
    override def nextAlivePokemonIndex: Option[Int] = None
    override def switchActivePokemon(index: Int): IPlayer = this
    override def addPokemon(p: IPokemon): IPlayer = this
  }

  "PlayerAttackState" should {
    
    "transition to MenuState when fleeing is allowed" in {
       val logic = new BattleLogicMock(true)
       val pPoke = MockPokemon(100, 100, Vector.empty, PokemonType.Normal)
       val ePoke = MockPokemon(100, 100, Vector.empty, PokemonType.Normal)
       val gs = GameState(MockPlayer(pPoke), MockPlayer(ePoke), false, "", "")
       val state = PlayerAttackState(gs, logic)

       val result = state.handle("f")
       result shouldBe a [MenuState]
       result.asInstanceOf[MenuState].gameState.battleOver should be (true)
       result.asInstanceOf[MenuState].gameState.msg1 should be ("Du bist geflohen!")
    }

    "remain in PlayerAttackState when fleeing is not allowed" in {
       val logic = new BattleLogicMock(false)
       val pPoke = MockPokemon(100, 100, Vector.empty, PokemonType.Normal)
       val ePoke = MockPokemon(100, 100, Vector.empty, PokemonType.Normal)
       val gs = GameState(MockPlayer(pPoke), MockPlayer(ePoke), false, "", "")
       val state = PlayerAttackState(gs, logic)

       val result = state.handle("fliehen")
       result shouldBe a [PlayerAttackState]
       result.asInstanceOf[PlayerAttackState].gameState.msg2 should include ("Flucht unmöglich")
    }

    "handle invalid input (non-integer)" in {
       val logic = new BattleLogicMock()
       val pPoke = MockPokemon(100, 100, Vector.empty, PokemonType.Normal)
       val ePoke = MockPokemon(100, 100, Vector.empty, PokemonType.Normal)
       val gs = GameState(MockPlayer(pPoke), MockPlayer(ePoke), false, "", "")
       val state = PlayerAttackState(gs, logic)

       val result = state.handle("abc")
       result shouldBe a [PlayerAttackState]
       result.asInstanceOf[PlayerAttackState].gameState.msg2 should include ("Ungültige Eingabe")
    }

    "handle invalid input (index out of bounds)" in {
       val logic = new BattleLogicMock()
       val attack = Attack("Hit", 10, PokemonType.Normal)
       val pPoke = MockPokemon(100, 100, Vector(attack), PokemonType.Normal)
       val ePoke = MockPokemon(100, 100, Vector.empty, PokemonType.Normal)
       val gs = GameState(MockPlayer(pPoke), MockPlayer(ePoke), false, "", "")
       val state = PlayerAttackState(gs, logic)

       val result = state.handle("2") 
       result shouldBe a [PlayerAttackState]
       result.asInstanceOf[PlayerAttackState].gameState.msg2 should include ("Ungültige Eingabe")
    }

    "execute attack and transition to EnemyAttackState if enemy survives" in {
       val logic = new BattleLogicMock()
       val attack = Attack("Hit", 10, PokemonType.Normal)
       val pPoke = MockPokemon(100, 100, Vector(attack), PokemonType.Normal)
       val ePoke = MockPokemon(100, 100, Vector.empty, PokemonType.Normal)
       val gs = GameState(MockPlayer(pPoke), MockPlayer(ePoke), false, "", "")
       val state = PlayerAttackState(gs, logic)

       val result = state.handle("1")
       result shouldBe a [EnemyAttackState]
       val nextState = result.asInstanceOf[EnemyAttackState]
       nextState.gameState.enemy.activePokemon.currentHp should be (90)
       nextState.gameState.msg2 should include ("10 Schaden!")
    }

    "show 'Sehr effektiv' message" in {
       val logic = new BattleLogicMock()
       val attack = Attack("Fire", 10, PokemonType.Fire)
       val pPoke = MockPokemon(100, 100, Vector(attack), PokemonType.Normal)
       val ePoke = MockPokemon(100, 100, Vector.empty, PokemonType.Grass)
       val gs = GameState(MockPlayer(pPoke), MockPlayer(ePoke), false, "", "")
       val state = PlayerAttackState(gs, logic)

       val result = state.handle("1")
       result shouldBe a [EnemyAttackState]
       result.asInstanceOf[EnemyAttackState].gameState.msg2 should include ("(Sehr effektiv!)")
    }

    "show 'Nicht sehr effektiv' message" in {
       val logic = new BattleLogicMock()
       val attack = Attack("Fire", 10, PokemonType.Fire)
       val pPoke = MockPokemon(100, 100, Vector(attack), PokemonType.Normal)
       val ePoke = MockPokemon(100, 100, Vector.empty, PokemonType.Water)
       val gs = GameState(MockPlayer(pPoke), MockPlayer(ePoke), false, "", "")
       val state = PlayerAttackState(gs, logic)

       val result = state.handle("1")
       result shouldBe a [EnemyAttackState]
       result.asInstanceOf[EnemyAttackState].gameState.msg2 should include ("(Nicht sehr effektiv...)")
    }

    "show 'Keine Wirkung' message" in {
       val logic = new BattleLogicMock()
       val attack = Attack("Normal", 10, PokemonType.Normal)
       val pPoke = MockPokemon(100, 100, Vector(attack), PokemonType.Normal)
       val ePoke = MockPokemon(100, 100, Vector.empty, PokemonType.Ghost)
       val gs = GameState(MockPlayer(pPoke), MockPlayer(ePoke), false, "", "")
       val state = PlayerAttackState(gs, logic)

       val result = state.handle("1")
       result shouldBe a [EnemyAttackState]
       result.asInstanceOf[EnemyAttackState].gameState.msg2 should include ("(Keine Wirkung!)")
    }

    "transition to MenuState (Win) if enemy faints" in {
       val logic = new BattleLogicMock()
       val attack = Attack("Kill", 100, PokemonType.Normal)
       val pPoke = MockPokemon(100, 100, Vector(attack), PokemonType.Normal)
       val ePoke = MockPokemon(10, 100, Vector.empty, PokemonType.Normal)
       val gs = GameState(MockPlayer(pPoke), MockPlayer(ePoke), false, "", "")
       val state = PlayerAttackState(gs, logic)

       val result = state.handle("1")
       result shouldBe a [MenuState]
       val winState = result.asInstanceOf[MenuState]
       winState.gameState.battleOver should be (true)
       winState.gameState.msg1 should be ("GEWONNEN!")
    }
  }
}