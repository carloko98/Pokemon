package de.htwg.se.model.GameStateComponent

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PokemonComponent.{IPokemon, PokemonType, Attack}

class GameStateSpec extends AnyWordSpec with Matchers {

  case class MockPokemon() extends IPokemon {
    override def name: String = "Mock"
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
    override def nextAlivePokemonIndex: Option[Int] = None
  }

  "A GameState" should {
    val p = MockPlayer("P1")
    val e = MockPlayer("E1")
    val gs = GameState(p, e, false, "Msg1", "Msg2")

    "have correct initial values" in {
      gs.player should be(p)
      gs.enemy should be(e)
      gs.battleOver should be(false)
      gs.msg1 should be("Msg1")
      gs.msg2 should be("Msg2")
    }

    "support copy mechanism" in {
      val newGs = gs.copy(battleOver = true, msg1 = "NewMsg")
      newGs.battleOver should be(true)
      newGs.msg1 should be("NewMsg")
      newGs.player should be(p)
    }
    
    "be equal to another instance with same values" in {
      val gs2 = GameState(p, e, false, "Msg1", "Msg2")
      gs should be(gs2)
    }
  }
}