package com.avsystem.fred
package ser

import com.avsystem.commons.annotation.positioned
import com.avsystem.commons.meta._
import com.avsystem.commons.misc.{Applier, Unapplier, ValueOf}
import com.avsystem.commons.serialization.GenCodec.ReadFailure
import com.avsystem.commons.serialization._
import com.avsystem.commons.serialization.json.{JsonOptions, JsonReader, JsonStringInput, JsonStringOutput}

import scala.annotation.StaticAnnotation
import scala.collection.mutable

class transform[T](val onWrite: T => T, val onRead: T => T) extends StaticAnnotation

trait Kodek[T] {
  def write(output: Output, value: T): Unit
  def read(input: Input): T
}
object Kodek {
  def apply[T](implicit kodek: Kodek[T]): Kodek[T] = kodek

  def writeJson[T: Kodek](value: T): String = {
    val sb = new JStringBuilder
    val o = new JsonStringOutput(sb, JsonOptions.Pretty)
    Kodek[T].write(o, value)
    sb.toString
  }

  def readJson[T: Kodek](json: String): T =
    Kodek[T].read(new JsonStringInput(new JsonReader(json)))

  def create[T](
    writeFun: (Output, T) => Unit,
    readFun: Input => T
  ): Kodek[T] = new Kodek[T] {
    def write(output: Output, value: T): Unit = writeFun(output, value)
    def read(input: Input): T = readFun(input)
  }

  implicit val IntKodek: Kodek[Int] = create(_.writeSimple().writeInt(_), _.readSimple().readInt())
  implicit val StringKodek: Kodek[String] = create(_.writeSimple().writeString(_), _.readSimple().readString())
  implicit def optCodec[T: Kodek]: Kodek[Opt[T]] = create(
    {
      case (o, Opt.Empty) => o.writeNull()
      case (o, Opt(v)) => Kodek[T].write(o, v)
    },
    i => if (i.readNull()) Opt.Empty else Opt(Kodek[T].read(i))
  )

  implicit def fromFallback[T](implicit fallbackKodek: Fallback[Kodek[T]]): Kodek[T] =
    fallbackKodek.value
}

sealed trait AdtKodek[T] extends Kodek[T]
object AdtKodek extends AdtMetadataCompanion[AdtKodek]

@positioned(positioned.here) class UnionKodek[T](
  @multi @adtCaseMetadata val cases: List[UnionCase[_]]
) extends AdtKodek[T] {
  def write(output: Output, value: T): Unit = {
    val caseUsed = cases.findOpt(_.isInstance(value))
      .getOrElse(throw new Exception(s"Unknown case: $value"))
    val oo = output.writeObject()
    val wrappedOutput = oo.writeField(caseUsed.name)
    caseUsed.kodek.asInstanceOf[Kodek[T]].write(wrappedOutput, value)
    oo.finish()
  }
  def read(input: Input): T = {
    val oi = input.readObject()
    val wrappedField = oi.nextField()
    val caseName = wrappedField.fieldName
    val caseUsed = cases.find(_.name == caseName)
      .getOrElse(throw new ReadFailure(s"Unknown case $caseName"))
      .asInstanceOf[UnionCase[T]]
    val result = caseUsed.kodek.read(wrappedField)
    oi.skipRemaining()
    result
  }
}
object UnionKodek extends AdtMetadataCompanion[UnionKodek]

sealed trait UnionCase[T] extends TypedMetadata[T] {
  def name: String
  def classTag: ClassTag[T]
  def kodek: Kodek[T]

  def isInstance(value: Any): Boolean =
    classTag.runtimeClass.isInstance(value.asInstanceOf[AnyRef])
}

@positioned(positioned.here) class CornerCase[T](
  @reifyName val name: String,
  @infer val classTag: ClassTag[T],
  @infer @checked val kodek: Kodek[T]
) extends UnionCase[T]

@positioned(positioned.here) class RecordCase[T](
  @reifyName val name: String,
  @infer val classTag: ClassTag[T],
  @composite val kodek: RecordKodek[T]
) extends UnionCase[T]

@positioned(positioned.here) class ObjectCase[T](
  @reifyName val name: String,
  @infer val classTag: ClassTag[T],
  @composite val kodek: SingletonKodek[T]
) extends UnionCase[T]

