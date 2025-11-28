package de.htwg.se.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.PokemonType.Fire

class PlayerSpec extends AnyWordSpec with Matchers {

  "A Player" should {
    val attack = Attack("Glut", 20, Fire)
    val healthyPokemon = Pokemon("Glumanda", Fire, 100, 100, Vector(attack))
    val deadPokemon = Pokemon("Glurak", Fire, 100, 0, Vector(attack))

    "be created with default values" in {
      val player = Player("Ash")
      player.name should be("Ash")
      player.team should be(Vector.empty)
      player.currentPokemonIndex should be(0)
      player.items should be(Vector.empty)
    }

    "allow adding a Pokemon" in {
      val player = Player("Ash").addPokemon(healthyPokemon)
      player.team.size should be(1)
      player.team.head should be(healthyPokemon)
    }

    "handle active Pokemon updates and switching" in {
      val player = Player("Ash", Vector(healthyPokemon))
      
      player.activePokemon should be(healthyPokemon)
      
      val damagedPokemon = healthyPokemon.copy(currentHp = 50)
      val updatedPlayer = player.updatePokemon(damagedPokemon)
      
      updatedPlayer.activePokemon.currentHp should be(50)
      
      val switchedPlayer = updatedPlayer.switchActivePokemon(0)
      switchedPlayer.currentPokemonIndex should be(0)
    }

    "correctly identify if defeated" in {
      val alivePlayer = Player("Ash", Vector(healthyPokemon, deadPokemon))
      alivePlayer.isDefeated should be(false)

      val deadPlayer = Player("Gary", Vector(deadPokemon))
      deadPlayer.isDefeated should be(true)
    }

    "find the next alive pokemon index" in {
      val team = Vector(deadPokemon, healthyPokemon)
      val player = Player("Ash", team)
      player.nextAlivePokemonIndex should be(Some(1))

      val deadTeam = Vector(deadPokemon, deadPokemon)
      val deadPlayer = Player("Gary", deadTeam)
      deadPlayer.nextAlivePokemonIndex should be(None)
      
      val aliveTeam = Vector(healthyPokemon)
      val alivePlayer = Player("Red", aliveTeam)
      alivePlayer.nextAlivePokemonIndex should be(Some(0))
    }

    "return the correct toString representation" in {
      val player = Player("Ash")
      player.toString should be("Ash")
    }
    
    "check if active pokemon is fainted" in {
      val p1 = Player("Ash", Vector(healthyPokemon))
      p1.isActiveFainted should be(false)
      
      val p2 = Player("Gary", Vector(deadPokemon))
      p2.isActiveFainted should be(true)
    }
  }
}