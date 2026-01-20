package de.htwg.se.controller.controllerImpl.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.GameStateComponent.GameState
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PokemonComponent.{IPokemon, Attack, PokemonType}

class TitleStateSpec extends AnyWordSpec with Matchers {

  // 1. Mock für Pokemon (mit allen neuen Feldern des IPokemon Traits)
  case class MockPokemon() extends IPokemon {
    override def name: String = "MockPokemon"
    override def id: Int = 0
    override def pType: PokemonType = PokemonType.Normal
    override def secondaryType: Option[PokemonType] = None
    override def maxHp: Int = 100
    override def currentHp: Int = 100
    override def attacks: Vector[Attack] = Vector.empty
    override def spriteUrl: String = ""
    override def isFainted: Boolean = false
    override def withHp(newHp: Int): IPokemon = this
    override def toString: String = name
  }

  // 2. Mock für Player (mit allen Methoden des IPlayer Traits)
  case class MockPlayer(name: String) extends IPlayer {
    override def team: Vector[IPokemon] = Vector(MockPokemon())
    override def withTeam(newTeam: Vector[IPokemon]): IPlayer = this
    override def currentPokemonIndex: Int = 0
    override def items: Vector[String] = Vector.empty
    override def activePokemon: IPokemon = MockPokemon()
    override def updatePokemon(p: IPokemon): IPlayer = this
    override def switchActivePokemon(index: Int): IPlayer = this
    override def addPokemon(p: IPokemon): IPlayer = this
    override def isActiveFainted: Boolean = false
    override def isDefeated: Boolean = false
    override def nextAlivePokemonIndex: Option[Int] = Some(0)
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
      val result = state.handle("xyz")
      result shouldBe a [TitleState]
      result.asInstanceOf[TitleState].gameState.msg2 should include ("[n]eues Spiel")
    }
  }
}