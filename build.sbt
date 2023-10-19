name := """bitbucket-scala-client"""

val scala212 = "2.12.10"

val play27 = "2.7.4"
val play28 = "2.8.2"

lazy val playJsonVersion = settingKey[String]("The version of play-json used for building.")
ThisBuild / playJsonVersion := play27

scalaVersion := scala212

scalacOptions := Seq("-deprecation", "-feature", "-unchecked", "-Ywarn-adapted-args", "-Xlint")

resolvers +=
  "Typesafe maven repository" at "https://repo.typesafe.com/typesafe/maven-releases/"

libraryDependencies ++= Dependencies.playJson(playJsonVersion.value) ++ Seq(
  "org.scalatest" %% "scalatest" % "3.0.8" % Test
)

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
  * on the entire matrix of play and scala versions.
  * If the command has `:` in it (like test:compile)
  * the alias becomes crossTestCompile instead of crossTest:compile
  * (which is not an allowed sbt alias name)
  */
def addCrossAlias(command: String) = {
  val matrix = Seq(scala212 -> Seq(play27, play28))

  addCommandAlias(
    s"cross${command.split(':').map(_.capitalize).mkString}",
    matrix
      .flatMap {
        case (scalaV, playVersions) =>
          s"""set ThisBuild / scalaVersion := "$scalaV"""" +: playVersions
            .flatMap(playV => Seq(s"""set ThisBuild / playJsonVersion := "$playV"""", command))
      }
      .mkString(";")
  )
}

// List of crossX aliases.
// Add a command here if you want to call it for
// the entire playVersion / scalaVersion matrix
addCrossAlias("update")
addCrossAlias("compile")
addCrossAlias("test:compile")
addCrossAlias("test")
addCrossAlias("publishSigned")
