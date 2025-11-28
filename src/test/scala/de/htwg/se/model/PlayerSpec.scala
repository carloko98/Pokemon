package de.htwg.se.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class PlayerSpec extends AnyWordSpec with Matchers {

  val p1 = Pokemon("Pikachu", PokemonType.Electric, 100, 100, Vector())
  val p2 = Pokemon("Glumanda", PokemonType.Fire, 80, 80, Vector())

  "A Player" should {
    "have correct team size" in {
      val player = Player("Ash", Vector(p1, p2))
      player.team.size shouldBe 2
    }

    "switch active Pokemon" in {
      val player = Player("Ash", Vector(p1, p2))
      val switched = player.switchActivePokemon(1)
      switched.currentPokemonIndex shouldBe 1
    }

    "update a Pokemon" in {
      val player = Player("Ash", Vector(p1, p2))
      val updated = player.updatePokemon(p1.copy(currentHp = 50))
      updated.activePokemon.currentHp shouldBe 50
    }
  }
}
