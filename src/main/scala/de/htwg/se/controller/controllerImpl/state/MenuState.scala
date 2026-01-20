package de.htwg.se.controller.controllerImpl.state

import de.htwg.se.model.GameStateComponent.GameState
import de.htwg.se.model.BattleLogicComponent.BattleLogicService
import de.htwg.se.model.PokemonComponent.PokemonService

case class MenuState(gameState: GameState) extends ControllerState {
  
  override def handle(input: String): ControllerState = input match {

    case "w" | "wild" =>
      // WILDER KAMPF -> Erstelle neuen Wild-Gegner (1 Pokemon)
      val wildEnemy = PokemonService.createWildEnemy()
      
      val newGameState = gameState.copy(
        enemy = wildEnemy, // <--- HIER UPDATE
        battleOver = false,
        msg1 = "Wilder Kampf gestartet!",
        msg2 = s"Ein wildes ${wildEnemy.activePokemon.name} taucht auf!"
      )
      PlayerAttackState(newGameState, BattleLogicService.getWildLogic())

    case "t" | "trainer" =>
      // TRAINER KAMPF -> Erstelle neuen Trainer-Gegner (2 Pokemon)
      val trainerEnemy = PokemonService.createTrainerEnemy()
      
      val newGameState = gameState.copy(
        enemy = trainerEnemy, // <--- HIER UPDATE
        battleOver = false,
        msg1 = "Trainerkampf gestartet!",
        msg2 = s"${trainerEnemy.name} fordert dich heraus!"
      )
      PlayerAttackState(newGameState, BattleLogicService.getTrainerLogic())

    case "c" | "center" =>
      val centerState = gameState.copy(
        msg1 = "Willkommen im PokéCenter!",
        msg2 = "Was möchtest du tun? [h]eilen, [i]tems oder [z]urück"
      )
      PokeCenterState(centerState)


    case "q" | "quit" =>
      System.exit(0)
      this 

    case _ =>
      val newGameState = gameState.copy(msg1 = "Unbekannter Befehl!", msg2 = "[s]tart (Wild), [t]rainer oder [q]uit")
      copy(gameState = newGameState)
  }
}