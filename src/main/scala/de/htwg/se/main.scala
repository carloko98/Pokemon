
package de.htwg.se

import de.htwg.se.model.{Pokemon, Attack, PokemonType}
import de.htwg.se.controller.ControllerImpl
import de.htwg.se.view.Tui

@main def runBattleUI(): Unit = {
  // Angriffe und Pokemon erstellen
  val tackle = Attack("Tackle", 6, PokemonType.Normal)
  val bubble = Attack("Bubble", 4, PokemonType.Water)


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

  // Eigentliche Main
  val ctrl = new ControllerImpl(player, enemy)
  val tui = new Tui(ctrl)

  tui.intro()
  tui.render()
  tui.inputLoop()
}