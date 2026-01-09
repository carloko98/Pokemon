package de.htwg.se.model.PokemonComponent

import de.htwg.se.model.PokemonComponent.PokemonImpl.PokemonTypeChart

trait IntPokemonType {
  def name: String
}


enum PokemonType extends IntPokemonType {
  case Normal, Fire, Water, Grass, Electric, Ice, Fighting, Poison, Ground,
       Flying, Psychic, Bug, Rock, Ghost, Dragon, Dark, Steel, Fairy

  override def name: String = this.toString

  def effectivenessAgainst(defender: PokemonType): Double =
    PokemonTypeChart.effectiveness(this, defender)
}