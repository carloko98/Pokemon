val scala3Version = "3.3.7"

// Verbesserte OS-Erkennung (inkl. Apple Silicon Support)
val osName: String = {
  val n = System.getProperty("os.name").toLowerCase
  val a = System.getProperty("os.arch").toLowerCase
  if (n.contains("linux")) "linux"
  else if (n.contains("mac")) {
    // Prüfen ob Apple Silicon (M1/M2...) oder Intel
    if (a == "aarch64") "mac-aarch64" else "mac"
  }
  else if (n.contains("windows")) "win"
  else throw new Exception("Unknown OS")
}


val javaFXVersion = "20"

lazy val root = (project in file("."))
  .settings(
    name := "Pokemon",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    // Wichtig damit JavaFX Fenster sauber starten
    fork := true,

    // Testing & XML
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % Test,
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.1.0",

    // ScalaFX & JavaFX
    libraryDependencies ++= Seq(
      "org.scalafx" %% "scalafx" % "20.0.0-R31",
      "org.openjfx" % "javafx-base" % javaFXVersion classifier osName,
      "org.openjfx" % "javafx-controls" % javaFXVersion classifier osName,
      "org.openjfx" % "javafx-fxml" % javaFXVersion classifier osName,
      "org.openjfx" % "javafx-graphics" % javaFXVersion classifier osName,
      "org.openjfx" % "javafx-media" % javaFXVersion classifier osName,
      "org.openjfx" % "javafx-web" % javaFXVersion classifier osName
    ),

    // Coverage Einstellungen
  coverageExcludedFiles := "(?i).*main;.*Tui;.*Gui;.*Scene"
  )