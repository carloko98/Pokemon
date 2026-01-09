package de.htwg.se.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class GameMementoSpec extends AnyWordSpec with Matchers {
  "A GameMemento" should {
    "store and retrieve the player state" in {
      val player = Player("Ash", Vector.empty)
      val memento = GameMemento(player)
      
      memento.player should be(player)
    }
    
    "be equal to another memento with the same state" in {
      val p1 = Player("Ash")
      val m1 = GameMemento(p1)
      val m2 = GameMemento(p1)
      
      m1 should be(m2)
    }
  }
}