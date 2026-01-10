package de.htwg.se

import com.google.inject.Guice
import de.htwg.se.view.Tui
import de.htwg.se.view.gui.Gui
import de.htwg.se.controller.IController

object PokemonApp { 

  def main(args: Array[String]): Unit = {
    
    // 1. Dependency Injection initialisieren
    val injector = Guice.createInjector(new PokemonModule)
    
    // Controller vom Injector holen (Dieser wird durch die 'provideController' Methode im Modul gebaut)
    val controller = injector.getInstance(classOf[IController])

    // 2. TUI initialisieren
    val tui = new Tui(controller)
    val tuiThread = new Thread {
      override def run(): Unit = {
        tui.intro()
        tui.inputLoop()
        System.exit(0)
      }
    }
    tuiThread.setDaemon(true) 
    tuiThread.start()

    // 3. GUI initialisieren
    val gui = new Gui(controller)
    gui.main(args) 
  }
}