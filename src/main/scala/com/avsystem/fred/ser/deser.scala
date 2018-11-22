package com.avsystem.fred
package ser

import java.time.Instant
import java.util.Locale

import com.avsystem.commons.annotation.AnnotationAggregate
import com.avsystem.commons.serialization._

@transparent case class Wrapper(str: String)
object Wrapper extends HasKodek[Wrapper]

class toUpper extends transform[String](_.toUpperCase(Locale.ENGLISH), _.toLowerCase(Locale.ENGLISH))
class prefix(prefix: String) extends transform[String](prefix + _, _.stripPrefix(prefix))

class weird(prefixName: String) extends AnnotationAggregate {
  @name(prefixName) @prefix(prefixName) type Implied
}

case class Fredynand(
  @weird("ble") surname: String,
  @name("AgE") @transientDefault age: Double = 42
)
object Fredynand extends HasKodek[Fredynand]

sealed trait Vehicle
case class Auto(silnik: String) extends Vehicle
case class Dorożka(koń: String) extends Vehicle
object Dorożka extends HasKodek[Dorożka]
case object Koń extends Vehicle
object Vehicle extends HasKodek[Vehicle]

case class MyList(int: Int, tail: Opt[MyList])
object MyList extends HasKodek[MyList]

object CustomGenCodecs {
  implicit def instantCodec: GenCodec[Instant] = ???
}

case class Instantaneous(instant: Instant)
object Instantaneous extends HasGenCodecWithDeps[CustomGenCodecs.type, Instantaneous]

sealed trait Tree[A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
case class Leaf[A](value: A) extends Tree[A]
object Tree extends HasPolyGenCodec[Tree]

object Teste {
  def test[T: Kodek](value: T): Unit = {
    val json = Kodek.writeJson[T](value)
    println(json)
    val readValue = Kodek.readJson[T](json)
    if (value != readValue) {
      throw new AssertionError(s"Read value $readValue inconsistent with written value $value")
    }
  }

  def main(args: Array[String]): Unit = {
    test(Fredynand("Fred", 42))
    test(Wrapper("Wrapped"))
    test[Vehicle](Dorożka("Rafał"))
    test(MyList(42, Opt(MyList(43, Opt.Empty))))
  }
}
