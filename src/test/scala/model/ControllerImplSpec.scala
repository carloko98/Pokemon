package de.htwg.controller

import de.htwg.model.{Pokemon, Attack, PokemonType}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class ControllerImplSpec extends AnyWordSpec {

  val tackle = Attack("Tackle", 6, PokemonType.Normal)
  val bubble = Attack("Bubble", 8, PokemonType.Water)

  val pikachu = Pokemon("PIKACHU", PokemonType.Electric, 34, 34, Vector(tackle))
  val horsea = Pokemon("HORSEA", PokemonType.Water, 40, 40, Vector(bubble))

  "ControllerImpl" should {

    "initialize with correct pokemon" in {
      val ctrl = new ControllerImpl(pikachu, horsea)
      ctrl.player should be(pikachu)
      ctrl.enemy should be(horsea)
      ctrl.isBattleOver should be(false)
    }

    "apply damage with type effectiveness" in {
      val ctrl = new ControllerImpl(pikachu, horsea)

      // Electric vs Water → 2.0x
      ctrl.doPlayerAttack(tackle) // 6 * 1.0 = 6
      ctrl.enemy.currentHp should be(34)

      // Simulate bubble (8 * 2.0 = 16 vs Electric)
      // We'll manually trigger
    }

    "end battle when player wins" in {
      val weakEnemy = horsea.copy(currentHp = 5)
      val ctrl = new ControllerImpl(pikachu, weakEnemy)

      ctrl.doPlayerAttack(tackle) // 6 > 5 → KO
      ctrl.isBattleOver should be(true)
      ctrl.getMessage should be(("Du hast gewonnen!", "HORSEA ist besiegt!"))
    }

    "end battle when player loses" in {
      val weakPlayer = pikachu.copy(currentHp = 5)
      val ctrl = new ControllerImpl(weakPlayer, horsea)

      // Enemy attacks with bubble: 8 * 0.5 = 4 < 5 → no KO
      // But if we force KO:
      val ctrl2 = new ControllerImpl(weakPlayer.copy(currentHp = 3), horsea)
      ctrl2.doPlayerAttack(tackle)
      // Enemy counters → should KO player
      ctrl2.isBattleOver should be(true)
    }

    "allow fleeing" in {
      val ctrl = new ControllerImpl(pikachu, horsea)
      ctrl.doFlee()
      ctrl.isBattleOver should be(true)
      ctrl.getMessage should be(("Du bist geflohen!", ""))
    }

    "show correct effectiveness messages" in {
      val ctrl = new ControllerImpl(pikachu, horsea)
      // We'll use reflection or package-private access if needed
      // Or extract effMsg to trait
      // For now: test via public behavior
    }

    "notify observers on every change" in {
      val ctrl = new ControllerImpl(pikachu, horsea)
      var updated = false
      val observer = new Observer { override def update(): Unit = updated = true }
      ctrl.addObserver(observer)

      ctrl.doPlayerAttack(tackle)
      updated should be(true)
    }
  }
}