package de.htwg.model

/**
 * Pokémon-Klasse (Model-Layer)
 * 
 * - Immutable (copy-with)
 * - HP, Name, Typ, Attacken
 * - MVC: Nur Daten + Zustandsänderung
 */
case class Pokemon(
  name: String,
  pType: PokemonType,
  maxHp: Int,
  currentHp: Int,
  attacks: Vector[Attack]
) {
  /**
   * Erzeugt neues Pokémon mit aktualisiertem HP (immutable!)
   */
  def withHp(newHp: Int): Pokemon = copy(currentHp = newHp.max(0).min(maxHp))

  /**
   * Prüft, ob Pokémon besiegt ist
   */
  def isFainted: Boolean = currentHp <= 0

  /**
   * String-Repräsentation für TUI
   */
  override def toString: String = s"$name (HP: $currentHp/$maxHp)"
}