package de.htwg.se.controller.controllerImpl.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.GameStateComponent.GameState
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PokemonComponent.{IPokemon, Attack, PokemonType}

class SelectProfileStateSpec extends AnyWordSpec with Matchers {

  // 1. Mock für Pokemon (mit allen neuen Feldern)
  case class MockPokemon() extends IPokemon {
    override def name: String = "TestMon"
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

  // 2. Mock für Player (mit allen neuen Methoden)
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

  "A SelectProfileState" should {
    val p = MockPlayer("Test")
    val e = MockPlayer("Enemy")
    val gs = GameState(p, e, false, "", "")
    val state = SelectProfileState(gs)

    "transition to TitleState on input 'b'" in {
      state.handle("b") shouldBe a [TitleState]
    }

    "stay in SelectProfileState on other input" in {
      // Laut deiner Implementierung gibt 'case _ => this' zurück
      state.handle("AnySaveGame") shouldBe state
    }
  }
}