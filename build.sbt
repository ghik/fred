import Dependencies._
import org.scalajs.jsenv.nodejs.NodeJSEnv

// inThisBuild essentially means "for all projects" - an unqualified setting would by default apply
// only to root project
inThisBuild(Seq(
  organization := "com.avsystem",
  version := "1.0-SNAPSHOT",
  scalaVersion := "2.12.4",
  // Mixed compile order makes it so that we can have both Java and Scala in the same module AND that they can
  // freely depend on each other, in both directions, i.e. there may be even circular dependencies between Scala
  // and Java files.
  compileOrder := CompileOrder.Mixed,
  scalacOptions ++= Seq(
    "-encoding", "utf-8",
    "-explaintypes",
    "-feature", // turns on language feature errors (for features NOT enabled with `-language:X` option)
    "-deprecation", // turns on individual deprecation warnings (by default there's only one general warning - wtf?)
    "-unchecked", // turns on individual unchecked warnings (e.g. unchecked casts)
    "-language:implicitConversions",
    "-language:existentials",
    "-language:dynamics",
    "-language:experimental.macros",
    "-language:higherKinds",
    "-Xfuture", // primarily turns off language features scheduled for removal in the future, e.g. procedure syntax
    "-Xfatal-warnings", // enforce no warning policy (but warnings can be suppressed using silencer)
    "-Xlint:-missing-interpolator,-adapted-args,-unused,_" // additional warnings
  ),
  // The base package option for IDE. IntelliJ will automatically create chained package declarations in newly
  // created Scala files. See `fred` package object for more details.
  ideBasePackages := Seq("com.avsystem.fred"),
))

// Root project doesn't have to be explicitly declared (it will be created by SBT anyway) but declaring it
// lets us configure it more.
lazy val fred = project.in(file("."))
  // Aggregation makes it so that invoking a task on root project also invokes it on all aggregated projects.
  .aggregate(fredLocal)
  .settings(
    // I need to do `.value` because `freDeps` contains cross-dependencies specified with %%% and must be
    // a `Def.Initialize[Seq[ModuleID]]` instead of just `Seq[ModuleID]`
    // I also can't do it inThisBuild because it must be known if this is a JVM or JS project
    libraryDependencies ++= freDeps.value,
    name := "fred"
  )

// A separate subproject declaration
lazy val fredLocal = project.enablePlugins(ScalaJSPlugin)
  .settings(
    libraryDependencies ++= freDeps.value,
  )
