package de.htwg.controller

import de.htwg.model.{Pokemon, Attack}

// Öffentliche Schnittstelle für die View (TUI)
trait Controller {
  // Aktuelle Pokémon
  def player: Pokemon
  def enemy: Pokemon

  // Zustand
  def isBattleOver: Boolean
  def getMessage: (String, String)  // (Zeile 1, Zeile 2)

  // Aktionen
  def doPlayerAttack(attack: Attack): Unit
  def doFlee(): Unit

  // Observer-Pattern: View wird benachrichtigt
  def addObserver(o: Observer): Unit
  def notifyObservers(): Unit
}

trait Observer {
  def update(): Unit  // Wird aufgerufen, wenn sich etwas ändert
}