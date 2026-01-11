package de.htwg.se.model.PokemonComponent.PokemonBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.PokemonComponent.PokemonType

class PokemonSpec extends AnyWordSpec with Matchers {
  "A Pokemon" should {
    val pokemon = Pokemon("Glumanda", PokemonType.Fire, 100, 100, Vector.empty)

    "have a name and valid attributes" in {
      pokemon.name should be("Glumanda")
      pokemon.pType should be(PokemonType.Fire)
      pokemon.maxHp should be(100)
      pokemon.currentHp should be(100)
      pokemon.attacks should be(Vector.empty)
    }

    "correctly update HP with withHp" in {
      val damage = pokemon.withHp(50)
      damage.currentHp should be(50)

      val heal = damage.withHp(200) 
      heal.currentHp should be(100)

      val faint = damage.withHp(-10) 
      faint.currentHp should be(0)
    }

    "correctly report if fainted" in {
      pokemon.isFainted should be(false)
      val deadPokemon = pokemon.withHp(0)
      deadPokemon.isFainted should be(true)
    }

    "return the correct toString representation" in {
      pokemon.toString should be("Glumanda (HP: 100/100)")
    }
  }
}