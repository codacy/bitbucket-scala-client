import codacy.libs._

import scala.io.Source
import scala.util.parsing.json.JSON

name := """bitbucket-scala-client"""

scalaVersion := "2.11.12"

scalacOptions := Seq("-deprecation",
                     "-feature",
                     "-unchecked",
                     "-Ywarn-adapted-args",
                     "-Xlint")

resolvers += "Typesafe maven repository" at "http://repo.typesafe.com/typesafe/maven-releases/"

libraryDependencies ++= Seq(
  playWs,
  playJson,
  scalatest % Test
)

mimaPreviousArtifacts := {
  val latestVersion = JSON
    .parseFull(
      Source
        .fromURL(
          "https://api.github.com/repos/codacy/bitbucket-scala-client/releases/latest")
        .mkString
    )
    .flatMap(_.asInstanceOf[Map[String, String]].get("tag_name"))
    .getOrElse("5.0.0")
  Set("com.codacy" %% "bitbucket-scala-client" % latestVersion)
}
mimaBinaryIssueFilters ++= ignoredABIProblems
val ignoredABIProblems = {
  import com.typesafe.tools.mima.core._
  import com.typesafe.tools.mima.core.ProblemFilters._
  Seq()
}

organizationName := "Codacy"

organizationHomepage := Some(new URL("https://www.codacy.com"))

startYear := Some(2014)

description := "Bitbucket Scala Client"

homepage := Some(url("https://github.com/codacy/bitbucket-scala-client.git"))

scmInfo := Some(
  ScmInfo(url("https://github.com/codacy/bitbucket-scala-client.git"),
          "scm:git:git@github.com:codacy/bitbucket-scala-client.git")
)

pgpPassphrase := Option(System.getenv("SONATYPE_GPG_PASSPHRASE"))
  .map(_.toCharArray)

resolvers ~= { _.filterNot(_.name.toLowerCase.contains("codacy")) }

publicMvnPublish
