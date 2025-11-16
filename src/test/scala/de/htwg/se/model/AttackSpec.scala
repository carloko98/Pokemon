package de.htwg.se.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.model.Attack

class AttackSpec extends AnyWordSpec {

  "An Attack" should {
    val tackle = Attack("Tackle", 6, PokemonType.Normal)

    "have correct name, damage, and type" in {
      tackle.name should be("Tackle")
      tackle.damage should be(6)
      tackle.attackType should be(PokemonType.Normal)
    }

    "have a meaningful toString" in {
      tackle.toString should include("Tackle")
      tackle.toString should include("6")
      tackle.toString should include("Normal")
    }
  }
}