package com.avsystem.fred
package parpoly

class PolyMain[A >: Null <: AnyRef] {
  def method[T >: CharSequence](): Unit = ()

  type FajnySet[X] = Set[Set[X]]

  type A = FajnySet[_]
  type A0 = Set[Set[X]] forSome {type X}

  type B0 = Set[Set[X] forSome {type X}]

  trait Cos {
    def method: CharSequence
  }

  type LepszeCos = Cos {
    def method: String
  }

  val x: LepszeCos = ???
  x.method

  trait MyStack {
    type Elem

    def push(elem: Elem): Unit
    def pop(): Elem
  }
  object MyStack {
    type Aux[E] = MyStack {type Elem = E}
  }

  trait StackUtils {
    val stack: MyStack

    def pop2(): (stack.Elem, stack.Elem) =
      (stack.pop(), stack.pop())
  }
  object StackUtils {
    def apply(s: MyStack): StackUtils {val stack: s.type} =
      new StackUtils {
        val stack: s.type = s
      }
  }

  abstract class BaseTask {
    def setConfig(config: String): this.type = {
      // ...
      this
    }
  }
  class Task extends BaseTask

  val task: Task = new Task().setConfig("costam")
}

object WeźScala {
  trait HasName {
    type Self >: this.type <: HasName

    def name: String
    def withName(newName: String): Self

    def optimizedRename(newName: String): Self =
      if(name == newName) this
      else withName(newName)
  }
  final case class Person(name: String, surname: String) extends HasName {
    type Self = Person
    def withName(newName: String): Person = copy(name = newName)
  }

  def capitalizeName(hasName: HasName): hasName.Self =
    hasName.withName(hasName.name.capitalize)

  val capPerson: Person = capitalizeName(Person("fred", "jakis"))
  val hasName: HasName = capPerson
}