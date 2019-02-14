package com.avsystem.fred
package hello

object StringColl {
  def foreach(consumer: String => Unit): Int = {
    consumer("jksldj")
    consumer("jksldjkl")
    2
  }
}

object Mejn {
  def main(args: Array[String]): Unit = {
    val count = for(s <- StringColl) {
      println(s)
    }
    println(count)
  }
}