@positioned(positioned.here) @annotated[transparent] class TransparentKodek[T](
  @adtParamMetadata val field: RecordField[_],
  @infer @checked val unapplier: Unapplier[T],
  @infer @checked val applier: Applier[T]
) extends AdtKodek[T] {
  def write(output: Output, value: T): Unit = {
    val fieldValue = unapplier.unapply(value).head
    field.kodek.asInstanceOf[Kodek[Any]].write(output, fieldValue)
  }
  def read(input: Input): T =
    applier.apply(List(field.kodek.asInstanceOf[Kodek[Any]].read(input)))
}

@positioned(positioned.here) class RecordKodek[T](
  @multi @adtParamMetadata val fields: List[RecordField[_]],
  @infer @checked val unapplier: Unapplier[T],
  @infer @checked val applier: Applier[T]
) extends AdtKodek[T] {
  val fieldsByName: Map[String, RecordField[_]] = fields.toMapBy(_.rawName)

  def write(output: Output, value: T): Unit = {
    val oo = output.writeObject()
    (fields zip unapplier.unapply(value)).foreach {
      case (field: RecordField[Any@unchecked], fieldValue) =>
        if (!field.transientDefault || !field.defValue.contains(fieldValue)) {
          field.kodek.write(oo.writeField(field.rawName), field.writeTransformed(fieldValue))
        }
      case _ =>
    }
    oo.finish()
  }
  def read(input: Input): T = {
    val fvMap = new mutable.OpenHashMap[String, Any]
    val oi = input.readObject()
    while (oi.hasNext) {
      val fi = oi.nextField()
      fieldsByName.getOpt(fi.fieldName) match {
        case Opt(field: RecordField[Any@unchecked]) =>
          fvMap(fi.fieldName) = field.readTransformed(field.kodek.read(fi))
        case Opt.Empty => fi.skip()
      }
    }
    def whenAbsent(f: RecordField[_]): Any =
      f.defValue.getOrElse(throw new ReadFailure(s"Nie ma ${f.rawName}"))
    val fieldValues = fields.map(f => fvMap.getOrElse(f.rawName, whenAbsent(f)))
    applier(fieldValues)
  }
}
object RecordKodek extends AdtMetadataCompanion[RecordKodek]

@positioned(positioned.here) class SingletonKodek[T](
  @infer @checked val value: ValueOf[T]
) extends AdtKodek[T] with TypedMetadata[T] {
  def write(output: Output, value: T): Unit =
    output.writeObject().finish()
  def read(input: Input): T = {
    input.skip()
    value.value
  }
}

class RecordField[T](
  @reifyName val name: String,
  @optional @reifyAnnot val annotName: Opt[name],
  @isAnnotated[transientDefault] val transientDefault: Boolean,
  @optional @reifyDefaultValue val defaultValue: Opt[DefaultValue[T]],
  @multi @reifyAnnot val transforms: List[transform[T]],
  @infer kodekCreator: => Kodek[T]
) extends TypedMetadata[T] {
  def kodek: Kodek[T] = kodekCreator
  def rawName: String = annotName.fold(name)(_.name)
  val defValue: Opt[T] = defaultValue.flatMap(dv => Try(dv.value).toOpt)
  def writeTransformed(value: T): T = transforms.foldRight(value)((t, v) => t.onWrite(v))
  def readTransformed(value: T): T = transforms.foldLeft(value)((v, t) => t.onRead(v))
}

trait Kodeki[T] {
  def kodek: AdtKodek[T]
  def codec: GenCodec[T]
}

object CustomCodecs {
  implicit val doubleKodek: Fallback[Kodek[Double]] =
    Fallback(Kodek.create(_.writeSimple().writeDouble(_), _.readSimple().readDouble()))
}

abstract class HasKodek[T](
  implicit macroKodek: MacroInstances[CustomCodecs.type, Kodeki[T]]
) {
  implicit lazy val kodek: Kodek[T] = macroKodek(CustomCodecs, this).kodek
  implicit lazy val codec: GenCodec[T] = macroKodek(CustomCodecs, this).codec
}
