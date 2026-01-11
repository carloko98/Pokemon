package de.htwg.se.model.GameStateComponent.GameStateBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.PlayerComponent.IPlayer
import de.htwg.se.model.PokemonComponent.{IPokemon, PokemonType, Attack}

class GameMementoSpec extends AnyWordSpec with Matchers {

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

  "A GameMemento" should {
    "store a player state" in {
      val p = MockPlayer("Ash")
      val memento = GameMemento(p)
      
      memento.player should be(p)
    }
    
    "be comparable" in {
      val p = MockPlayer("Ash")
      val m1 = GameMemento(p)
      val m2 = GameMemento(p)
      
      m1 should be(m2)
    }
  }
}