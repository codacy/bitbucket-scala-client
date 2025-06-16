addSbtPlugin("com.typesafe" % "sbt-mima-plugin" % "0.2.0")

addSbtPlugin("com.codacy" % "codacy-sbt-plugin" % "25.2.4")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.1")

ThisBuild / libraryDependencySchemes +=
  "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
