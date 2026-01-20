package de.htwg.se.controller.controllerImpl.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.GameStateComponent.GameState
import de.htwg.se.model.BattleLogicComponent.IBattleLogic
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PokemonComponent.{IPokemon, Attack, PokemonType}

class SwitchPokemonStateSpec extends AnyWordSpec with Matchers {

  class BattleLogicMock extends IBattleLogic {
    override def getLossMessage(name: String): String = ""
    override def getWinMessage(name: String): String = ""
    override def isFleeingAllowed: Boolean = true
  }

  case class MockPokemon(name: String, currentHp: Int) extends IPokemon {
    override def id: Int = 1
    override def pType: PokemonType = PokemonType.Normal
    override def secondaryType: Option[PokemonType] = None
    override def maxHp: Int = 100
    override def attacks: Vector[Attack] = Vector.empty
    override def spriteUrl: String = ""
    override def isFainted: Boolean = currentHp <= 0
    override def withHp(newHp: Int): IPokemon = this
    override def toString: String = name
  }

  case class MockPlayer(team: Vector[IPokemon], currentPokemonIndex: Int = 0) extends IPlayer {
    override def name: String = "Tester"
    override def activePokemon: IPokemon = team(currentPokemonIndex)
    override def withTeam(newTeam: Vector[IPokemon]): IPlayer = this
    override def updatePokemon(p: IPokemon): IPlayer = this
    override def switchActivePokemon(index: Int): IPlayer = copy(currentPokemonIndex = index)
    override def addPokemon(p: IPokemon): IPlayer = this
    override def items: Vector[String] = Vector.empty
    override def isActiveFainted: Boolean = activePokemon.isFainted
    override def isDefeated: Boolean = false
    override def nextAlivePokemonIndex: Option[Int] = None
  }

  "A SwitchPokemonState" should {
    val logic = new BattleLogicMock()
    val p1 = MockPokemon("Mon1", 100)
    val p2 = MockPokemon("Mon2", 100)
    val fainted = MockPokemon("K.O. Mon", 0)
    
    val player = MockPlayer(Vector(p1, p2, fainted))
    val gs = GameState(player, player, false, "", "")
    val state = SwitchPokemonState(gs, logic)

    "transition back to PlayerAttackState on 'z'" in {
      state.handle("z") shouldBe a [PlayerAttackState]
    }

    "switch pokemon successfully and give turn to enemy" in {
      val result = state.handle("2")
      result shouldBe a [EnemyAttackState]
      val nextGS = result.asInstanceOf[EnemyAttackState].gameState
      nextGS.player.activePokemon.name should be ("Mon2")
      nextGS.msg1 should include ("Du wechselst auf Mon2")
    }

    "reject switching to the already active pokemon" in {
      val result = state.handle("1").asInstanceOf[SwitchPokemonState]
      result.gameState.msg2 should include ("kämpft bereits")
    }

    "reject switching to a fainted pokemon" in {
      val result = state.handle("3").asInstanceOf[SwitchPokemonState]
      result.gameState.msg2 should include ("kampfunfähig")
    }

    "handle invalid index" in {
      val result = state.handle("99").asInstanceOf[SwitchPokemonState]
      result.gameState.msg2 should include ("Ungültiger Index")
    }

    "handle non-numeric input" in {
      val result = state.handle("abc").asInstanceOf[SwitchPokemonState]
      result.gameState.msg2 should include ("Bitte Index wählen")
    }
  }
}