// PokemonComponent/PokemonImpl/PokemonDBSImpl.scala
package de.htwg.se.model.PokemonComponent.PokemonImpl

import de.htwg.se.model.PokemonComponent.{IntPokemon, IntPokemonDBS, IntAttack}
import de.htwg.se.model.PokemonComponent.PokemonType._


object PokemonDBS extends IntPokemonDBS {

  private val entries: Map[String, IntPokemon] = Map(
    "glurak" -> Pokemon("Glurak", Fire, 150, 150, Vector(
      Attack("Flammenwurf", 40, Fire),
      Attack("Kratzer", 10, Normal),
      Attack("Feuerzahn", 25, Fire),
      Attack("Drachenklaue", 30, Dragon)
    )),
    "turtok" -> Pokemon("Turtok", Water, 160, 160, Vector(
      Attack("Hydropumpe", 45, Water),
      Attack("Tackle", 10, Normal),
      Attack("Aquaknarre", 20, Water),
      Attack("Biss", 15, Dark)
    )),
    "bisaflor" -> Pokemon("Bisaflor", Grass, 160, 160, Vector(
      Attack("Solarstrahl", 50, Grass),
      Attack("Tackle", 10, Normal),
      Attack("Rankenhieb", 20, Grass),
      Attack("Erdbeben", 30, Ground)
    )),
    "rattfratz" -> Pokemon("Rattfratz", Normal, 60, 60, Vector(
      Attack("Ruckzuckhieb", 15, Normal),
      Attack("Biss", 15, Dark)
    )),
    "zubat" -> Pokemon("Zubat", Poison, 50, 50, Vector(
      Attack("Blutsauger", 10, Grass),
      Attack("Superschall", 0, Normal)
    ))
  )

  override def get(name: String): Option[IntPokemon] =
    entries.get(name.toLowerCase)
}