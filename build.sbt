organization := "fnumatic.de"

name := "codekata-csvviewer"

version := "0.1-SNAPSHOT"

seq(lsSettings :_*)

libraryDependencies += "cc.co.scala-reactive" %% "reactive-core" % "0.1"

// test Libraries
libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"

libraryDependencies += "org.mockito" % "mockito-core" % "1.8.5" % "test"

libraryDependencies += "junit" % "junit" % "4.8" % "test"
