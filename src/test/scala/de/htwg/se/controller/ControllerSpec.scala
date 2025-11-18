package de.htwg.se.controller

import de.htwg.se.model.{Pokemon, Attack, PokemonType}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class ControllerSpec extends AnyWordSpec {

  // ==================== TEST-DATEN ====================
  val tackle = Attack("Tackle", 10, PokemonType.Normal)
  val bubble = Attack("Bubble", 15, PokemonType.Water)
  val leafBlade = Attack("Leaf Blade", 20, PokemonType.Grass)
  val electroBall = Attack("Electro Ball", 20, PokemonType.Electric)

  // Basis-Pokémon (Vollständig gesund)
  val basePikachu = Pokemon("PIKACHU", PokemonType.Electric, 100, 100, Vector(electroBall, tackle))
  val baseHorsea = Pokemon("HORSEA", PokemonType.Water, 100, 100, Vector(bubble, tackle))
  val baseDigda = Pokemon("DIGDA", PokemonType.Ground, 100, 100, Vector(tackle))
  val baseBisasam = Pokemon("BISASAM", PokemonType.Grass, 100, 100, Vector(leafBlade))

  "Ein Controller" should {

    "korrekt initialisiert werden" in {
      val ctrl = Controller(basePikachu, baseHorsea)
      ctrl.player should be(basePikachu)
      ctrl.enemy should be(baseHorsea)
      ctrl.isBattleOver should be(false)
      ctrl.getMessage should be(("", ""))
    }

    "den Kampf beenden, wenn der Spieler gewinnt" in {
      // Setup: Gegner hat nur 5 HP
      val weakEnemy = baseHorsea.withHp(5)
      val ctrl = Controller(basePikachu, weakEnemy)

      // Aktion: Spieler greift an (10 Schaden > 5 HP)
      val nextState = ctrl.doPlayerAttack(tackle)

      // Check: Kampf vorbei, Spieler gewonnen
      nextState.isBattleOver should be(true)
      nextState.getMessage._1 should be("Du hast gewonnen!")
      nextState.getMessage._2 should be("HORSEA ist besiegt!")
    }

    "den Kampf beenden, wenn der Spieler verliert" in {
      // Setup: Spieler hat nur 5 HP, Gegner ist stark
      val weakPlayer = basePikachu.withHp(5)
      val ctrl = Controller(weakPlayer, baseHorsea)

      // Aktion: Spieler greift an (Gegner überlebt), Gegner greift zurück (Tackle/Bubble > 5 HP)
      val nextState = ctrl.doPlayerAttack(tackle)

      // Check: Kampf vorbei, Spieler verloren
      nextState.isBattleOver should be(true)
      nextState.getMessage._1 should be("Du hast verloren!")
      nextState.getMessage._2 should be("PIKACHU ist besiegt!")
    }

    "Fliehen korrekt verarbeiten" in {
      val ctrl = Controller(basePikachu, baseHorsea)

      // Aktion: Flucht
      val fledState = ctrl.doFlee()

      // Check: Kampf vorbei, korrekte Nachricht
      fledState.isBattleOver should be(true)
      fledState.getMessage._1 should be("Du bist geflohen!")
    }

    "Aktionen ignorieren, wenn der Kampf bereits vorbei ist" in {
      // 1. Wir erstellen einen Controller, der bereits im "GameOver"-Zustand ist
      val battleOverCtrl = Controller(basePikachu, baseHorsea, battleOver = true)

      // 2. Wir versuchen anzugreifen
      val sameState = battleOverCtrl.doPlayerAttack(tackle)

      // 3. Assert: Es sollte exakt dasselbe Objekt zurückkommen (keine Änderung)
      sameState should be(battleOverCtrl)
      
      // 4. Wir versuchen zu fliehen
      val sameState2 = battleOverCtrl.doFlee()
      sameState2 should be(battleOverCtrl)
    }

    "Typ-Effektivität korrekt berechnen" in {
      // Setup: Starker Gegner, damit er nicht stirbt (und zurückschlagen kann)
      val strongHorsea = baseHorsea.withHp(100) 
      val ctrlSuper = Controller(basePikachu, strongHorsea)
      
      // --- Test 1: SEHR EFFEKTIV (Elektro vs Wasser) ---
      // Electro Ball hat 20 Schaden. x 2.0 = 40 Schaden erwartet.
      val resSuper = ctrlSuper.doPlayerAttack(electroBall)
      
      val damageDealt = strongHorsea.currentHp - resSuper.enemy.currentHp
      damageDealt should be(40) // Beweis für 2.0x Multiplikator

      // --- Test 2: NICHT SEHR EFFEKTIV (Wasser vs Wasser) ---
      // Bubble hat 15 Schaden. x 0.5 = 7.5 -> abgerundet 7 Schaden erwartet.
      val ctrlNot = Controller(baseHorsea, baseHorsea) // Wasser vs Wasser
      val resNot = ctrlNot.doPlayerAttack(bubble)
      
      val damageDealtNot = baseHorsea.currentHp - resNot.enemy.currentHp
      damageDealtNot should be(7) // Beweis für 0.5x Multiplikator

      // --- Test 3: IMMUN (Elektro vs Boden) ---
      // Electro Ball (20) vs Digda (Boden) -> 0 Schaden erwartet.
      val ctrlImmune = Controller(basePikachu, baseDigda)
      val resImmune = ctrlImmune.doPlayerAttack(electroBall)
      
      val damageDealtImmune = baseDigda.currentHp - resImmune.enemy.currentHp
      damageDealtImmune should be(0) // Beweis für 0.0x Multiplikator
      
      // Hier können wir sogar den Text prüfen, weil bei 0 Schaden oft
      // "Hat keine Wirkung" stehen bleibt oder als Teil des Angriffs formatiert ist,
      // aber der HP-Test ist viel sicherer.
    }
  }
}