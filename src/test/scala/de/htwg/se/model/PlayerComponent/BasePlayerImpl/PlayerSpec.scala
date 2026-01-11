package de.htwg.se.model.PlayerComponent.BasePlayerImpl

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.PlayerComponent.PlayerBaseImpl.Player
import de.htwg.se.model.PokemonComponent.MockPokemonImpl.MockPokemon

class PlayerSpec extends AnyWordSpec with Matchers {

  "A Player" should {
    // Hier nutzen wir jetzt MockPokemon statt Pokemon
    val p1 = MockPokemon("P1", currentHp = 100)
    val p2 = MockPokemon("P2", currentHp = 0)
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
      updatedPlayer.team(0).currentHp should be(50)
    }

    "add a new pokemon" in {
      val p3 = MockPokemon("P3")
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
      val lostPlayer = player.updatePokemon(p1Fainted)
      lostPlayer.isDefeated should be(true)
    }

    "find the next alive pokemon index" in {
      player.nextAlivePokemonIndex should be(Some(0))

      val switched = player.switchActivePokemon(1)
      switched.nextAlivePokemonIndex should be(Some(0))

      val p1Fainted = p1.withHp(0)
      val allDead = player.updatePokemon(p1Fainted)
      allDead.nextAlivePokemonIndex should be(None)

      val switchedToP2 = allDead.switchActivePokemon(1)
      val p2Alive = p2.withHp(100)
      val p2AlivePlayer = switchedToP2.updatePokemon(p2Alive)
      
      p2AlivePlayer.nextAlivePokemonIndex should be(Some(1))
    }
    
    "have a correct toString representation" in {
        player.toString should be("Ash")
    }
  }
}