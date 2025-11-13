// src/main/scala/de/htwg/Main.scala
package de.htwg

import de.htwg.model.{Pokemon, Attack, PokemonType}
import de.htwg.controller.ControllerImpl
import de.htwg.view.Tui

@main def runBattleUI(): Unit = {
  // Angriffe erstellen
  val tackle = Attack("Tackle", 6, PokemonType.Normal)
  val bubble = Attack("Bubble", 4, PokemonType.Water)

  // Pokémon erstellen – OHNE LEVEL!
  val enemy = Pokemon(
    name = "HORSEA",
    pType = PokemonType.Water,
    maxHp = 40,
    currentHp = 40,
    attacks = Vector(bubble, tackle)
  )

  val player = Pokemon(
    name = "PIKACHU",
    pType = PokemonType.Electric,
    maxHp = 60,
    currentHp = 60,
    attacks = Vector(tackle)
  )

  // Controller und View starten
  val ctrl = new ControllerImpl(player, enemy)
  val tui = new Tui(ctrl)

  // Spiel starten
  tui.intro()
  tui.render()
  tui.inputLoop()
}