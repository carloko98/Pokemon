val scala3Version = "3.3.7"

lazy val root = (project in file("."))
  .settings(
    name := "Pokemon",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    // Bibliotheken hier drinnen definieren
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % Test,
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.1.0",

    // --- COVERAGE EINSTELLUNGEN ---
   
    coverageExcludedPackages := ".*view.*",
    coverageExcludedFiles := "(?i).*main.scala"
  )