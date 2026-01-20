package de.htwg.se.controller.controllerImpl.state

import de.htwg.se.model.GameStateComponent.GameState

case class PokeCenterState(gameState: GameState) extends ControllerState {

  override def handle(input: String): ControllerState = input.toLowerCase match {
    
    case "h" | "heilen" =>
      val healedTeam = gameState.player.team.map(p => p.withHp(p.maxHp))
      val healedPlayer = gameState.player.withTeam(healedTeam)
      val newState = gameState.copy(
        player = healedPlayer,
        msg1 = "Deine Pokémon sind wieder fit!",
        msg2 = "Möchtest du noch items kaufen oder zurück?"
      )
      PokeCenterState(newState)

    // --- ITEMS (Platzhalter) ---
    case "i" | "items" =>
      val shopState = gameState.copy(
        msg1 = "Der Shop ist noch leer!",
        msg2 = "Komm später wieder. zurück"
      )
      PokeCenterState(shopState)

    // --- ZURÜCK ZUM MENÜ ---
    case "z" | "zurück" | "b" | "back" =>
      val backState = gameState.copy(
        msg1 = "Zurück im Menü.",
        msg2 = "Wähle: [s]tart, [t]rainer oder [c]enter"
      )
      MenuState(backState)

    case _ => 
      copy(gameState = gameState.copy(msg2 = "Befehle: heilen, items, zurück"))
  }
}