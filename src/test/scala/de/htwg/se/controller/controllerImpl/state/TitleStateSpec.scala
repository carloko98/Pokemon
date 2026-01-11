package de.htwg.se.controller.controllerImpl.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.GameStateComponent.GameState
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PokemonComponent.IPokemon

class TitleStateSpec extends AnyWordSpec with Matchers {

  case class MockPokemon() extends IPokemon {
    def name = "MockPokemon"
    def pType = null
    def maxHp = 100
    def currentHp = 100
    def attacks = Vector.empty
    def isFainted = false
    def withHp(newHp: Int) = this
  }

  case class MockPlayer(name: String) extends IPlayer {
    def team = Vector(MockPokemon())
    def currentPokemonIndex = 0
    def items = Vector.empty
    def activePokemon = MockPokemon()
    def updatePokemon(p: IPokemon) = this
    def switchActivePokemon(i: Int) = this
    def addPokemon(p: IPokemon) = this
    def isActiveFainted = false
    def isDefeated = false
    def nextAlivePokemonIndex = Some(0)
  }

  "A TitleState" should {
    val p = MockPlayer("P1")
    val e = MockPlayer("P2")
    val gs = GameState(p, e, false, "", "")
    val state = TitleState(gs)

    "transition to NameInputState on input 'n'" in {
      state.handle("n") shouldBe a [NameInputState]
    }

    "transition to NameInputState on input 'neu'" in {
      state.handle("neu") shouldBe a [NameInputState]
    }

    "transition to SelectProfileState on input 'l'" in {
      state.handle("l") shouldBe a [SelectProfileState]
    }

    "transition to SelectProfileState on input 'laden'" in {
      state.handle("laden") shouldBe a [SelectProfileState]
    }

    "stay in TitleState on invalid input" in {
      state.handle("xyz") shouldBe a [TitleState]
      val result = state.handle("xyz").asInstanceOf[TitleState]
      result.gameState.msg2 should include ("[n]eues Spiel")
    }
  }
}