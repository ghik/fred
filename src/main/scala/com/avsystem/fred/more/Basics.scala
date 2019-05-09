package com.avsystem.fred
package more

object Basics {
  def main(args: Array[String]): Unit = {
    if (args.nonEmpty) {
      for (arg <- args) {
        println(arg)
      }
    }

    val x = 5
    val y = "cos"
    s"lol $x moar $y wincyj"
    StringContext("lol ", " moar ", " wincyj").s(x, y)

    val l = 1 :: 2 :: 3 :: Nil
    val array = Array(1, 2, 3)
    array(0) = 5
    array.update(0, 5)
  }
}
