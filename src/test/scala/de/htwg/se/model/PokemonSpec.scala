package de.htwg.se.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.PokemonComponent.PokemonImpl.Pokemon

class PokemonSpec extends AnyWordSpec with Matchers {
  "A Pokemon" should {
    // Da wir keine Instanz von PokemonType haben, nutzen wir null. 
    // In einem echten Szenario würde hier ein Mock oder ein echter Typ (z.B. PokemonType.Fire) stehen.
    val pTypeMock = null.asInstanceOf[PokemonType]
    
    // Wir nutzen einen leeren Vector für Attacks, um Abhängigkeiten zu minimieren
    val pokemon = Pokemon("Glumanda", pTypeMock, 100, 100, Vector.empty)

    "have a name and valid attributes" in {
      pokemon.name should be("Glumanda")
      pokemon.maxHp should be(100)
      pokemon.currentHp should be(100)
      pokemon.attacks should be(Vector.empty)
    }

    "correctly update HP with withHp" in {
      val damage = pokemon.withHp(50)
      damage.currentHp should be(50)

      val heal = damage.withHp(200) // Sollte bei maxHp (100) gedeckelt werden
      heal.currentHp should be(100)

      val faint = damage.withHp(-10) // Sollte bei 0 gedeckelt werden
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