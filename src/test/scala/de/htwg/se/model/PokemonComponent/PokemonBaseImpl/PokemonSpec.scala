package de.htwg.se.model.PokemonComponent.PokemonBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.PokemonComponent.PokemonType

class PokemonSpec extends AnyWordSpec with Matchers {
  "A Pokemon" should {
    // Jetzt mit ID, secondaryType (Option), spriteUrl
    val pokemon = Pokemon("Glumanda", 4, PokemonType.Fire, None, 100, 100, Vector.empty, "")

    "have a name and valid attributes" in {
      pokemon.name should be("Glumanda")
      pokemon.id should be(4)
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

    "return the correct toString representation" in {
      // Das Format in deiner Pokemon.scala war: s"$name (#$id) [HP: $currentHp/$maxHp]"
      pokemon.toString should be("Glumanda (#4) [HP: 100/100]")
    }
  }
}