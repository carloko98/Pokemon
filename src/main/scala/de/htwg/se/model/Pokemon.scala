package de.htwg.se.model

import de.htwg.se.model.PokemonType

case class Pokemon(
  name: String,
  pType: PokemonType,
  maxHp: Int,
  currentHp: Int,
  attacks: Vector[Attack]
) extends PokemonInterface {
  
  def withHp(newHp: Int): Pokemon = copy(currentHp = newHp.max(0).min(maxHp))

  def isFainted: Boolean = currentHp <= 0

  override def toString: String = s"$name (HP: $currentHp/$maxHp)"
}