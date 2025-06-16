addSbtPlugin("com.typesafe" % "sbt-mima-plugin" % "0.2.0")

addSbtPlugin("com.codacy" % "codacy-sbt-plugin" % "25.2.4")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.3.0")

ThisBuild / libraryDependencySchemes +=
  "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
