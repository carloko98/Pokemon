package de.htwg.se.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.PokemonType._

class PokemonSpec extends AnyWordSpec with Matchers {

  val tackle = Attack("Tackle", 40, Normal)
  val bubble = Attack("Bubble", 4, Water)

  "A Pokemon" should {
    "have correct attacks" in {
      val horsea = Pokemon("Horsea", Water, 50, 50, Vector(tackle, bubble))
      horsea.attacks should contain(tackle)
    }

    "update HP correctly" in {
      val pikachu = Pokemon("Pikachu", Electric, 100, 100, Vector(tackle))
      val damaged = pikachu.withHp(50)
      damaged.currentHp shouldBe 50
    }

    "faint correctly" in {
      val pikachu = Pokemon("Pikachu", Electric, 100, 0, Vector(tackle))
      pikachu.isFainted shouldBe true
    }
  }
}
