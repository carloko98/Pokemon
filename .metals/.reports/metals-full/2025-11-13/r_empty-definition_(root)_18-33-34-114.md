error id: file:///D:/Desktop/AIN/WS%2025_26/Software%20Engineering/Programme_Scala/Pokemon/src/main/scala/main.scala:de/htwg/PokemonType.Water.
file:///D:/Desktop/AIN/WS%2025_26/Software%20Engineering/Programme_Scala/Pokemon/src/main/scala/main.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -de/htwg/model/PokemonType.Water.
	 -de/htwg/model/PokemonType.Water#
	 -de/htwg/model/PokemonType.Water().
	 -PokemonType.Water.
	 -PokemonType.Water#
	 -PokemonType.Water().
	 -scala/Predef.PokemonType.Water.
	 -scala/Predef.PokemonType.Water#
	 -scala/Predef.PokemonType.Water().
offset: 404
uri: file:///D:/Desktop/AIN/WS%2025_26/Software%20Engineering/Programme_Scala/Pokemon/src/main/scala/main.scala
text:
```scala
// src/main/scala/de/htwg/main.scala
package de.htwg

import de.htwg.model.{Pokemon, Attack, PokemonType}
import de.htwg.controller.ControllerImpl
import de.htwg.view.Tui

object MainApp:
  @main def runBattleUI(): Unit = {
    val tackle = Attack("Tackle", 6, PokemonType.Normal)
    val bubble = Attack("Bubble", 8, PokemonType.Water)

    val enemy = Pokemon("HORSEA", 16, PokemonType.Wate@@r, 40, 40, List(bubble, tackle))
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