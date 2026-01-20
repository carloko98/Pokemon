package de.htwg.se.view.gui

import scalafx.scene.layout.{BorderPane, VBox, HBox, GridPane, Priority, Region} 
import scalafx.scene.control.{Button, Label, ProgressBar}
import scalafx.geometry.{Pos, Insets}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.Cursor // Wichtig für den Mauszeiger
import de.htwg.se.controller.IController
import de.htwg.se.controller.ViewState
import de.htwg.se.controller.ViewState._ 

class BattleScene(controller: IController) extends BorderPane {

  val playerPoke = controller.getPlayerPokemon
  val enemyPoke = controller.getEnemyPokemon
  
  style = "-fx-background-color: #2b2b2b;" 

  // --- HIER IST DIE MAGIE ---
  // Wenn der Gegner dran ist, machen wir das ganze Fenster klickbar!
  if (controller.viewState == VEnemyAtk) {
    cursor = Cursor.Hand // Zeigt an: Du kannst klicken
    onMouseClicked = _ => controller.handleInput("") // Klick irgendwohin -> Weiter
  }
  // --------------------------

  def createPokemonImage(url: String): ImageView = {
    val image = if (url != null && url.nonEmpty) new Image(url, true) else null
    new ImageView(image) {
      fitWidth = 250
      fitHeight = 250
      preserveRatio = true
    }
  }

  val enemyInfo = new VBox {
    alignment = Pos.CenterRight
    padding = Insets(20)
    spacing = 5
    children = Seq(
      new Label(s"${enemyPoke.name}") { style = "-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;" },
      new HBox {
        alignment = Pos.CenterRight
        spacing = 10
        children = Seq(
          new Label("HP:") { style = "-fx-text-fill: white;" },
          new ProgressBar {
            progress = enemyPoke.currentHp.toDouble / enemyPoke.maxHp
            style = "-fx-accent: #ff4444;" 
            prefWidth = 200
          }
        )
      },
      new Label(s"${enemyPoke.currentHp} / ${enemyPoke.maxHp}") { style = "-fx-text-fill: #aaaaaa;" },
      
      createPokemonImage(enemyPoke.spriteUrl)
    )
  }

  val playerInfo = new VBox {
    alignment = Pos.CenterLeft
    padding = Insets(20)
    spacing = 5
    children = Seq(
      createPokemonImage(playerPoke.spriteUrl),
      
      new Label(s"${playerPoke.name}") { style = "-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;" },
      new HBox {
        alignment = Pos.CenterLeft
        spacing = 10
        children = Seq(
          new Label("HP:") { style = "-fx-text-fill: white;" },
          new ProgressBar {
            progress = playerPoke.currentHp.toDouble / playerPoke.maxHp
            style = "-fx-accent: #44ff44;" 
            prefWidth = 200
          }
        )
      },
      new Label(s"${playerPoke.currentHp} / ${playerPoke.maxHp}") { style = "-fx-text-fill: #aaaaaa;" }
    )
  }

  val actionBox = new VBox {
    padding = Insets(20)
    spacing = 10
    style = "-fx-background-color: #444444; -fx-border-color: #666666; -fx-border-width: 2px 0 0 0;"
    prefHeight = 150

    val (m1, m2) = controller.getMessage
    children += new Label(s"$m1\n$m2") {
      style = "-fx-text-fill: yellow; -fx-font-size: 16px; -fx-font-weight: bold;"
    }

    val controls = controller.viewState match {
      case VPlayerAtk => createPlayerActions()
      case VEnemyAtk  => createWaitLabel() // <--- Geändert zu Label statt Button
      case _          => new Label("")
    }
    children += controls
  }

  // Layout zusammenbauen
  top = enemyInfo
  center = playerInfo
  bottom = actionBox

  def createPlayerActions(): BorderPane = {
    
    val layout = new BorderPane {
      padding = Insets(0, 20, 0, 20)
    }

    val attackGrid = new GridPane {
      hgap = 10 
      vgap = 10 
      alignment = Pos.Center 
    }

    playerPoke.attacks.zipWithIndex.foreach { case (attack, index) =>
      val btn = new Button(s"${attack.name}\n(Dmg: ${attack.damage})") {
        style = "-fx-base: #555555; -fx-text-fill: white; -fx-font-size: 13px; -fx-background-radius: 8;"
        minWidth = 200       
        prefWidth = 200      
        minHeight = 60      
        textAlignment = scalafx.scene.text.TextAlignment.Center
        wrapText = true      
        onAction = _ => controller.handleInput((index + 1).toString) 
      }
      
      val col = index % 2
      val row = index / 2
      attackGrid.add(btn, col, row)
    }
    
    layout.center = attackGrid

    val sideButtons = new VBox {
      spacing = 15
      alignment = Pos.CenterRight 
      padding = Insets(0, 0, 0, 40) 
      
      children = Seq(
        new Button("Wechseln") {
           style = "-fx-base: #aaaa44; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;"
           minWidth = 120
           prefHeight = 45
           onAction = _ => controller.handleInput("w")
        },
        new Button("Fliehen") {
          style = "-fx-base: #aa3333; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;"
          minWidth = 120
          prefHeight = 45
          onAction = _ => controller.handleInput("f")
        }
      )
    }

    layout.right = sideButtons
    layout
  }

  // --- NEU: Nur noch ein Label, kein Button mehr ---
  def createWaitLabel(): Label = {
    new Label(">> Klicke irgendwo für Gegnerzug... <<") {
      style = "-fx-font-size: 16px; -fx-text-fill: #cccccc; -fx-font-weight: bold;"
      maxWidth = Double.MaxValue
      alignment = Pos.Center
    }
  }
}