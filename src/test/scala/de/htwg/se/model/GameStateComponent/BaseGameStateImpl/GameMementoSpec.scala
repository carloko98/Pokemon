package de.htwg.se.model.GameStateComponent.GameStateBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.PlayerComponent.MockPlayerImpl.MockPlayer

class GameMementoSpec extends AnyWordSpec with Matchers {

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