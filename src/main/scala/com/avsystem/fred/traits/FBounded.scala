package com.avsystem.fred
package traits

abstract class Base[T <: Base[T]] { this: T =>
  def withId(otherId: String): T = this
}

class Device extends Base[Device]


trait Serializator[T]
object Serializator extends LowPriorityImplicits {
  implicit def impl1[T](implicit ct: ClassTag[T]): Serializator[T] = ???

  protected def utilMethod(): Unit = ()
}
trait LowPriorityImplicits { this: Serializator.type =>
  implicit def impl2[T]: Serializator[T] = {
    utilMethod()
    ???
  }
}
