package de.htwg.se.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class PokemonFactorySpec extends AnyWordSpec with Matchers {

  "PokemonFactory" should {

    "create a player with given Pokemon names" in {
      val player = PokemonFactory.createPlayer("Ash", Vector("Pikachu"))
      player.name shouldBe "Ash"
      player.team.map(_.name) should contain ("MissingNo") // weil Pikachu nicht im DBS ist
    }

    "create a random enemy player" in {
      val enemy = PokemonFactory.createRandomEnemy()
      enemy.name shouldBe "Team Rocket Rüpel"
      enemy.team.nonEmpty shouldBe true
      enemy.team.forall(p => p.maxHp > 0) shouldBe true
    }
  }
}
