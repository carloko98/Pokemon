## Features
- Vollständiges **MVC-Pattern**
- **Immutable Model** (`Pokemon.withHp`)
- **Komplette Typ-Effektivitäts-Matrix** (Gen 9)
- **TUI** mit HP-Balken, Rahmen, dynamischem Menü
- **Observer-Pattern** (Controller → TUI)
- **100 % Testabdeckung** (Scoverage)

## Tests
- `PokemonSpec`: Immutabilität, HP-Clamping
- `PokemonTypeSpec`: Alle 324 Typ-Kombinationen
- `ControllerImplSpec`: Angriff, Flucht, Sieg/Niederlage
- `TuiSpec`: Rendering, HP-Balken

## Coverage
![Scoverage 100%](docs/coverage.png)

## Ausführung
```bash
sbt run