package de.htwg.se.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model._
import de.htwg.se.controller.state._
import de.htwg.se.model.PokemonType._

class ControllerSpec extends AnyWordSpec with Matchers {

  val pikachu = Pokemon(
    name = "Pikachu",
    maxHp = 100,
    currentHp = 100,
    pType = Electric,
    attacks = Vector(
      Attack("Thunder Shock", 40, Electric),
      Attack("Quick Attack", 30, Normal)
    )
  )

  val charizard = Pokemon(
    name = "Charizard",
    maxHp = 150,
    currentHp = 150,
    pType = Fire,
    attacks = Vector(
      Attack("Flamethrower", 90, Fire),
      Attack("Slash", 70, Normal)
    )
  )

  "A Controller" should {

    "start in TitleState" in {
      val controller = new Controller(
        Player("Ash", Vector(pikachu), 0, Vector.empty),
        Player("Gary", Vector(charizard), 0, Vector.empty)
      )
      controller.state shouldBe a[TitleState]
    }

    "handle NameInputState correctly" in {
      val initialState = NameInputState(GameState(
        player = Player("Ash", Vector(pikachu), 0, Vector.empty),
        enemy = Player("Gary", Vector(charizard), 0, Vector.empty)
      ))

      val next = initialState.handle("Eevee")
      next shouldBe a[MenuState]
      next.gameState.player.name shouldBe "Eevee"
    }

    "start a wild battle from MenuState" in {
      val menu = MenuState(GameState(
        player = Player("Ash", Vector(pikachu), 0, Vector.empty),
        enemy = Player("MissingNo", Vector.empty), // Wild Pokemon
        battleOver = false
      ))

      val next = menu.handle("s")
      next shouldBe a[PlayerAttackState]
      next.gameState.battleOver shouldBe false
    }

    "start a trainer battle from MenuState" in {
      val menu = MenuState(GameState(
        player = Player("Ash", Vector(pikachu), 0, Vector.empty),
        enemy = PokemonFactory.createRandomEnemy(),
        battleOver = false
      ))

      val next = menu.handle("t")
      next shouldBe a[PlayerAttackState]
      next.gameState.battleOver shouldBe false
    }

    "execute a player attack correctly" in {
      val enemyPokemon = PokemonFactory.getPokemon("Bisaflor")
      val enemyPlayer = Player("Team Rocket", Vector(enemyPokemon))
      val gs = GameState(
        player = Player("Ash", Vector(pikachu), 0, Vector.empty),
        enemy = enemyPlayer,
        battleOver = false
      )
      val state = PlayerAttackState(gs, WildBattleLogic)
      val next = state.handle("1") // erste Attacke
      
      // Wenn Gegner überlebt, nächste State = EnemyAttackState
      if (next.gameState.battleOver) {
        next shouldBe a[MenuState]
      } else {
        next shouldBe a[EnemyAttackState]
        next.gameState.enemy.activePokemon.currentHp should be < enemyPokemon.maxHp
      }
    }

    "execute enemy attack correctly" in {
      val player = pikachu.copy(currentHp = 50)
      val enemy = PokemonFactory.getPokemon("Zubat")
      val gs = GameState(
        player = Player("Ash", Vector(player), 0, Vector.empty),
        enemy = Player("Team Rocket", Vector(enemy)),
        battleOver = false
      )
      val state = EnemyAttackState(gs, WildBattleLogic)
      val next = state.handle("any")

      if (next.gameState.battleOver) {
        next shouldBe a[MenuState]
      } else {
        next shouldBe a[PlayerAttackState]
        next.gameState.player.activePokemon.currentHp should be < player.maxHp
      }
    }
  }
}
