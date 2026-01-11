package de.htwg.se.controller.controllerImpl.state

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.model.GameStateComponent.GameState
import de.htwg.se.model.BattleLogicComponent.IBattleLogic
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PokemonComponent.{IPokemon, Attack, AttackType}

class EnemyAttackStateSpec extends AnyWordSpec with Matchers {

  // --- MOCK DEFINITIONS ---
  
  // Ein einfacher Mock für die BattleLogic
  class BattleLogicMock(fleeAllowed: Boolean = true) extends IBattleLogic {
    override def getLossMessage(name: String): String = s"$name hat verloren"
    override def getWinMessage(name: String): String = s"$name hat gewonnen"
    override def isFleeingAllowed: Boolean = fleeAllowed
  }

  // Helper zum Erstellen von Dummy-Attacken mit kontrollierter Effektivität
  // Wir erstellen eine anonyme Klasse von AttackType, um 'effectivenessAgainst' zu steuern
  def createAttackWithEffectiveness(effValue: Double): Attack = {
    val mockType = new AttackType {
      override def toString: String = "MockType"
      // Wir überschreiben die Methode, um genau den Wert zurückzugeben, den wir testen wollen
      override def effectivenessAgainst(opponent: AttackType): Double = effValue
    }
    // Name, Damage, MaxPP, Type
    Attack("TestAttack", 10, 10, mockType)
  }

  // Ein generischer Pokemon Mock
  case class MockPokemon(
      hp: Int, 
      atkList: List[Attack], 
      isFaintedVal: Boolean = false
  ) extends IPokemon {
    override def name: String = "TestMon"
    override def currentHp: Int = hp
    override def attacks: List[Attack] = atkList
    override def pType: AttackType = new AttackType { override def toString = "DefType" override def effectivenessAgainst(o: AttackType) = 1.0 }
    
    // Unwichtige Dummies
    override def attack: Int = 10
    override def defense: Int = 10
    override def speed: Int = 10
    override def withHp(newHp: Int): IPokemon = this.copy(hp = newHp)
    override def isFainted: Boolean = isFaintedVal
  }

  // Ein Player Mock
  case class MockPlayer(
      poke: IPokemon, 
      fainted: Boolean
  ) extends IPlayer {
    override def name: String = "TestPlayer"
    override def activePokemon: IPokemon = poke
    override def isActiveFainted: Boolean = fainted
    override def updatePokemon(newPoke: IPokemon): IPlayer = this.copy(poke = newPoke)
    // Unwichtig
    override def pokemonList: List[IPokemon] = List(poke)
  }

  "An EnemyAttackState" should {

    "transition to PlayerAttackState when player survives (Effectiveness 1.0)" in {
      // Setup: 1.0 Effektivität
      val attack = createAttackWithEffectiveness(1.0)
      val pPoke = MockPokemon(100, List(attack))
      val ePoke = MockPokemon(100, List(attack)) // Enemy hat nur 1 Attacke -> Random ist deterministisch
      
      val player = MockPlayer(pPoke, fainted = false)
      val enemy = MockPlayer(ePoke, fainted = false)
      
      val gs = GameState(player, enemy, battleOver = false, "", "")
      val logic = new BattleLogicMock()
      val state = EnemyAttackState(gs, logic)

      val result = state.handle("")
      
      result shouldBe a [PlayerAttackState]
      // Prüfen ob Nachricht leer ist (Standard bei 1.0)
      val casted = result.asInstanceOf[PlayerAttackState]
      casted.gameState.msg2 should include ("Schaden!")
      casted.gameState.msg2 should not include "(" 
    }

    "transition to MenuState when player faints" in {
      val attack = createAttackWithEffectiveness(1.0)
      val pPoke = MockPokemon(0, List(attack))
      val ePoke = MockPokemon(100, List(attack))
      
      // WICHTIG: fainted = true simulieren
      val player = MockPlayer(pPoke, fainted = true) 
      val enemy = MockPlayer(ePoke, fainted = false)
      
      val gs = GameState(player, enemy, battleOver = false, "", "")
      val logic = new BattleLogicMock()
      val state = EnemyAttackState(gs, logic)

      val result = state.handle("")

      result shouldBe a [MenuState]
      val casted = result.asInstanceOf[MenuState]
      casted.gameState.battleOver should be (true)
      casted.gameState.msg1 should be ("VERLOREN!")
    }

    "display 'Sehr effektiv' message when effectiveness > 1.0" in {
      val attack = createAttackWithEffectiveness(2.0)
      val pPoke = MockPokemon(100, List(attack))
      val ePoke = MockPokemon(100, List(attack))
      
      val player = MockPlayer(pPoke, fainted = false)
      val enemy = MockPlayer(ePoke, fainted = false)
      
      val gs = GameState(player, enemy, battleOver = false, "", "")
      val state = EnemyAttackState(gs, new BattleLogicMock())

      val result = state.handle("")
      
      // Wir müssen den State casten, um an msg2 zu kommen
      result shouldBe a [PlayerAttackState]
      val nextState = result.asInstanceOf[PlayerAttackState]
      nextState.gameState.msg2 should include ("Sehr effektiv!")
    }

    "display 'Nicht sehr effektiv' message when effectiveness < 1.0 but > 0" in {
      val attack = createAttackWithEffectiveness(0.5)
      val pPoke = MockPokemon(100, List(attack))
      val ePoke = MockPokemon(100, List(attack))
      
      val player = MockPlayer(pPoke, fainted = false)
      val enemy = MockPlayer(ePoke, fainted = false)
      
      val gs = GameState(player, enemy, battleOver = false, "", "")
      val state = EnemyAttackState(gs, new BattleLogicMock())

      val result = state.handle("")
      
      val nextState = result.asInstanceOf[PlayerAttackState]
      nextState.gameState.msg2 should include ("Nicht sehr effektiv...")
    }

    "display 'Keine Wirkung' message when effectiveness is 0.0" in {
      val attack = createAttackWithEffectiveness(0.0)
      val pPoke = MockPokemon(100, List(attack))
      val ePoke = MockPokemon(100, List(attack))
      
      val player = MockPlayer(pPoke, fainted = false)
      val enemy = MockPlayer(ePoke, fainted = false)
      
      val gs = GameState(player, enemy, battleOver = false, "", "")
      val state = EnemyAttackState(gs, new BattleLogicMock())

      val result = state.handle("")
      
      val nextState = result.asInstanceOf[PlayerAttackState]
      nextState.gameState.msg2 should include ("Keine Wirkung!")
    }
  }
}