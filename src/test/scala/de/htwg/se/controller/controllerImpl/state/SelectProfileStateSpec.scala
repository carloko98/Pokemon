package de.htwg.se.controller.controllerImpl.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.GameStateComponent.GameState
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PokemonComponent.{IPokemon, Attack, PokemonType}

class SelectProfileStateSpec extends AnyWordSpec with Matchers {

  case class MockPokemon() extends IPokemon {
    override def name: String = "TestMon"
    override def pType: PokemonType = PokemonType.Normal
    override def maxHp: Int = 100
    override def currentHp: Int = 100
    override def attacks: Vector[Attack] = Vector.empty
    override def isFainted: Boolean = false
    override def withHp(newHp: Int): IPokemon = this
  }

  case class MockPlayer(name: String) extends IPlayer {
    override def team: Vector[IPokemon] = Vector(MockPokemon())
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

    "stay in SelectProfileState (or transition) on other input" in {
      noException should be thrownBy state.handle("AnySaveGame")
    }
  }
}