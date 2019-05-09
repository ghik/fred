package com.avsystem.fred
package traits

trait Service {
  def handle(arg: String): Unit
}

trait Printing extends Service {
  def handle(arg: String): Unit = println(arg)
}

trait TimeMeasuring extends Service {
//  println("trait initialization")

//  val fourtyTwo: Double = math.sqrt(42)
//  lazy val stuff: String = "stuff".toUpperCase

//  var mutableness: Double = 3.14
//
//  private[this] val privateThis = "privateThis"
//  private val privateVal = "privateVal"
//
  private def now(): Long = System.nanoTime()

  abstract override def handle(arg: String): Unit = {
    val start = now()
    try super.handle(arg) finally {
      val duration = now() - start
      println(s"Took $duration nanoseconds")
    }
  }
}

trait Logging extends Service {
  abstract override def handle(arg: String): Unit = {
    println(s"EXECUTING HANDLE WITH $arg")
    super.handle(arg)
  }
}

class SuperService extends Service
  with Printing
  with TimeMeasuring
