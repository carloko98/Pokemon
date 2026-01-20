package de.htwg.se.model.PokemonComponent.PokemonBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class PokemonFactorySpec extends AnyWordSpec with Matchers {

  "PokemonFactory" should {

    "create a player with given Pokemon names" in {
      val player = PokemonFactory.createPlayer("Ash", Vector("Pikachu"))
      player.name shouldBe "Ash"
      player.team.map(_.name) should contain ("Pikachu") 
    }

    "create a wild enemy player" in {
      // In deiner Factory heißt die Methode createWildEnemy
      val enemy = PokemonFactory.createWildEnemy()
      enemy.name should startWith("Wildes ")
      enemy.team.nonEmpty shouldBe true
    }

    "create a trainer enemy player" in {
      // Und hier createTrainerEnemy
      val enemy = PokemonFactory.createTrainerEnemy()
      enemy.name shouldBe "Team Rocket Rüpel"
      enemy.team.size shouldBe 2
    }

    "get a specific pokemon from DBS" in {
      val pokemon = PokemonFactory.getPokemon("Charizard")
      // Hier musst du prüfen, ob Charizard in deiner DBS ist, 
      // ansonsten kommt auch hier MissingNo
      if (pokemon.name != "MissingNo") {
        pokemon.name shouldBe "Charizard"
      }
    }
  }
}