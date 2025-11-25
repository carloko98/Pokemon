package de.htwg.se

import de.htwg.se.model.PokemonFactory
import de.htwg.se.controller.Controller
import de.htwg.se.view.Tui

@main def runBattleUI(): Unit = {
  
  // Erstelle Spieler mit Teams via Factory
  val player = PokemonFactory.createPlayer("Ash Ketchum", Vector("Glurak", "Bisaflor"))
  
  // Gegner ist ein zufälliger Trainer (aus der Factory Hilfsmethode)
  val enemy = PokemonFactory.createRandomEnemy()

  // Controller wird nun mit Playern initialisiert
  val ctrl = new Controller(player, enemy)
  val tui = new Tui(ctrl)

  tui.intro()
  tui.inputLoop()
}