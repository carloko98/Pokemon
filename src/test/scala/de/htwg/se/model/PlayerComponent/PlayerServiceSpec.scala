package de.htwg.se.model.PlayerComponent

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.PokemonComponent.{IPokemon, PokemonType, Attack}
import de.htwg.se.model.PlayerComponent.PlayerBaseImpl.Player

class PlayerServiceSpec extends AnyWordSpec with Matchers {

  case class MockPokemon(
      name: String,
      pType: PokemonType = PokemonType.Normal,
      maxHp: Int = 100,
      currentHp: Int = 100,
      attacks: Vector[Attack] = Vector.empty
  ) extends IPokemon {
    override def isFainted: Boolean = currentHp <= 0
    override def withHp(newHp: Int): IPokemon = copy(currentHp = newHp)
    override def toString: String = name
  }

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