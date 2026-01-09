package de.htwg.se.model.PokemonComponent

import de.htwg.se.model.PokemonComponent.IntPokemonType

trait IntAttack {
  def name: String
  def damage: Int
  def attackType: PokemonType
}