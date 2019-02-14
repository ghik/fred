package com.avsystem.fred

import java.util.concurrent.{CompletableFuture, Future => JFuture}

case class Persona(id: String, money: Long = 0)

object SyncDB {
  def get(id: String): Persona = Persona(id)
  def save(p: Persona): Unit = ()
}

object JFuturesDB {
  def get(id: String): JFuture[Persona] = CompletableFuture.completedFuture(Persona(id))
  def save(p: Persona): JFuture[Unit] = CompletableFuture.completedFuture(())
}

object FuturesDB {
  def get(id: String): Future[Persona] = Future.successful(Persona(id))
  def save(p: Persona): Future[Unit] = Future.unit
}

object CpsDB {
  def get(id: String, cont: Try[Persona] => Unit): Unit =
    cont(Success(Persona(id)))
  def save(p: Persona, cont: Try[Unit] => Unit): Unit =
    cont(Success(()))

  def incMoney(id: String, cont: Try[Unit] => Unit): Unit =
    get(id, {
      case Success(p) => save(p.copy(money = p.money + 1), cont)
      case Failure(t) => cont(Failure(t))
    })
}

object AsyncDB {
  def get(id: String): Async[Persona] = successfulAsync(Persona(id))
  def save(p: Persona): Async[Unit] = successfulAsync(())

  def incMoney(id: String): Async[Unit] = for {
    p <- AsyncDB.get(id)
    _ <- AsyncDB.save(p.copy(money = p.money + 1))
  } yield ()
}

object Async {
  def readFile(filename: String): JFuture[String] =
    CompletableFuture.completedFuture("")

  def main(args: Array[String]): Unit = {
    val filename = "costam.txt"
    val contentsFut = readFile(filename)
    val upperFilename = filename.toUpperCase
    val contents = contentsFut.get
  }

}
