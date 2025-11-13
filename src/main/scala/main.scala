// src/main/scala/de/htwg/Main.scala
package de.htwg

import de.htwg.model.{Pokemon, Attack, PokemonType}
import de.htwg.controller.ControllerImpl
import de.htwg.view.Tui

// Hauptmethode – wird von sbt ausgeführt
@main def runBattleUI(): Unit = {
  // Angriffe erstellen
  val tackle = Attack("Tackle", 6, PokemonType.Normal)
  val bubble = Attack("Bubble", 8, PokemonType.Water)

  // Pokémon erstellen
  val enemy = Pokemon("HORSEA", 16, PokemonType.Water, 40, 40, List(bubble, tackle))
  val player = Pokemon("PIKACHU", 12, PokemonType.Electric, 34, 34, List(tackle))

  // Controller und View starten
  val ctrl = new ControllerImpl(player, enemy)
  val tui = new Tui(ctrl)

  // Spiel starten
  tui.intro()
  tui.render()
  tui.inputLoop()
}