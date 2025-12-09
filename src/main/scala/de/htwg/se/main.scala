package de.htwg.se

import de.htwg.se.controller.Controller
import de.htwg.se.view.Tui
import de.htwg.se.view.gui.Gui 
import de.htwg.se.model.PokemonFactory

object PokemonApp { 

  def main(args: Array[String]): Unit = {
    
    // 1. Model und Controller initialisieren
    val initialPlayer = PokemonFactory.createPlayer("Gast", Vector("Glurak", "Bisaflor"))
    val initialEnemy = PokemonFactory.createRandomEnemy()
    
    val controller = new Controller(initialPlayer, initialEnemy)

    // 2. TUI initialisieren und in einem separaten Thread starten
    val tui = new Tui(controller)
    
    val tuiThread = new Thread {
      override def run(): Unit = {
        tui.intro()
        tui.inputLoop()
        System.exit(0)
      }
    }
    // Daemon bedeutet: Wenn der Haupt-Thread (GUI) stirbt, stirbt auch dieser Thread
    tuiThread.setDaemon(true) 
    tuiThread.start()

    // 3. GUI initialisieren und starten
    // Die GUI blockiert den Main-Thread, deshalb am Ende
    val gui = new Gui(controller)
    gui.main(args) 
  }
}