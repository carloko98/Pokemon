package de.htwg.se.model.PokemonComponent

import de.htwg.se.model.PokemonComponent.IntPokemon

trait IntPokemonDBS {
  def get(name: String): Option[IntPokemon]
}