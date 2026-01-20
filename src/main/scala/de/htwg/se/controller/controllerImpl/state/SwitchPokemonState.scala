package de.htwg.se.controller.controllerImpl.state

import de.htwg.se.model.GameStateComponent.GameState
import de.htwg.se.model.BattleLogicComponent.IBattleLogic

case class SwitchPokemonState(gameState: GameState, logic: IBattleLogic) extends ControllerState {

  override def handle(input: String): ControllerState = {
    input.toLowerCase match {
      case "z" | "zurück" => 
        // Abbruch: Zurück zum Angriff
        PlayerAttackState(gameState, logic)
        
      case _ =>
        input.toIntOption match {
          case Some(index) => trySwitch(index - 1)
          case None => 
            copy(gameState = gameState.copy(msg2 = "Bitte Index wählen oder 'z' für Zurück."))
        }
    }
  }

  private def trySwitch(index: Int): ControllerState = {
    val player = gameState.player
    
    // 1. Prüfen: Index gültig?
    if (index < 0 || index >= player.team.size) {
      return copy(gameState = gameState.copy(msg2 = "Ungültiger Index!"))
    }

    // 2. Prüfen: Ist es das aktuelle Pokemon?
    if (index == player.currentPokemonIndex) {
      return copy(gameState = gameState.copy(msg2 = "Dieses Pokemon kämpft bereits!"))
    }

    // 3. Prüfen: Ist es besiegt?
    if (player.team(index).isFainted) {
      return copy(gameState = gameState.copy(msg2 = s"${player.team(index).name} ist kampfunfähig!"))
    }

    // Alles okay: Wechsel durchführen
    val newPlayer = player.switchActivePokemon(index)
    val switchedPoke = newPlayer.activePokemon

    val newGameState = gameState.copy(
      player = newPlayer,
      msg1 = s"Du wechselst auf ${switchedPoke.name}!",
      msg2 = "Gegner ist am Zug..."
    )

    // Wechsel kostet eine Runde -> Gegner ist dran
    EnemyAttackState(newGameState, logic)
  }
}
