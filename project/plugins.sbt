addSbtPlugin("com.typesafe" % "sbt-mima-plugin" % "0.2.0")

addSbtPlugin("com.codacy" % "codacy-sbt-plugin" % "22.0.2")

ThisBuild / libraryDependencySchemes +=
  "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
