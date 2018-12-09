name := "blog-apache-avro-handson"

organization := "com.github.binxio"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.8"

scalacOptions += "-Ypartial-unification"
scalacOptions += "-Ydelambdafy:inline"
scalacOptions += "-unchecked"
scalacOptions += "-deprecation"
scalacOptions += "-language:higherKinds"
scalacOptions += "-language:implicitConversions"
scalacOptions += "-feature"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.27"
libraryDependencies += "com.github.mpilquist" %% "simulacrum" % "0.14.0"
libraryDependencies += "com.sksamuel.avro4s" %% "avro4s-core" % "2.0.3"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.11"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test

// testing configuration
fork in Test := true
parallelExecution := false

// enable scala code formatting //
import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform

// Scalariform settings
SbtScalariform.autoImport.scalariformPreferences := SbtScalariform.autoImport.scalariformPreferences.value
   .setPreference(AlignSingleLineCaseStatements, true)
   .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
   .setPreference(DoubleIndentConstructorArguments, true)
   .setPreference(DanglingCloseParenthesis, Preserve)

// enable updating file headers //
organizationName := "Dennis Vriend, binx.io"
startYear := Some(2018)
licenses := Seq(("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt")))
headerMappings := headerMappings.value + (HeaderFileType.scala -> HeaderCommentStyle.CppStyleLineComment)

enablePlugins(AutomateHeaderPlugin, SbtScalariform)
