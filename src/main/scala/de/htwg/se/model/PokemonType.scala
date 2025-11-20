package de.htwg.se.model
 
enum PokemonType {
  case Normal, Fire, Water, Grass, Electric, Ice, Fighting, Poison, Ground,
       Flying, Psychic, Bug, Rock, Ghost, Dragon, Dark, Steel, Fairy

   //**Quelle**: Pokémon Database (pokemondb.net/type)

  def effectivenessAgainst(defender: PokemonType): Double = (this, defender) match {
    
    // =====================================================
    // NORMAL: Schwach gegen Rock, immun gegen Ghost
    // =====================================================
    case (Normal, Rock)   => 0.5
    case (Normal, Ghost)  => 0.0
    case (Normal, Steel)  => 0.5

    // =====================================================
    // FEUER: Stark gegen Gras/Bug/Stahl/Eis, schwach gegen Wasser/Feuer/Rock/Drache
    // =====================================================
    case (Fire, Fire)     => 0.5
    case (Fire, Water)    => 0.5
    case (Fire, Grass)    => 2.0
    case (Fire, Ice)      => 2.0
    case (Fire, Bug)      => 2.0
    case (Fire, Rock)     => 0.5
    case (Fire, Dragon)   => 0.5
    case (Fire, Steel)    => 2.0

    // =====================================================
    // WASSER: Stark gegen Feuer/Boden/Rock, schwach gegen Gras/Wasser/Drache
    // =====================================================
    case (Water, Fire)    => 2.0
    case (Water, Water)   => 0.5
    case (Water, Grass)   => 0.5
    case (Water, Ground)  => 2.0
    case (Water, Rock)    => 2.0
    case (Water, Dragon)  => 0.5

    // =====================================================
    // ELEKTRO: Stark gegen Wasser/Flug, schwach gegen Electro/Grass/Dragon, immun gegen Boden
    // =====================================================
    case (Electric, Water)    => 2.0
    case (Electric, Electric) => 0.5
    case (Electric, Grass)    => 0.5
    case (Electric, Ground)   => 0.0
    case (Electric, Flying)   => 2.0
    case (Electric, Dragon)   => 0.5

    // =====================================================
    // PFLANZE: Stark gegen Wasser/Boden/Rock, schwach gegen Feuer/Flug/Gift
    // =====================================================
    case (Grass, Fire)    => 0.5
    case (Grass, Water)   => 2.0
    case (Grass, Grass)   => 0.5
    case (Grass, Poison)  => 0.5
    case (Grass, Ground)  => 2.0
    case (Grass, Flying)  => 0.5
    case (Grass, Bug)     => 0.5
    case (Grass, Rock)    => 2.0
    case (Grass, Dragon)  => 0.5
    case (Grass, Steel)   => 0.5

    // =====================================================
    // EIS: Stark gegen Gras/Boden/Flug/Drache, schwach gegen Feuer/Wasser/Stahl
    // =====================================================
    case (Ice, Fire)      => 0.5
    case (Ice, Water)     => 0.5
    case (Ice, Grass)     => 2.0
    case (Ice, Ice)       => 0.5
    case (Ice, Ground)    => 2.0
    case (Ice, Flying)    => 2.0
    case (Ice, Dragon)    => 2.0
    case (Ice, Steel)     => 0.5

    // =====================================================
    // KAMPF: Stark gegen Normal/Eis/Rock/Dunkel/Stahl, immun gegen Geist
    // =====================================================
    case (Fighting, Normal)  => 2.0
    case (Fighting, Ice)     => 2.0
    case (Fighting, Poison)  => 0.5
    case (Fighting, Flying)  => 0.5
    case (Fighting, Psychic) => 0.5
    case (Fighting, Bug)     => 0.5
    case (Fighting, Rock)    => 2.0
    case (Fighting, Ghost)   => 0.0
    case (Fighting, Dark)    => 2.0
    case (Fighting, Steel)   => 2.0
    case (Fighting, Fairy)   => 0.5

    // =====================================================
    // GIFT: Stark gegen Gras/Fee, immun gegen Stahl
    // =====================================================
    case (Poison, Poison) => 0.5
    case (Poison, Grass)  => 2.0
    case (Poison, Ground) => 0.5
    case (Poison, Rock)   => 0.5
    case (Poison, Ghost)  => 0.5
    case (Poison, Steel)  => 0.0
    case (Poison, Fairy)  => 2.0

    // =====================================================
    // BODE: Stark gegen Feuer/Elektro/Gift/Rock/Stahl, immun gegen Flug
    // =====================================================
    case (Ground, Fire)     => 2.0
    case (Ground, Electric) => 2.0
    case (Ground, Grass)    => 0.5
    case (Ground, Poison)   => 2.0
    case (Ground, Flying)   => 0.0
    case (Ground, Bug)      => 0.5
    case (Ground, Rock)     => 2.0
    case (Ground, Steel)    => 2.0

    // =====================================================
    // FLUG: Stark gegen Gras/Kampf/Käfer, schwach gegen Elektro/Rock/Stahl
    // =====================================================
    case (Flying, Electric) => 0.5
    case (Flying, Grass)    => 2.0
    case (Flying, Fighting) => 2.0
    case (Flying, Bug)      => 2.0
    case (Flying, Rock)     => 0.5
    case (Flying, Steel)    => 0.5

    // =====================================================
    // PSYSTAB: Stark gegen Kampf/Gift, immun gegen Dunkel
    // =====================================================
    case (Psychic, Fighting) => 2.0
    case (Psychic, Poison)   => 2.0
    case (Psychic, Psychic)  => 0.5
    case (Psychic, Steel)    => 0.5
    case (Psychic, Dark)     => 0.0

    // =====================================================
    // KÄFER: Stark gegen Psy/Dunkel/Gras, schwach gegen Feuer/Flug/Kampf
    // =====================================================
    case (Bug, Fire)     => 0.5
    case (Bug, Grass)    => 2.0
    case (Bug, Fighting) => 0.5
    case (Bug, Poison)   => 0.5
    case (Bug, Flying)   => 0.5
    case (Bug, Psychic)  => 2.0
    case (Bug, Ghost)    => 0.5
    case (Bug, Dark)     => 2.0
    case (Bug, Steel)    => 0.5
    case (Bug, Fairy)    => 0.5

    // =====================================================
    // STEIN: Stark gegen Feuer/Eis/Flug/Käfer, schwach gegen Kampf/Boden/Stahl
    // =====================================================
    case (Rock, Fire)     => 2.0
    case (Rock, Ice)      => 2.0
    case (Rock, Fighting) => 0.5
    case (Rock, Ground)   => 0.5
    case (Rock, Flying)   => 2.0
    case (Rock, Bug)      => 2.0
    case (Rock, Steel)    => 0.5

    // =====================================================
    // GEIST: Stark gegen Psy/Geist, immun gegen Normal
    // =====================================================
    case (Ghost, Normal)  => 0.0
    case (Ghost, Psychic) => 2.0
    case (Ghost, Ghost)   => 2.0
    case (Ghost, Dark)    => 0.5

    // =====================================================
    // DRACHE: Stark gegen Drache, immun gegen Fee
    // =====================================================
    case (Dragon, Dragon) => 2.0
    case (Dragon, Steel)  => 0.5
    case (Dragon, Fairy)  => 0.0

    // =====================================================
    // DUNKEL: Stark gegen Psy/Geist, schwach gegen Kampf/Fee
    // =====================================================
    case (Dark, Fighting) => 0.5
    case (Dark, Psychic)  => 2.0
    case (Dark, Ghost)    => 2.0
    case (Dark, Dark)     => 0.5
    case (Dark, Fairy)    => 0.5

    // =====================================================
    // STAHL: Stark gegen Eis/Rock/Fee, schwach gegen Feuer/Wasser/Stahl
    // =====================================================
    case (Steel, Fire)    => 0.5
    case (Steel, Ice)     => 2.0
    case (Steel, Rock)    => 2.0
    case (Steel, Steel)   => 0.5
    case (Steel, Fairy)   => 2.0

    // =====================================================
    // FEE: Stark gegen Kampf/Drache/Dunkel, schwach gegen Gift/Stahl/Feuer
    // =====================================================
    case (Fairy, Fire)     => 0.5
    case (Fairy, Fighting) => 2.0
    case (Fairy, Poison)   => 0.5
    case (Fairy, Steel)    => 0.5
    case (Fairy, Dragon)   => 2.0
    case (Fairy, Dark)     => 2.0

    // =====================================================
    // **NEUTRAL**: Alles andere = 1.0
    // =====================================================
    case _ => 1.0
  }
}