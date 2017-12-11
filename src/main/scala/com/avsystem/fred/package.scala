package com.avsystem

import com.avsystem.commons.SharedExtensions
import com.avsystem.commons.collection.CollectionAliases
import com.avsystem.commons.jiop.JavaInterop

// Package object lets us put values, type aliases, methods, implicits, etc. directly into package namespace.
// Normally these things can only be inside classes. In this case our package object inherits some stuff from
// traits defined in the commons library. Thanks to the fact that we configured `ideBasePackages` in build.sbt,
// the `com.avsystem.fred` package is recognized by IDE as "base package" - when creating a Scala file, IntelliJ
// will use chained package declaration which will effectively "import" everything from the `fred` package.
// So, in other words - using project-global package object for package configured as base package is a way to make
// some stuff visible globally in the entire project.
package object fred
  extends SharedExtensions with CollectionAliases with JavaInterop
