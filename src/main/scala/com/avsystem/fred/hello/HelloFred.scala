package com.avsystem.fred
package hello

import java.util.concurrent.locks.{Lock, ReentrantLock}

object HelloFred {
  sealed trait Optjon[+A]
  case class Jest[+A](jest: A) extends Optjon[A]
  case object Niema extends Optjon[Nothing]

  // dla każdego A, Nothing <: A
  // dla każdego A, Niema.type <: Optjon[A]

  val x: Optjon[Any] = Jest[String]("fuu")

  def lul(str: String) = str

  def fail: Nothing =
    throw new Exception("jklajsdkljfkljaksldjfkla")

  def gimmeUnit(): Unit = ()

  def anycos(any: Any): String = any.toString

  def wypisz(list: JList[Any]): Unit =
    Zuo.wypisz(list.asInstanceOf[JList[AnyRef]])

  class Cos {
    def ++++(cosinnego: Cos): Cos = ???
    def ---(cosinnego: Cos): Cos = ???
    def **(cosinnego: Cos): Cos = ???
    def add(cosinnego: Cos): Cos = ???
    def +=(cosinnego: Cos): Unit = ???
  }

  val itos: Int => String = { x =>
    println("dużo kodu")
    x.toString
  }

  val stod: String => Double = _.toDouble

  trait Number {
    def plus(n: Number): Number
    def times(n: Number): Number
  }

  val f0s: () => String = () => "lol"
  val f0a: () => Any = f0s

  val itod: Int => Double = stod compose itos
  val itod2: Int => Double = itos andThen stod

  val atos: Any => String = _.toString
  val itos2: Int => Any = atos

  def mapIntListToStringList[A](l: List[A])(f: A => String): List[String] =
    l.map(f)

  mapIntListToStringList(List(1, 2, 3))(_.toString)

  def underLock[T](lock: Lock)(expr: => T): T = {
    lock.lock()
    try expr finally {
      expr
      lock.unlock()
    }
  }

  // the method is parameterized (generic) with arbitrary type T
  def ifelse[T](condition: Boolean, whenTrue: => T, whenFalse: => T = ()): T =
    if (condition) whenTrue else whenFalse

  val costam: Any = ifelse(true, "costam")

  lazy val msg: String = underLock(new ReentrantLock) {
    if (math.random() < 0.0000001) "OK"
    else throw new Exception("nie da sie")
  }

  def ileśrazy(ilerazy: Int)(kod: => Unit): Unit =
    (0 until ilerazy).foreach(_ => kod)

  class Kąponęt(timestamp: () => Long) {

  }

  new Kąponęt(() => System.currentTimeMillis())

  def main(args: Array[String]): Unit = {
    var a, b, c, d = new Cos

    val `()`: () => Unit = () => ()
    `()`()

    ileśrazy(42) {
      println(42)
    }
  }
}
