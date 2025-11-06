package de.htwg

case class Pokemon(
    name: String,
    level: Int,
    pType: PokemonType,
    maxHp: Int,
    currentHp: Int,
    attacks: List[Attack]
) {
    def isFainted: Boolean = currentHp <= 0
    def withHp(newHp: Int): Pokemon = this.copy(currentHp = newHp.max(0).min(maxHp))
}