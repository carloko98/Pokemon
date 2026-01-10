package de.htwg.se.model.PokemonComponent.PokemonBaseImpl

// Importiert die Typen und Attacken aus dem übergeordneten Component-Package
import de.htwg.se.model.PokemonComponent.PokemonType
import de.htwg.se.model.PokemonComponent.PokemonType._ // Damit du direkt 'Fire', 'Water' schreiben kannst
import de.htwg.se.model.PokemonComponent.Attack

object PokemonDBS {

  private val entries: Map[String, Pokemon] = Map(
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

  def get(name: String): Option[Pokemon] = entries.get(name.toLowerCase)
}