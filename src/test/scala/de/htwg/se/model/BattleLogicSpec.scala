package de.htwg.se.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.PokemonType._
import de.htwg.se.controller.state._

class BattleLogicSpec extends AnyWordSpec with Matchers {

  "Battle logic" should {

    "perform a player attack in a wild battle" in {
      val playerPokemon = PokemonFactory.getPokemon("Pikachu")
      val player = Player("Ash", Vector(playerPokemon), 0, Vector.empty)

      // Wild-Pokémon simulieren: Spieler mit nur einem Pokémon
      val wildEnemyPokemon = PokemonFactory.getPokemon("Zubat") 
      val enemyPlayer = Player("Wild Pokemon", Vector(wildEnemyPokemon), 0, Vector.empty)

      val gameState = GameState(player, enemyPlayer, msg1 = "", msg2 = "", battleOver = false)
      val state = PlayerAttackState(gameState, WildBattleLogic)

      val nextState = state.handle("1") // wählt erste Attacke

      nextState shouldBe a[EnemyAttackState]
      nextState.gameState.enemy.activePokemon.currentHp should be < (wildEnemyPokemon.maxHp)
    }

    "end battle if enemy faints" in {
      val playerPokemon = PokemonFactory.getPokemon("Pikachu")
      val player = Player("Ash", Vector(playerPokemon), 0, Vector.empty)

      val weakEnemy = PokemonFactory.getPokemon("Zubat").withHp(1)
      val enemyPlayer = Player("Wild Pokemon", Vector(weakEnemy), 0, Vector.empty)

      val gameState = GameState(player, enemyPlayer, msg1 = "", msg2 = "", battleOver = false)
      val state = PlayerAttackState(gameState, WildBattleLogic)

      val nextState = state.handle("1")
      nextState shouldBe a[MenuState]
      nextState.gameState.battleOver shouldBe true
    }
  }
}
