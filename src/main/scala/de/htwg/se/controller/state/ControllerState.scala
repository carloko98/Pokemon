package de.htwg.se.controller.state

import de.htwg.se.model.GameState

trait ControllerState {
  val gameState: GameState
  def handle(input: String): ControllerState

  def currentPhase: String                  
  def prompt: String                       
  def hint: String                          
  def allowedInputs: Set[String]
}