package de.htwg.model

// Pokémon: vollständig immutable dank case class + copy
case class Pokemon(
    name: String,               // z. B. "PIKACHU"
    level: Int,                 // Level
    pType: PokemonType,         // Typ (z. B. Electric)
    maxHp: Int,                 // Maximale HP
    currentHp: Int,             // Aktuelle HP
    attacks: List[Attack]       // Liste der verfügbaren Angriffe
) {
  // Prüft, ob Pokémon besiegt ist
  def isFainted: Boolean = currentHp <= 0

  // Gibt neues Pokémon mit geänderter HP zurück (immutable!)
  def withHp(newHp: Int): Pokemon =
    this.copy(currentHp = newHp.max(0).min(maxHp))  // clamp zwischen 0 und maxHp
}