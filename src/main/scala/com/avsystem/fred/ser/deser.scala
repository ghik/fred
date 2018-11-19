package com.avsystem.fred
package ser

import java.util.Locale

import com.avsystem.commons.annotation.AnnotationAggregate
import com.avsystem.commons.serialization.{name, transientDefault, transparent}

@transparent case class Wrapper(str: String)
object Wrapper extends HasKodek[Wrapper]

class toUpper extends transform[String](_.toUpperCase(Locale.ENGLISH))
class prefix(prefix: String) extends transform[String](prefix + _)

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

object Teste {
  def main(args: Array[String]): Unit = {
    println(Kodek.writeJson(Fredynand("Fred", 42)))
    println(Kodek.writeJson(Wrapper("Wrapped")))
    println(Kodek.writeJson[Vehicle](Dorożka("Rafał")))
  }
}
