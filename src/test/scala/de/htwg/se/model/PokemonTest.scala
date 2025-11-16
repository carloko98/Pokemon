package de.htwg.se.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.model.{Attack, Pokemon}

class PokemonSpec extends AnyWordSpec {

  val tackle = Attack("Tackle", 6, PokemonType.Normal)
  val bubble = Attack("Bubble", 8, PokemonType.Water)

  val pikachu = Pokemon("PIKACHU", PokemonType.Electric, 34, 34, Vector(tackle))
  val horsea = Pokemon("HORSEA", PokemonType.Water, 40, 40, Vector(bubble, tackle))

  "A Pokemon" should {

    "have correct initial values" in {
      pikachu.name should be("PIKACHU")
      pikachu.pType should be(PokemonType.Electric)
      pikachu.maxHp should be(34)
      pikachu.currentHp should be(34)
      pikachu.attacks should contain(tackle)
    }

    "return a new instance with updated HP via withHp" in {
      val damaged = pikachu.withHp(20)
      damaged should not be theSameInstanceAs(pikachu)
      damaged.currentHp should be(20)
    }

    "clamp HP to 0 when below zero" in {
      pikachu.withHp(-5).currentHp should be(0)
    }

    "clamp HP to maxHp when above" in {
      pikachu.withHp(100).currentHp should be(34)
    }

    "return true for isFainted when HP <= 0" in {
      pikachu.copy(currentHp = 0).isFainted should be(true)
      pikachu.copy(currentHp = -1).isFainted should be(true)
      pikachu.copy(currentHp = 1).isFainted should be(false)
    }

    "have a correct toString representation" in {
      pikachu.toString should be("PIKACHU (HP: 34/34)")
      pikachu.withHp(10).toString should be("PIKACHU (HP: 10/34)")
    }

    "be equal if all fields are equal" in {
      val p1 = Pokemon("A", PokemonType.Fire, 10, 10, Vector(tackle))
      val p2 = Pokemon("A", PokemonType.Fire, 10, 10, Vector(tackle))
      p1 should be(p2)
    }

    "not be equal if any field differs" in {
      val p1 = Pokemon("A", PokemonType.Fire, 10, 10, Vector(tackle))
      val p2 = Pokemon("B", PokemonType.Fire, 10, 10, Vector(tackle))
      p1 should not be p2
    }
  }
}