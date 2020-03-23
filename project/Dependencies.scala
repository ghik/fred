import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._

object Dependencies {
  // It's good to extract dependency version numbers to named values.
  val silencerVersion = "1.6.0"
  val avsCommonsVersion = "1.43.0"
  val udashRestVersion = "0.8.0-RC3"
  val monixVersion = "3.1.0"
  val typesafeConfigVersion = "1.3.3"
  val collectionCompatVersion = "2.1.2"

  // List of dependencies containing cross dependencies (for both JVM and JS, using %%% syntax) must be wrapped
  // into `Def.setting` because %%% can't be expanded until it's known whether the project is a JVM or JS project.
  val freDeps = Def.setting(Seq(
    "com.github.ghik" % "silencer-lib" % silencerVersion cross CrossVersion.full,
    // the invented-here commons library
    "com.avsystem.commons" %%% "commons-core" % avsCommonsVersion,
    "io.udash" %%% "udash-rest" % udashRestVersion,
    "io.monix" %%% "monix" % monixVersion,
    "org.scala-lang.modules" %% "scala-collection-compat" % collectionCompatVersion,
    // compiler plugin for selective warning suppression, which Scala doesn't have by default
    compilerPlugin("com.github.ghik" % "silencer-plugin" % silencerVersion cross CrossVersion.full),
    // the invented-here static code analyzer with some random rules like rejecting `import java.util`
    compilerPlugin("com.avsystem.commons" %% "commons-analyzer" % avsCommonsVersion),
    "org.scalatest" %%% "scalatest" % "3.0.4" % Test,
  ))
  
  val freJvmDeps = Seq(
    "com.typesafe" % "config" % typesafeConfigVersion,
  )
}
