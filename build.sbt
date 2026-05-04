name := """bitbucket-scala-client"""

val scala213 = "2.13.18"

val playJson28 = "2.8.2"
val playJson210 = "2.10.8"

lazy val playJsonVersion = settingKey[String]("The version of play-json used for building.")
ThisBuild / playJsonVersion := playJson28

ThisBuild / scalaVersion := scala213

scalacOptions := Seq("-deprecation", "-feature", "-unchecked", "-Xlint")

resolvers +=
  "Typesafe maven repository" at "https://repo.typesafe.com/typesafe/maven-releases/"

libraryDependencies ++= Dependencies.playJson(playJsonVersion.value) ++ Seq(
  "org.scalatest" %% "scalatest" % "3.0.8" % Test,
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4"
)

ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-java8-compat" % VersionScheme.Always

organizationName := "Codacy"

organizationHomepage := Some(new URL("https://www.codacy.com"))

startYear := Some(2014)

description := "Bitbucket Scala Client"

homepage := Some(url("https://github.com/codacy/bitbucket-scala-client.git"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/codacy/bitbucket-scala-client.git"),
    "scm:git:git@github.com:codacy/bitbucket-scala-client.git"
  )
)

pgpPassphrase := Option(System.getenv("SONATYPE_GPG_PASSPHRASE"))
  .map(_.toCharArray)

name := s"${name.value}_playjson${playJsonVersion.value.split('.').take(2).mkString}"

/**
  * Given a command it creates an alias to run the command
  * across all supported play-json versions.
  * If the command has `:` in it (like test:compile)
  * the alias becomes crossTestCompile instead of crossTest:compile
  * (which is not an allowed sbt alias name)
  */
def addCrossAlias(command: String) = {
  val playVersions = Seq(playJson28, playJson210)

  addCommandAlias(
    s"cross${command.split(':').map(_.capitalize).mkString}",
    playVersions
      .flatMap(playV => Seq(s"""set ThisBuild / playJsonVersion := "$playV"""", command))
      .mkString(";")
  )
}

// List of crossX aliases.
// Add a command here if you want to call it for
// every supported play-json version
addCrossAlias("update")
addCrossAlias("compile")
addCrossAlias("test:compile")
addCrossAlias("test")
addCrossAlias("publish")
addCrossAlias("publishLocal")
addCrossAlias("publishSigned")
