package de.htwg.se.model.PokemonComponent.PokemonBaseImpl

import de.htwg.se.model.PokemonComponent.PokemonType
import de.htwg.se.model.PokemonComponent.Attack
import de.htwg.se.model.PokemonComponent.IPokemon

case class Pokemon(
  name: String,
  pType: PokemonType,
  maxHp: Int,
  currentHp: Int,
  attacks: Vector[Attack]
) extends IPokemon {
  
  // Implementierung der Methoden aus IPokemon
  def withHp(newHp: Int): Pokemon = copy(currentHp = newHp.max(0).min(maxHp))

  def isFainted: Boolean = currentHp <= 0

  override def toString: String = s"$name (HP: $currentHp/$maxHp)"
}