package de.htwg.se.model.PokemonComponent.PokemonBaseImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class PokemonDBSSpec extends AnyWordSpec with Matchers {
  "The PokemonDBS" should {
    "find a pokemon by name (lowercase)" in {
      // "Charizard" ist der Name in deiner pokedex.json
      val result = PokemonDBS.get("charizard")
      result should be(defined)
      result.get.name should be("Charizard")
    }

    "find a pokemon by name (mixed case)" in {
      // Testet das .toLowerCase in der Implementierung
      val result = PokemonDBS.get("ChArIzArD") 
      result should be(defined)
      result.get.name should be("Charizard")
    }

    "return None for unknown pokemon" in {
      PokemonDBS.get("DasGibtsNicht") should be(None)
    }

    "contain expected default pokemon data" in {
      // Beispiel Charizard (ID 6): 
      // Weight in JSON: "90.5 kg" -> 90.5 / 2 = 45.25 -> + 100 = 145 HP
      val charizard = PokemonDBS.get("charizard").get
      charizard.maxHp should be(145)
      charizard.attacks should have size 4
      
      // Beispiel Bulbasaur (ID 1):
      // Weight in JSON: "6.9 kg" -> 6.9 / 2 = 3.45 -> + 100 = 103 HP
      val bulba = PokemonDBS.get("bulbasaur").get
      bulba.maxHp should be(103)
    }
  }
}