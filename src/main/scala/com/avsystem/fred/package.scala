package com.avsystem

import com.avsystem.commons.collection.CollectionAliases
import com.avsystem.commons.jiop.JavaInterop
import com.avsystem.commons.{CommonAliases, SharedExtensions}

// Package object lets us put values, type aliases, methods, implicits, etc. directly into package namespace.
// Normally these things can only be inside classes or objects. In this case our package object inherits some stuff from
// traits defined in the commons library. Thanks to the fact that we configured `ideBasePackages` in build.sbt,
// the `com.avsystem.fred` package is recognized by IDE as "base package" - when creating a Scala file, IntelliJ
// will use chained package declaration which will effectively "import" everything from the `fred` package.
// So, in other words - using project-global package object for package configured as base package is a way to make
// some stuff visible globally in the entire project.
package object fred
  extends SharedExtensions with CommonAliases with CollectionAliases with JavaInterop {

  type Callback[T] = Try[T] => Unit
  type Async[T] = Callback[T] => Unit

  def successfulAsync[A](value: A): Async[A] =
    readyAsync(Success(value))

  def failedAsync[A](cause: Throwable): Async[A] =
    readyAsync(Failure(cause))

  def readyAsync[A](result: Try[A]): Async[A] =
    callback => callback(result)

  implicit class asyncOps[A](async: Async[A]) {
    def withFilter(p: A => Boolean): Async[A] =
      callback => async(res => callback(res.filter(p)))

    def map[B](f: A => B): Async[B] =
      callback => async(tb => callback(tb.map(f)))

    def flatMap[B](f: A => Async[B]): Async[B] =
      callback => async(ta => ta.fold(failedAsync, f).apply(callback))
  }
}
