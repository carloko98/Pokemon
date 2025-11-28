package de.htwg.se.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.PokemonType.Fire

class PokemonSpec extends AnyWordSpec with Matchers {

  "A Pokemon" should {
    val attack = Attack("Glut", 20, Fire)
    val pokemon = Pokemon("Glumanda", Fire, 100, 50, Vector(attack))

    "have a correct toString representation" in {
      pokemon.toString should be("Glumanda (HP: 50/100)")
    }

    "correctly update HP with withHp" in {
      val damagedPokemon = pokemon.withHp(30)
      damagedPokemon.currentHp should be(30)

      val healedOverMax = pokemon.withHp(200)
      healedOverMax.currentHp should be(100)

      val deadPokemon = pokemon.withHp(-50)
      deadPokemon.currentHp should be(0)
    }

    "correctly detect if it is fainted" in {
      pokemon.isFainted should be(false)
      
      val deadPokemon = pokemon.withHp(0)
      deadPokemon.isFainted should be(true)
    }
  }
}