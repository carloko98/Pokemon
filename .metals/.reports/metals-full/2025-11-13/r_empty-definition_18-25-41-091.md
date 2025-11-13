error id: file:///D:/Desktop/AIN/WS%2025_26/Software%20Engineering/Programme_Scala/Pokemon/src/main.scala:
file:///D:/Desktop/AIN/WS%2025_26/Software%20Engineering/Programme_Scala/Pokemon/src/main.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -de/htwg/model/PokemonType.
	 -PokemonType.
	 -scala/Predef.PokemonType.
offset: 251
uri: file:///D:/Desktop/AIN/WS%2025_26/Software%20Engineering/Programme_Scala/Pokemon/src/main.scala
text:
```scala
// src/main/scala/de/htwg/Main.scala
package de.htwg

import de.htwg.model.{Pokemon, Attack, PokemonType}
import de.htwg.controller.ControllerImpl
import de.htwg.view.Tui

@main def runBattleUI(): Unit = {
  val tackle = Attack("Tackle", 6, Po@@kemonType.Normal)
  val bubble = Attack("Bubble", 8, PokemonType.Water)

  val enemy = Pokemon("HORSEA", 16, PokemonType.Water, 40, 40, List(bubble, tackle))
  val player = Pokemon("PIKACHU", 12, PokemonType.Electric, 34, 34, List(tackle))

  val ctrl = new ControllerImpl(player, enemy)
  val tui  = new Tui(ctrl)

  tui.intro()
  tui.render()
  tui.inputLoop()
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 