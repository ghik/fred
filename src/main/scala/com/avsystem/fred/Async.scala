package com.avsystem.fred

import java.util.concurrent.{CompletableFuture, Future => JFuture}

import monix.eval.Task

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

  def bothMoney(id1: String, id2: String)(implicit ec: ExecutionContext): Future[Long] = {
    for {
      _ <- Future.unit
      p1f = get(id1)
      p2f = get(id2)
      m1 <- p1f.mapNow(_.money)
      m2 <- p2f.mapNow(_.money)
    } yield m1 + m2
  }

  def totalMoney(maxid: Int)(implicit ec: ExecutionContext): Future[Long] = {
    def loop(id: Int, money: Long): Future[Long] =
      if (id > maxid) Future.successful(money)
      else get(id.toString).flatMap(p => loop(id + 1, money + p.money))
    loop(0, 0)
  }

  def main(args: Array[String]): Unit = {
    import com.avsystem.commons.concurrent.RunNowEC.Implicits._
    totalMoney(1000000).onComplete(println)
  }
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

object TaskDB {
  def get(id: String): Task[Persona] = Task.now(Persona(id))
  def save(p: Persona): Task[Unit] = Task.unit

  def incMoney(id: String): Task[Unit] = for {
    p <- get(id)
    _ <- save(p.copy(money = p.money + 1))
  } yield ()
}

object Async {
  def readFile(filename: String): JFuture[String] =
    CompletableFuture.completedFuture("")

  def main(args: Array[String]): Unit = {

  }

}
