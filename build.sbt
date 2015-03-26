name := """NewsasWebApp"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.scalaz" %% "scalaz-core" % "7.0.6",
"org.typelevel" %% "scalaz-contrib-210"        % "0.2",
"org.typelevel" %% "scalaz-contrib-validation" % "0.2",
"org.typelevel" %% "scalaz-contrib-undo"       % "0.2",
// currently unavailable because there's no 2.11 build of Lift yet
// "org.typelevel" %% "scalaz-lift"               % "0.2",
"org.typelevel" %% "scalaz-nscala-time"        % "0.2",
"org.typelevel" %% "scalaz-spire"              % "0.2"
)
