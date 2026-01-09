package de.htwg.se

import de.htwg.se.controller.controllerImpl.Controller
import de.htwg.se.controller.ControllerInterface
import de.htwg.se.view.Tui
import de.htwg.se.view.gui.Gui 
import de.htwg.se.model.PokemonComponent.PokemonImpl.PokemonFactory

object PokemonApp { 

  def main(args: Array[String]): Unit = {
    
    // 1. Model und Controller initialisieren
    val initialPlayer = PokemonFactory.createPlayer("Gast", Vector("Glurak", "Bisaflor"))
    val initialEnemy = PokemonFactory.createRandomEnemy()
    
    val controller : ControllerInterface = new Controller(initialPlayer, initialEnemy)

    // 2. TUI initialisieren und in einem separaten Thread starten
    val tui = new Tui(controller)
    
    val tuiThread = new Thread {
      override def run(): Unit = {
        tui.intro()
        tui.inputLoop()
        System.exit(0)
      }
    }
    // Wenn der Haupt-Thread (GUI) stirbt, stirbt auch dieser Thread
    tuiThread.setDaemon(true) 
    tuiThread.start()

    // 3. GUI initialisieren und starten
    // Die GUI blockiert den Main-Thread
    val gui = new Gui(controller)
    gui.main(args) 
  }
}

// nicht controllerImpl implementiereb nur import de.htwg.se.controller