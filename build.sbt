import Dependencies._

import scala.io.Source
import scala.util.parsing.json.JSON

name := """bitbucket-scala-client"""

version := "1.9.0-SNAPSHOT"

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.6")

unmanagedSourceDirectories in Compile += {
  (scalaVersion.value, (sourceDirectory in Compile).value) match {
    case (v, dir) if v startsWith "2.11" => dir / "scala-2.11"
    case (v, dir) if v startsWith "2.12" => dir / "scala-2.12"
  }
}

scalacOptions := Seq("-deprecation", "-feature", "-unchecked", "-Ywarn-adapted-args", "-Xlint")

resolvers += "Typesafe maven repository" at "http://repo.typesafe.com/typesafe/maven-releases/"

libraryDependencies ++= Seq(
  Dependencies.playWS,
  Dependencies.playJson(scalaVersion.value),
  Dependencies.scalaTest
)

mimaPreviousArtifacts := {
  val latestVersion = JSON.parseFull(
    Source.fromURL("https://api.github.com/repos/codacy/bitbucket-scala-client/releases/latest").mkString
  ).flatMap(_.asInstanceOf[Map[String, String]].get("tag_name")).getOrElse("5.0.0")
  Set("com.codacy" %% "bitbucket-scala-client" % latestVersion)
}
mimaBinaryIssueFilters ++= ignoredABIProblems
val ignoredABIProblems = {
  import com.typesafe.tools.mima.core._
  import com.typesafe.tools.mima.core.ProblemFilters._
  Seq()
}

organization := "com.codacy"

organizationName := "Codacy"

organizationHomepage := Some(new URL("https://www.codacy.com"))

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

publishTo := sonatypePublishTo.value

startYear := Some(2014)

description := "Bitbucket Scala Client"

licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("https://github.com/codacy/bitbucket-scala-client.git"))

pomExtra :=
  <scm>
    <url>https://github.com/codacy/bitbucket-scala-client.git</url>
    <connection>scm:git:git@github.com:codacy/bitbucket-scala-client.git</connection>
    <developerConnection>scm:git:https://github.com/codacy/bitbucket-scala-client.git</developerConnection>
  </scm>
    <developers>
      <developer>
        <id>rtfpessoa</id>
        <name>Rodrigo</name>
        <email>rodrigo [at] codacy.com</email>
        <url>https://github.com/rtfpessoa</url>
      </developer>
    </developers>
