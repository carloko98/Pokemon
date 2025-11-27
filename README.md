# Pokémon

[![Scala CI](https://github.com/CARLOKO98/Pokemon/actions/workflows/scala.yml/badge.svg)](https://github.com/CARLOKO98/Pokemon/actions/workflows/scala.yml)
[![Coverage Status](https://coveralls.io/repos/github/carloko98/Pokemon/badge.svg?branch=main)](https://coveralls.io/github/carloko98/Pokemon?branch=main)

Ein scala-basiertes Pokémon-Spiel...
## Features
- Vollständiges **MVC-Pattern**
```bash
Design Pattern:

    - StatePattern in controller/state/*
    - FactoryPattern in model/PokemonFactory
    - TemplatePattern in model/BattleLogic executeAttack ist das Template 
    - Strategy Pattern model/BattleLogic
    - Memento Patter model/GameMemento controller/Controller saveGame loadGame
    - Singleton Pattern PokemonDBS, PokemonFactory, WildBattleLogic enthalten keine Daten
```



## Ausführung
```bash
sbt run