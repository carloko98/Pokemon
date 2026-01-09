package de.htwg.se.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.model.PokemonComponent.PokemonImpl.Attack

class AttackSpec extends AnyWordSpec {

  "An Attack" should {
    "store name, damage and type" in {
      val atk = Attack("Tackle", 6, PokemonType.Normal)
      atk.name shouldBe "Tackle"
      atk.damage shouldBe 6
      atk.attackType shouldBe PokemonType.Normal
    }

    "have a readable toString" in {
      Attack("Bubble", 4, PokemonType.Water).toString should include("Bubble")
    }
  }
}
