// src/test/scala/de/htwg/se/controller/state/GameStateSpec.scala
package de.htwg.se.controller.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import de.htwg.se.model._
import de.htwg.se.controller.state._

class GameStateSpec extends AnyWordSpec {

  import PokemonType._
  import PokemonFactory._

  // Fertige Test-Pokémon direkt aus deiner Factory
  private val pikachu = getPokemon("Pikachu").withHp(80)
  private val glurak  = getPokemon("Glurak").withHp(120)
  private val bisaflor = getPokemon("Bisaflor").withHp(160)

  private val ash   = Player("Ash", Vector(pikachu))
  private val gary  = Player("Gary", Vector(glurak))

  private val emptyState = GameState(Player("Temp", Vector.empty), Player("Rival", Vector.empty))
  private val readyState = GameState(ash, gary)

  "TitleState" should {
    "zu NameInputState wechseln bei 'n' oder 'neu'" in {
      TitleState(emptyState).handle("n") shouldBe a[NameInputState]
      TitleState(emptyState).handle("neu") shouldBe a[NameInputState]
    }
    "zu SelectProfileState wechseln bei 'l' oder 'laden'" in {
      TitleState(emptyState).handle("l") shouldBe a[SelectProfileState]
    }
  }

  "NameInputState" should {
    
    "leeren Namen ablehnen" in {
      NameInputState(emptyState).handle("").gameState.msg2 should include("leer")
    }
    "gültigen Namen → Spieler mit Glurak + Bisaflor und zu MenuState" in {
      val next = NameInputState(emptyState).handle("Misty")
      next shouldBe a[MenuState]
      next.gameState.player.name should be("Misty")
      next.gameState.player.team.map(_.name) should contain allOf("Glurak", "Bisaflor")
    }
  }

  "MenuState" should {
    val menuReady = readyState.copy(battleOver = true)
    "Wildkampf starten bei 's'" in {
      MenuState(menuReady).handle("s") shouldBe a[PlayerAttackState]
    }
    "Trainerkampf starten bei 't'" in {
      MenuState(menuReady).handle("trainer") shouldBe a[PlayerAttackState]
    }
  }

  "PlayerAttackState" should {
    "Flucht nur im Wildkampf erlauben" in {
      PlayerAttackState(readyState, WildBattleLogic).handle("f") shouldBe a[MenuState]
      PlayerAttackState(readyState, TrainerBattleLogic).handle("f") shouldBe a[PlayerAttackState]
    }

    "Angriff ausführen → EnemyAttackState (wenn Gegner überlebt)" in {
      val next = PlayerAttackState(readyState, WildBattleLogic).handle("1") // erste Attacke
      next shouldBe a[EnemyAttackState]
      next.gameState.enemy.activePokemon.currentHp should be < 120
    }

    "bei Sieg direkt zu MenuState mit GEWONNEN!" in {
      val weakEnemyState = readyState.copy(
        enemy = gary.copy(team = Vector(glurak.withHp(10)))
      )
      val next = PlayerAttackState(weakEnemyState, WildBattleLogic).handle("1")
      next shouldBe a[MenuState]
      next.gameState.msg1 should be("GEWONNEN!")
      next.gameState.battleOver should be(true)
    }
  }

  "EnemyAttackState" should {
    "Gegner angreifen lassen" in {
      val next = EnemyAttackState(readyState, WildBattleLogic).handle("")
      next should (be(a[PlayerAttackState]) or be(a[MenuState]))
    }

    "bei Niederlage zu MenuState mit VERLOREN!" in {
      val weakPlayerState = readyState.copy(
        player = ash.copy(team = Vector(pikachu.withHp(5)))
      )
      // Mehrere Durchläufe wegen Zufall
      val results = (1 to 30).map(_ => EnemyAttackState(weakPlayerState, WildBattleLogic).handle(""))
      results.exists(r => r.isInstanceOf[MenuState] && r.gameState.msg1 == "VERLOREN!") should be(true)
    }
  }
                
}