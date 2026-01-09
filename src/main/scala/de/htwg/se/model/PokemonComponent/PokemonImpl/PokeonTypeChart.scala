package de.htwg.se.model.PokemonComponent.PokemonImpl

import de.htwg.se.model.PokemonComponent.PokemonType
import de.htwg.se.model.PokemonComponent.PokemonType._


/**
 * Statische Typ-Chart-Tabelle (Generation 6+)
 * Enthält alle bekannten Typ-Multiplikatoren
 */
object PokemonTypeChart {

  private val chart: Map[(PokemonType, PokemonType), Double] = Map(

    // NORMAL
    (Normal, Rock)   -> 0.5,
    (Normal, Ghost)  -> 0.0,
    (Normal, Steel)  -> 0.5,

    // FEUER
    (Fire, Fire)     -> 0.5,
    (Fire, Water)    -> 0.5,
    (Fire, Grass)    -> 2.0,
    (Fire, Ice)      -> 2.0,
    (Fire, Bug)      -> 2.0,
    (Fire, Rock)     -> 0.5,
    (Fire, Dragon)   -> 0.5,
    (Fire, Steel)    -> 2.0,

    // WASSER
    (Water, Fire)    -> 2.0,
    (Water, Water)   -> 0.5,
    (Water, Grass)   -> 0.5,
    (Water, Ground)  -> 2.0,
    (Water, Rock)    -> 2.0,
    (Water, Dragon)  -> 0.5,

    // ELEKTRO
    (Electric, Water)    -> 2.0,
    (Electric, Electric) -> 0.5,
    (Electric, Grass)    -> 0.5,
    (Electric, Ground)   -> 0.0,
    (Electric, Flying)   -> 2.0,
    (Electric, Dragon)   -> 0.5,

    // PFLANZE
    (Grass, Fire)    -> 0.5,
    (Grass, Water)   -> 2.0,
    (Grass, Grass)   -> 0.5,
    (Grass, Poison)  -> 0.5,
    (Grass, Ground)  -> 2.0,
    (Grass, Flying)  -> 0.5,
    (Grass, Bug)     -> 0.5,
    (Grass, Rock)    -> 2.0,
    (Grass, Dragon)  -> 0.5,
    (Grass, Steel)   -> 0.5,

    // EIS
    (Ice, Fire)      -> 0.5,
    (Ice, Water)     -> 0.5,
    (Ice, Grass)     -> 2.0,
    (Ice, Ice)       -> 0.5,
    (Ice, Ground)    -> 2.0,
    (Ice, Flying)    -> 2.0,
    (Ice, Dragon)    -> 2.0,
    (Ice, Steel)     -> 0.5,

    // KAMPF
    (Fighting, Normal)   -> 2.0,
    (Fighting, Ice)      -> 2.0,
    (Fighting, Poison)   -> 0.5,
    (Fighting, Flying)   -> 0.5,
    (Fighting, Psychic)  -> 0.5,
    (Fighting, Bug)      -> 0.5,
    (Fighting, Rock)     -> 2.0,
    (Fighting, Ghost)    -> 0.0,
    (Fighting, Dark)     -> 2.0,
    (Fighting, Steel)    -> 2.0,
    (Fighting, Fairy)    -> 0.5,

    // GIFT
    (Poison, Poison) -> 0.5,
    (Poison, Grass)  -> 2.0,
    (Poison, Ground) -> 0.5,
    (Poison, Rock)   -> 0.5,
    (Poison, Ghost)  -> 0.5,
    (Poison, Steel)  -> 0.0,
    (Poison, Fairy)  -> 2.0,

    // BODEN
    (Ground, Fire)     -> 2.0,
    (Ground, Electric) -> 2.0,
    (Ground, Grass)    -> 0.5,
    (Ground, Poison)   -> 2.0,
    (Ground, Flying)   -> 0.0,
    (Ground, Bug)      -> 0.5,
    (Ground, Rock)     -> 2.0,
    (Ground, Steel)    -> 2.0,

    // FLUG
    (Flying, Electric) -> 0.5,
    (Flying, Grass)    -> 2.0,
    (Flying, Fighting) -> 2.0,
    (Flying, Bug)      -> 2.0,
    (Flying, Rock)     -> 0.5,
    (Flying, Steel)    -> 0.5,

    // PSYCHO
    (Psychic, Fighting) -> 2.0,
    (Psychic, Poison)   -> 2.0,
    (Psychic, Psychic)  -> 0.5,
    (Psychic, Steel)    -> 0.5,
    (Psychic, Dark)     -> 0.0,

    // KÄFER
    (Bug, Fire)     -> 0.5,
    (Bug, Grass)    -> 2.0,
    (Bug, Fighting) -> 0.5,
    (Bug, Poison)   -> 0.5,
    (Bug, Flying)   -> 0.5,
    (Bug, Psychic)  -> 2.0,
    (Bug, Ghost)    -> 0.5,
    (Bug, Dark)     -> 2.0,
    (Bug, Steel)    -> 0.5,
    (Bug, Fairy)    -> 0.5,

    // STEIN
    (Rock, Fire)     -> 2.0,
    (Rock, Ice)      -> 2.0,
    (Rock, Fighting) -> 0.5,
    (Rock, Ground)   -> 0.5,
    (Rock, Flying)   -> 2.0,
    (Rock, Bug)      -> 2.0,
    (Rock, Steel)    -> 0.5,

    // GEIST
    (Ghost, Normal)  -> 0.0,
    (Ghost, Psychic) -> 2.0,
    (Ghost, Ghost)   -> 2.0,
    (Ghost, Dark)    -> 0.5,

    // DRACHE
    (Dragon, Dragon) -> 2.0,
    (Dragon, Steel)  -> 0.5,
    (Dragon, Fairy)  -> 0.0,

    // DUNKEL
    (Dark, Fighting) -> 0.5,
    (Dark, Psychic)  -> 2.0,
    (Dark, Ghost)    -> 2.0,
    (Dark, Dark)     -> 0.5,
    (Dark, Fairy)    -> 0.5,

    // STAHL
    (Steel, Fire)    -> 0.5,
    (Steel, Ice)     -> 2.0,
    (Steel, Rock)    -> 2.0,
    (Steel, Steel)   -> 0.5,
    (Steel, Fairy)   -> 2.0,

    // FEE
    (Fairy, Fire)     -> 0.5,
    (Fairy, Fighting) -> 2.0,
    (Fairy, Poison)   -> 0.5,
    (Fairy, Steel)    -> 0.5,
    (Fairy, Dragon)   -> 2.0,
    (Fairy, Dark)     -> 2.0
  )

  /**
   * Berechnet den Typ-Multiplikator
   * @return 0.0, 0.5, 1.0, 2.0
   */
  def effectiveness(attacker: PokemonType, defender: PokemonType): Double =
    chart.getOrElse((attacker, defender), 1.0)
}