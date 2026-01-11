package de.htwg.se.model.PokemonComponent.PokemonBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class PokemonDBSSpec extends AnyWordSpec with Matchers {
  "The PokemonDBS" should {
    "find a pokemon by name (lowercase)" in {
      val result = PokemonDBS.get("glurak")
      result should be(defined)
      result.get.name should be("Glurak")
    }

    "find a pokemon by name (mixed case)" in {
      // Testet das .toLowerCase in der Implementierung
      val result = PokemonDBS.get("Glurak") 
      result should be(defined)
    }

    "return None for unknown pokemon" in {
      PokemonDBS.get("MissingNo") should be(None)
    }

    "contain expected default pokemon data" in {
      // Prüft exemplarisch, ob die Werte stimmen (Deckung der Map-Initialisierung)
      val glurak = PokemonDBS.get("glurak").get
      glurak.maxHp should be(150)
      glurak.attacks should have size 4
      
      val rattfratz = PokemonDBS.get("rattfratz").get
      rattfratz.maxHp should be(60)
    }
  }
}