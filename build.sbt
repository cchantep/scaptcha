organization := "scaptcha"

name := "scaptcha"

version := "1.1.2"

scalaVersion := "2.11.1"

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

scalacOptions += "-feature"

resolvers += "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.1.0-M7",
  "org.specs2" %% "specs2" % "2.3.12-scalaz-7.1.0-M7",
  "commons-io" % "commons-io" % "2.4" % "test")

publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))
