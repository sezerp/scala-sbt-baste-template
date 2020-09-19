import sbtbuildinfo.BuildInfoKey.action
import sbtbuildinfo.BuildInfoKeys.{buildInfoKeys, buildInfoOptions, buildInfoPackage}
import sbtbuildinfo.{BuildInfoKey, BuildInfoOption}

import scala.util.Try

val configDependencies = Seq(
  "com.github.pureconfig" %% "pureconfig" % "0.12.2"
)

val unitTestingStack = Seq(
  "org.scalatest" %% "scalatest" % "3.1.1" % Test,
  "org.scalamock" %% "scalamock" % "4.4.0" % Test
)

val loggingDependencies = Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.codehaus.janino" % "janino" % "3.1.0",
  "de.siegmar" % "logback-gelf" % "2.2.0"
)

val commonDependencies = configDependencies ++ unitTestingStack ++ loggingDependencies

lazy val commonSettings = Seq(
  version := "0.1-SNAPSHOT",
  organization := "com.pawelzabczynski",
  scalaVersion := "2.13.3",
  libraryDependencies ++= commonDependencies,
)

lazy val buildInfoSettings = Seq(
  buildInfoKeys := Seq[BuildInfoKey](
    name,
    version,
    scalaVersion,
    sbtVersion,
    action("lastCommitHash") {
      import scala.sys.process._
      // if the build is done outside of a git repository, we still want it to succeed
      Try("git rev-parse HEAD".!!.trim).getOrElse("?")
    }
  ),
  buildInfoOptions += BuildInfoOption.BuildTime,
  buildInfoOptions += BuildInfoOption.ToJson,
  buildInfoOptions += BuildInfoOption.ToMap,
  buildInfoPackage := "com.pawelzabczynski.version",
  buildInfoObject := "BuildInfo"
)

lazy val fatJarSettings = Seq(
  assemblyJarName in assembly := "scala-base-template.jar",
  assemblyMergeStrategy in assembly := {
    x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)

lazy val root = (project in file("."))
  .enablePlugins(BuildInfoPlugin)
  .settings(commonSettings: _*)
  .settings(buildInfoSettings)
  .settings(fatJarSettings)
  .settings(
    mainClass in Compile := Some("com.pawelzabczynski.Main")
  )