// project/plugins.sbt is a file where SBT plugins are configured
// File name is just a convention, it may be any *.sbt file.
// In general, all the project/*.sbt files form the meta-build (build of the build) in exactly the same way
// as toplevel *.sbt files form the build of our primary project (fred)

resolvers += Resolver.url("jetbrains-bintray",
  url("http://dl.bintray.com/jetbrains/sbt-plugins/"))(Resolver.ivyStylePatterns)

// This plugin primarily lets us specify project's base package which will be picked up by IntelliJ.
// The IDE will then automatically use chained package declarations in newly created Scala files.
// The plugin is also useful for excluding projects and directories for the IDE.
addSbtPlugin("org.jetbrains" % "sbt-ide-settings" % "1.0.0")
// Uses Coursier for dependency resolution, which is much faster than the default Ivy.
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-RC13")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.25")
