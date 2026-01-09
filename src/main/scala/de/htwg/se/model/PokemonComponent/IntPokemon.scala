// PokemonComponent/IntPokemon.scala
package de.htwg.se.model.PokemonComponent

import de.htwg.se.model.PokemonComponent.IntAttack

trait IntPokemon {
  def name: String
  def pType: PokemonType
  def maxHp: Int
  def currentHp: Int
  def attacks: Vector[IntAttack]

  def isFainted: Boolean
  def withHp(newHp: Int): IntPokemon   // ← Rückgabetyp ist das Interface!
}