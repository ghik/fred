package com.avsystem.fred
package methods

object Methods {
  sealed trait MyList[+A] {
    def foldLeft[B](z: B)(f: (B, A) => B): B = this match {
      case MyNil => z
      case MyCons(head, tail) => tail.foldLeft(f(z, head))(f)
    }

    def toReverseScalaList: List[A] =
      foldLeft[List[A]](Nil)((tail, head) => head :: tail)
  }
  case object MyNil extends MyList[Nothing]
  case class MyCons[+A](head: A, tail: MyList[A]) extends MyList[A]

  object MyList {
    def apply[A](as: A*): MyList[A] =
      as.foldRight(MyNil: MyList[A])(MyCons(_, _))
  }

  def joinNonEmpty(sep: String)(lhs: String, rhs: String): String =
    if (lhs.nonEmpty && rhs.nonEmpty) lhs + sep + rhs
    else if (lhs.nonEmpty) lhs
    else rhs

  def zrób(int: Int, @deprecatedName('str) string: String): Unit = ()
  def oblicz: Int = 42

  def main(args: Array[String]): Unit = {
    val methods: Methods = new Methods()
    println(methods.metoda()())
  }
}
class Methods(costam: String = "") {
  def defaultString: String = ""

  // $lessinit$greater$default$1

  def metoda(x: Int = 1, y: Int = 2)(z: Int = x + y): String = s"$x-$y-$z"
}

case class Event(data: Int, timestamp: JDate)
