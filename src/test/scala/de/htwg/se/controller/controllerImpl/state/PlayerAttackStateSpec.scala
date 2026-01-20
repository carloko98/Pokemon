package de.htwg.se.controller.controllerImpl.state

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.model.GameStateComponent.GameState
import de.htwg.se.model.BattleLogicComponent.IBattleLogic
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PokemonComponent.{IPokemon, Attack, PokemonType}

class PlayerAttackStateSpec extends AnyWordSpec with Matchers {

  class BattleLogicMock(allowFlee: Boolean = true) extends IBattleLogic {
    override def getLossMessage(name: String): String = s"$name hat verloren"
    override def getWinMessage(name: String): String = s"$name hat gewonnen"
    override def isFleeingAllowed: Boolean = allowFlee
  }

  case class MockPokemon(
      name: String,
      currentHp: Int,
      pType: PokemonType,
      secondaryType: Option[PokemonType] = None,
      maxHp: Int = 100,
      id: Int = 0,
      attacks: Vector[Attack] = Vector.empty,
      spriteUrl: String = ""
  ) extends IPokemon {
    override def withHp(newHp: Int): IPokemon = copy(currentHp = newHp.max(0).min(maxHp))
    override def isFainted: Boolean = currentHp <= 0
    override def toString: String = name
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

  "PlayerAttackState" should {
    val logic = new BattleLogicMock()

    "transition to MenuState when fleeing is allowed" in {
       val pPoke = MockPokemon("P", 100, PokemonType.Normal)
       val ePoke = MockPokemon("E", 100, PokemonType.Normal)
       val gs = GameState(MockPlayer(pPoke), MockPlayer(ePoke), false, "", "")
       val result = PlayerAttackState(gs, logic).handle("f")
       
       result shouldBe a [MenuState]
       result.asInstanceOf[MenuState].gameState.battleOver should be (true)
    }

    "execute attack and transition to EnemyAttackState if enemy survives" in {
       val attack = Attack("Hit", 10, PokemonType.Normal)
       val pPoke = MockPokemon("P", 100, PokemonType.Normal, attacks = Vector(attack))
       val ePoke = MockPokemon("E", 100, PokemonType.Normal)
       val gs = GameState(MockPlayer(pPoke), MockPlayer(ePoke), false, "", "")
       
       val result = PlayerAttackState(gs, logic).handle("1")
       result shouldBe a [EnemyAttackState]
       result.asInstanceOf[EnemyAttackState].gameState.enemy.activePokemon.currentHp should be (90)
    }

    "transition to MenuState (Win) if enemy faints and has no more pokemon" in {
       val attack = Attack("Kill", 100, PokemonType.Normal)
       val pPoke = MockPokemon("P", 100, PokemonType.Normal, attacks = Vector(attack))
       val ePoke = MockPokemon("E", 10, PokemonType.Normal)
       
       // nextIndex = None -> Gegner hat keine weiteren Pokemon
       val gs = GameState(MockPlayer(pPoke), MockPlayer(ePoke, "Enemy", None), false, "", "")
       val result = PlayerAttackState(gs, logic).handle("1")
       
       result shouldBe a [MenuState]
       result.asInstanceOf[MenuState].gameState.msg1 should be ("GEWONNEN!")
    }

    "transition to EnemyAttackState if enemy faints but switches to next pokemon" in {
       val attack = Attack("Kill", 100, PokemonType.Normal)
       val pPoke = MockPokemon("P", 100, PokemonType.Normal, attacks = Vector(attack))
       val ePoke = MockPokemon("E", 10, PokemonType.Normal)
       
       // nextIndex = Some(1) -> Gegner schickt nächstes Pokemon
       val gs = GameState(MockPlayer(pPoke), MockPlayer(ePoke, "Enemy", Some(1)), false, "", "")
       val result = PlayerAttackState(gs, logic).handle("1")
       
       result shouldBe a [EnemyAttackState]
       result.asInstanceOf[EnemyAttackState].gameState.msg1 should include ("wurde besiegt!")
    }

    "handle invalid input (abc)" in {
       val pPoke = MockPokemon("P", 100, PokemonType.Normal)
       val gs = GameState(MockPlayer(pPoke), MockPlayer(pPoke), false, "", "")
       val result = PlayerAttackState(gs, logic).handle("abc")
       result shouldBe a [PlayerAttackState]
       result.asInstanceOf[PlayerAttackState].gameState.msg2 should include ("Ungültige Eingabe")
    }
  }
}