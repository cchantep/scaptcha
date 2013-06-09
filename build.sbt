organization := "scaptcha"

name := "scaptcha"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.0"

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

scalacOptions += "-feature"

resolvers += "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.0.0",
  "org.specs2" %% "specs2" % "1.14" % "test",
  "commons-io" % "commons-io" % "2.4" % "test")

publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))
