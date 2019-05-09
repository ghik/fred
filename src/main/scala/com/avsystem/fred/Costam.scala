package com.avsystem.fred

object Costam {
  final val Name = "name"

  def main(args: Array[String]): Unit = {
    Costam("ab") plus Costam("wincyj")

    val map = new MHashMap[String, Int]
    map += (("jeden", 1))
  }
}

case class Costam(private val name: String) {
  def this(int: Int) = this(int.toString)

  def +(arg: Costam): Costam = Costam(name + arg.name)

  def plus(arg: Costam): Costam = this + arg
}
