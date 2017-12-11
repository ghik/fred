import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

object Dependencies {
  // It's good to extract dependency version numbers to named values.
  val silencerVersion = "0.5"
  val avsCommonsVersion = "1.25.3"

  // List of dependencies containing cross dependencies (for both JVM and JS, using %%% syntax) must be wrapped
  // into `Def.setting` because %%% can't be expanded until it's known whether the project is a JVM or JS project.
  val freDeps = Def.setting(Seq(
    "com.github.ghik" %% "silencer-lib" % silencerVersion,
    // the invented-here commons library
    "com.avsystem.commons" %%% "commons-core" % avsCommonsVersion,
    // compiler plugin for selective warning suppression, which Scala doesn't have by default
    compilerPlugin("com.github.ghik" %% "silencer-plugin" % silencerVersion),
    // the invented-here static code analyzer with some random rules like rejecting `import java.util`
    compilerPlugin("com.avsystem.commons" %% "commons-analyzer" % avsCommonsVersion),
    "org.scalatest" %%% "scalatest" % "3.0.4" % Test,
  ))
}