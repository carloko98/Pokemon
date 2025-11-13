package de.htwg.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class PokemonTypeSpec extends AnyWordSpec {

  "PokemonType.effectivenessAgainst" should {

    "return correct multiplier for all type combinations" in {
      import PokemonType._

      val allTypes = PokemonType.values.toSeq

      // Teste jede Kombination
      for {
        attacker <- allTypes
        defender <- allTypes
      } {
        val eff = attacker.effectivenessAgainst(defender)
        eff should (be(0.0) or be(0.5) or be(1.0) or be(2.0))
      }
    }

    "have specific known interactions" in {
      import PokemonType._

      Electric.effectivenessAgainst(Water) should be(2.0)
      Water.effectivenessAgainst(Fire) should be(2.0)
      Electric.effectivenessAgainst(Ground) should be(0.0)
      Normal.effectivenessAgainst(Ghost) should be(0.0)
      Fire.effectivenessAgainst(Grass) should be(2.0)
      Grass.effectivenessAgainst(Water) should be(2.0)
      Ghost.effectivenessAgainst(Normal) should be(0.0)
      Dragon.effectivenessAgainst(Fairy) should be(0.0)
    }

    "be symmetric where expected (neutral)" in {
      import PokemonType._
      Fire.effectivenessAgainst(Poison) should be(1.0)
      Poison.effectivenessAgainst(Fire) should be(1.0)
    }
  }
}