package de.htwg.se.model.PlayerComponent

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.PlayerComponent.PlayerBaseImpl.Player
import de.htwg.se.model.PokemonComponent.MockPokemonImpl.MockPokemon

class PlayerServiceSpec extends AnyWordSpec with Matchers {

  "PlayerService" should {
    "build a player correctly" in {
      val poke = MockPokemon("Pika")
      val player = PlayerService.buildPlayer("Ash", Vector(poke))
      
      player shouldBe a [Player]
      player.name should be("Ash")
      player.team should be(Vector(poke))
    }
  }
}