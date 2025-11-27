package de.htwg.se

import de.htwg.se.model.PokemonFactory
import de.htwg.se.controller.Controller
import de.htwg.se.view.Tui

@main def runBattleUI(): Unit = {

  val dummyPlayer = PokemonFactory.createPlayer("Gast", Vector("Glurak"))
  val dummyEnemy = PokemonFactory.createRandomEnemy()

  val ctrl = new Controller(dummyPlayer, dummyEnemy)
  val tui = new Tui(ctrl)
  tui.intro()
  tui.inputLoop()
}