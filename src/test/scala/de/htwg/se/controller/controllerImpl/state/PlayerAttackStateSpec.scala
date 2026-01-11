package de.htwg.se.controller.controllerImpl.state

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.model.GameStateComponent.GameState
import de.htwg.se.model.PokemonComponent.PokemonService

class PlayerAttackStateSpec extends AnyWordSpec with Matchers {

  "A PlayerAttackState" should {
    val p = PokemonService.createPlayer("Ash", Vector("Glurak"))
    val e = PokemonService.createRandomEnemy()
    val gs = GameState(p, e, false, "", "")
    val state = PlayerAttackState(gs)

    "transition to MenuState on input 'f' (Flee)" in {
      state.handle("f") should be(a [MenuState])
    }

    "transition to EnemyAttackState on valid attack index '1'" in {
      state.handle("1") should be(a [EnemyAttackState])
    }

    "stay in PlayerAttackState on invalid input" in {
      state.handle("99") should be(a [PlayerAttackState])
      state.handle("xyz") should be(a [PlayerAttackState])
    }
  }
}