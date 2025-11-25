package de.htwg.se

import de.htwg.se.model.PokemonFactory
import de.htwg.se.controller.Controller
import de.htwg.se.view.Tui

@main def runBattleUI(): Unit = {
  
  val player = PokemonFactory.createPlayer("Ash Ketchum", Vector("Glurak", "Bisaflor"))
  val enemy = PokemonFactory.createRandomEnemy()
  val ctrl = new Controller(player, enemy)
  val tui = new Tui(ctrl)

  tui.intro()
  tui.inputLoop()
}