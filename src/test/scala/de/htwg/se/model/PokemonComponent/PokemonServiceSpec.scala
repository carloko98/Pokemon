package de.htwg.se.model.PokemonComponent

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.PlayerComponent.IPlayer

class PokemonServiceSpec extends AnyWordSpec with Matchers {

  "PokemonService" should {
    "create a player with specific pokemon" in {
      val player = PokemonService.createPlayer("Ash", Vector("Glurak"))
      player shouldBe a [IPlayer]
      player.name should be("Ash")
      player.team.head.name should be("Glurak")
    }

    "create a random enemy" in {
      val enemy = PokemonService.createRandomEnemy()
      enemy shouldBe a [IPlayer]
      enemy.team should not be empty
    }

    "get a specific pokemon" in {
      val poke = PokemonService.getPokemon("Glurak")
      poke shouldBe a [IPokemon]
      poke.name should be("Glurak")
    }
  }
}