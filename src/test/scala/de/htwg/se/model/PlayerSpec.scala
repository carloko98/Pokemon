package de.htwg.se.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class PlayerSpec extends AnyWordSpec with Matchers {
  "A Player" should {
    // Setup mit echten Pokemon-Objekten
    val p1 = Pokemon("P1", null, 100, 100, Vector.empty)
    val p2 = Pokemon("P2", null, 100, 0, Vector.empty) // P2 ist bereits besiegt (0 HP)
    val player = Player("Ash", Vector(p1, p2))

    "have correct initial values" in {
      player.name should be("Ash")
      player.team should be(Vector(p1, p2))
      player.currentPokemonIndex should be(0)
    }

    "return the active pokemon" in {
      player.activePokemon should be(p1)
    }

    "switch active pokemon" in {
      val switched = player.switchActivePokemon(1)
      switched.currentPokemonIndex should be(1)
      switched.activePokemon should be(p2)
    }

    "update a pokemon in the team" in {
      val p1Damage = p1.withHp(50)
      val updatedPlayer = player.updatePokemon(p1Damage)
      updatedPlayer.activePokemon.currentHp should be(50)
      // Sicherstellen, dass es im Team aktualisiert wurde
      updatedPlayer.team(0).currentHp should be(50)
    }

    "add a new pokemon" in {
      val p3 = Pokemon("P3", null, 100, 100, Vector.empty)
      val newPlayer = player.addPokemon(p3)
      newPlayer.team should have size 3
      newPlayer.team(2) should be(p3)
    }

    "check if active pokemon is fainted" in {
      player.isActiveFainted should be(false)
      
      val p1Fainted = p1.withHp(0)
      val lostPlayer = player.updatePokemon(p1Fainted)
      lostPlayer.isActiveFainted should be(true)
    }

    "check if player is completely defeated" in {
      player.isDefeated should be(false)
      
      val p1Fainted = p1.withHp(0)
      // Wenn P1 auch 0 HP hat, sind alle (P1 und P2) besiegt
      val lostPlayer = player.updatePokemon(p1Fainted)
      lostPlayer.isDefeated should be(true)
    }

    "find the next alive pokemon index" in {
      // P1 lebt, P2 tot -> Index 0
      player.nextAlivePokemonIndex should be(Some(0))

      // Wir wechseln zu P2 (tot). P1 lebt noch an Index 0.
      val switched = player.switchActivePokemon(1)
      switched.nextAlivePokemonIndex should be(Some(0))

      // Beide tot
      val p1Fainted = p1.withHp(0)
      val allDead = player.updatePokemon(p1Fainted)
      allDead.nextAlivePokemonIndex should be(None)
    }
    
    "have a correct toString representation" in {
        player.toString should be("Ash")
    }
  }
}