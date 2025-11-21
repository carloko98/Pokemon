package de.htwg.se.controller

import de.htwg.se.model.{Pokemon, Attack, PokemonType}
import de.htwg.se.util.Observer
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class ControllerSpec extends AnyWordSpec {

  val tackle = Attack("Tackle", 10, PokemonType.Normal)
  val bubble = Attack("Bubble", 10, PokemonType.Water)
  
  val basePikachu = Pokemon("PIKACHU", PokemonType.Electric, 100, 100, Vector(tackle))
  val baseHorsea = Pokemon("HORSEA", PokemonType.Water, 100, 100, Vector(bubble))


  class TestObserver extends Observer {
    var updated: Boolean = false
    override def update(): Unit = updated = true
  }

  "Ein Controller" should {

    "korrekt initialisiert werden" in {
      val ctrl = new Controller(basePikachu.copy(), baseHorsea.copy())
      ctrl.getPlayer should be(basePikachu)
      ctrl.getEnemy should be(baseHorsea)
      ctrl.isBattleOver should be(false)
      ctrl.getMessage should be(("", ""))
    }

    "Observer benachrichtigen, wenn sich der State ändert" in {
      val ctrl = new Controller(basePikachu.copy(), baseHorsea.copy())
      val observer = new TestObserver
      ctrl.add(observer) 

      ctrl.doPlayerAttack(tackle)
      observer.updated should be(true)
    }

    "den GameState aktualisieren (Schaden berechnen)" in {
      val enemyWithOneAttack = baseHorsea.copy(attacks = Vector(bubble))
      val ctrl = new Controller(basePikachu.copy(), enemyWithOneAttack)
      
      val initialEnemyHp = ctrl.getEnemy.currentHp
      val initialPlayerHp = ctrl.getPlayer.currentHp

      ctrl.doPlayerAttack(tackle)
      ctrl.getEnemy.currentHp should be(initialEnemyHp - 10)
      ctrl.getPlayer.currentHp should be(initialPlayerHp - 10)
      ctrl.getMessage._1 should include("setzte Bubble ein")
    }

    "den Kampf beenden, wenn der Gegner besiegt ist" in {
      val weakHorsea = baseHorsea.withHp(5)
      val ctrl = new Controller(basePikachu.copy(), weakHorsea)

      ctrl.doPlayerAttack(tackle)

      ctrl.isBattleOver should be(true)
      ctrl.getMessage._1 should be("Du hast gewonnen!")
      ctrl.getMessage._2 should include("HORSEA ist besiegt")
      ctrl.getEnemy.isFainted should be(true)
    }

    "den Kampf beenden, wenn der Spieler besiegt wird" in {
      val weakPikachu = basePikachu.withHp(5)
      val strongHorsea = baseHorsea.copy(attacks = Vector(tackle)) 
      
      val ctrl = new Controller(weakPikachu, strongHorsea)

      ctrl.doPlayerAttack(tackle)

      ctrl.isBattleOver should be(true)
      ctrl.getMessage._1 should be("Du hast verloren!")
      ctrl.getMessage._2 should include("PIKACHU ist besiegt")
      ctrl.getPlayer.isFainted should be(true)
    }

    "Fliehen verarbeiten" in {
      val ctrl = new Controller(basePikachu.copy(), baseHorsea.copy())
      
      ctrl.doFlee()
      
      ctrl.isBattleOver should be(true)
      ctrl.getMessage._1 should be("Du bist geflohen!")
    }

    "Keine Aktionen mehr zulassen, wenn Kampf vorbei ist" in {
      val ctrl = new Controller(basePikachu.copy(), baseHorsea.copy())
      ctrl.doFlee()
      val hpBefore = ctrl.getEnemy.currentHp
      val observer = new TestObserver
      ctrl.add(observer)
      ctrl.doPlayerAttack(tackle)
      ctrl.getEnemy.currentHp should be(hpBefore)
      observer.updated should be(false) 
    }

    "die privaten Effektivitäts-Nachrichten korrekt formatieren" in {
      val ctrl = new Controller(basePikachu.copy(), baseHorsea.copy())
      
      val method = classOf[Controller].getDeclaredMethod("effMsg", classOf[Double])
      method.setAccessible(true)

      val msgSuper = method.invoke(ctrl, 2.0).asInstanceOf[String]
      msgSuper should include("Sehr effektiv!")

      val msgNot = method.invoke(ctrl, 0.5).asInstanceOf[String]
      msgNot should include("Nicht sehr effektiv...")

      val msgZero = method.invoke(ctrl, 0.0).asInstanceOf[String]
      msgZero should include("Hat keine Wirkung!")

      val msgNeutral = method.invoke(ctrl, 1.0).asInstanceOf[String]
      msgNeutral should be("")
    }
  }
}