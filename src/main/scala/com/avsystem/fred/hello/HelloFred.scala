package com.avsystem.fred
package hello

import java.util.concurrent.locks.{Lock, ReentrantLock}

import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

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

  trait MyClock {
    def currentTimeMillis(): Long
  }

  class Kąponęt(clock: MyClock) {

  }

  new Kąponęt(() => System.currentTimeMillis())

  def main(args: Array[String]): Unit = {
    var a, b, c, d = new Cos

    val `()`: () => Unit = () => ()
    `()`()

    ileśrazy(1) {
      println(42)
    }

    val Pierdyliard = 1
    val lis = List.fill(Pierdyliard)(Pierdyliard)

    var suma = 0
    for (el <- lis) {
      suma += el
    }

    def wincyj(int: Int): Opt[Int] = ???

    @tailrec def loop(int: Int = 0): Unit = {
      println(int)
      wincyj(int) match {
        case Opt(newInt) => loop(newInt)
        case Opt.Empty =>
      }
    }

    case class Address(city: String, street: String, kod: String)

    def przerób(serialNumber: String): String = {
      val subSerial = serialNumber.stripPrefix("costam") |> { s =>
        s.substring(s.length - 14, s.length - 4)
      }
      subSerial
    }

    case class Age(int: Int)
    object Age {
      def apply(int: Int): String = int.toString
    }
    List(1, 2, 3).map(Age(_))

    case class Person(name: String, surname: String,
      address: Address, madSkillz: List[String], age: Int = 0)
    object Person {
      def unapply(arg: Person): Some[(String, String, Address, List[String])] =
        Some((arg.name, arg.surname, arg.address, arg.madSkillz))
    }

    val fred = Person("Fred", "freddie",
      Address("krk", "ra", "31-315"),
      List("znajomość SV")
    ) |> (p => p.copy(address = p.address.copy(street = "Radzikowskiego")))

    val l = List(fred, fred.address)

    class CzyParzy(x: Int) {
      def isEmpty: Boolean = x % 2 != 0
      def get: CzyParzy = this
      def _1: Int = x / 2
      def _2: Boolean = x > 0
    }

    object Parzy {
      def unapply(int: Int): CzyParzy = new CzyParzy(int)
    }

    lazy val skillzRepr = (fred: Any) match {
      case Parzy(y, true) => s"dzielenie przez 2, na przykład $y"
      case p@Person(name, surname, _, skill :: _, _) =>
        skill
      case l: List[String@unchecked] if l.contains("Fread") =>
      case _ => throw new Error("Niemożliwe, Fred wszystko umie")
    }

    val pf: PartialFunction[Any, String] = {
      case _: Int =>
        "int"
      case l: List[String@unchecked] if l.contains("Fread") =>
        "lista"
    }

    val list: List[Int] = List(1, 2, 3)
    list match {
      case List(a, b, c, l@_*) => println(s"OGON ŚLIMAKA: $l")
    }

    val (int, str, lista) = (1, "string", List(3.14))
    println(int + 3)
    println(str.toUpperCase)
    println(lista.mkString(","))

    val map = Iterator.range(0, 10).map(i => (i, (i * i).toString))
      .collect({ case (k, v) => (k, v * 2) }).toMap

    object Collatz {
      def unapplySeq(int: Int): Opt[Seq[Int]] = {
        val result = new ArrayBuffer[Int]

        def collatz(int: Int): Unit = {
          result += int
          int match {
            case 1 =>
            case Parzy(i, _) => collatz(i)
            case _ => collatz(3 * int + 1)

          }
        }

        collatz(int)
        Opt(result)
      }
    }

    4321 match {
      case Collatz(f, s, t, tail@_*) =>
        println(f, s, t)
        println(tail)
    }

    val stringi = for {
      (k, v) <- List("a" -> 1, "b" -> 2, "c" -> 3)
      vkwadrat = v * v
      b <- vkwadrat to 5 if b % 2 == 0
    } yield s"$k $b"

    val numerki = for {
      a <- List(1, 2, 3)
      b <- List(4, 5, 6)
      c <- List(7, 8, 9)
    } yield a + b + c

    List(1, 2, 3).flatMap(a =>
      List(4, 5, 6).flatMap(b =>
        List(7, 8, 9).map(c =>
          a + b + c)))

    import scala.concurrent.ExecutionContext.Implicits.global

    def loadPerson(id: String): Future[Opt[Person]] = ???

    def savePerson(id: String, person: Person): Future[Unit] = ???

    def olderId(fid: String, sid: String): Future[Opt[String]] = {
      for {
        _ <- Future.unit
        f1 = loadPerson(fid)
        f2 = loadPerson(sid)
        maybeP1 <- f1
        maybeP2 <- f2
      } yield for {
        p1 <- maybeP1
        p2 <- maybeP2
      } yield if (p1.age > p2.age) fid else sid
    }


    def happyBirthdayTo(you: String): Future[Unit] = for {
      maybePerson <- loadPerson(you)
      _ <- maybePerson match {
        case Opt(p) => savePerson(you, p.copy(age = p.age + 1))
        case Opt.Empty => Future.unit
      }
    } yield ()

    def parseName: Opt[String] = ???

    def parseSurname: Opt[String] = ???

    def parsePerson(): Opt[Person] = for {
      name <- parseName
      surname <- parseSurname
    } yield Person(name, surname, null, null)

    println(if (pf.isDefinedAt(3.14)) pf(3.14) else "duble")
    println(pf.applyOrElse(3.14, (_: Double) => "duble"))
  }
}
