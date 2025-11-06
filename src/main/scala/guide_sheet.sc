// ========================================================
// Pokemon Battle Worksheet
// Für VS Code (Scala Worksheet Simulation)
// ========================================================
// ========================================================
// Pokemon Battle Worksheet
// Für VS Code (Scala Worksheet Simulation)
// ========================================================


// ========================================================
// TODO 1: Core Data Structures (Beispiel implementiert)
// ========================================================


enum PokemonType {
case Water, Fire, Plant, Electric, Normal, Flying
}


case class Attack(name: String, damage: Int, attackType: PokemonType)


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


// ========================================================
// TODO 2: Beispiel-Pokémon erstellen (Beispiel implementiert)
// ========================================================


// Beispiel-Attacken
val tackle = Attack("Tackle", 6, PokemonType.Normal)
val bubble = Attack("Bubble", 8, PokemonType.Water)
val ember = Attack("Ember", 7, PokemonType.Fire)


// Beispiel-Pokémon
val enemy = Pokemon("HORSEA", 16, PokemonType.Water, maxHp = 40, currentHp = 40, attacks = List(bubble, tackle))
val player = Pokemon("SHELLY", 12, PokemonType.Water, maxHp = 34, currentHp = 34, attacks = List(tackle, ember))


// Testausgabe
println(s"Spieler: ${player.name}, HP: ${player.currentHp}/${player.maxHp}, Attacken: ${player.attacks.map(_.name)}")
println(s"Gegner: ${enemy.name}, HP: ${enemy.currentHp}/${enemy.maxHp}, Attacken: ${enemy.attacks.map(_.name)}")

// ========================================================
// TODO 3: Einfache Kampf-Logik
// ========================================================
// - Funktion applyDamage(attacker, defender, attack) -> (neues Pokémon, Schaden)
// - Prüft, ob Pokémon besiegt ist (isFainted)
// - Optional: kleiner Zufallsfaktor für Variation im Schaden

// ========================================================
// TODO 4: Text UI erweitern
// ========================================================
// - renderBattleScreen(enemy, player, message1, message2)
// - Nutze bestehende Hilfsfunktionen: padRight, line, width
// - Zeige Name + Level, HP-Bar (berechnet aus currentHp/maxHp)
// - ASCII Platzhalter für Sprite (z.B. "<Water Sprite>")

// ========================================================
// TODO 5: Spielerinteraktion
// ========================================================
// - readLine() für Spieleraktionen
// - Optionen: Attacke wählen, Status anzeigen, Flucht
// - Nachrichten (message1, message2) dynamisch aktualisieren

// ========================================================
// TODO 6: Game Loop
// ========================================================
// - Solange beide Pokémon nicht besiegt sind:
//   1. renderBattleScreen aufrufen
//   2. Spieleraktion lesen
//   3. Aktion ausführen (applyDamage)
//   4. Gegnerzug, falls noch aktiv
//   5. Nachrichten aktualisieren
// - Nach Kampfende: Gewinnernachricht anzeigen

// ========================================================
// Optional: Erweiterungen
// ========================================================
// - Typ-Effektivität (z.B. Water > Fire)
// - Mehrere Attacken pro Pokémon
// - Items oder Status-Effekte
// - Animation oder Verzögerung bei der Ausgabe
// - Erweiterte ASCII-Sprites
