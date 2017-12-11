
// It's good to extract dependency version numbers to named values. When there is a lot of dependencies, it's also
// good to create a separate Scala file in project subdirectory (usually project/Dependencies.scala) for listing
// all the dependencies.
val silencerVersion = "0.5"
val avsCommonsVersion = "1.25.3"

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
  libraryDependencies ++= Seq(
    "com.github.ghik" %% "silencer-lib" % silencerVersion,
    // the invented-here commons library
    "com.avsystem.commons" %% "commons-core" % avsCommonsVersion excludeAll "com.google.code.findbugs",
    // compiler plugin for selective warning suppression, which Scala doesn't have by default
    compilerPlugin("com.github.ghik" %% "silencer-plugin" % silencerVersion),
    // the invented-here static code analyzer with some random rules like rejecting `import java.util`
    compilerPlugin("com.avsystem.commons" %% "commons-analyzer" % avsCommonsVersion),
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
    name := "fred"
  )

// A separate subproject declaration
lazy val fredLocal = project
