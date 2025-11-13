ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.7"

lazy val root = (project in file("."))
  .settings(
    name := "Pokemon",
   // idePackagePrefix := Some("de.htwg")
  )

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % Test


// alles, was nicht bewertet werden soll (z. B. Templates, Main)
coverageExcludedPackages := "de\\.htwg\\..*"
coverageExcludedFiles := ".*Main\\.scala"
