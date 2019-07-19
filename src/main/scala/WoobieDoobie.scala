
import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import cats._
import cats.data._
import cats.effect.IO
import cats.implicits._

object WoobieDoobie extends App {

  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

  val driver = classOf[org.h2.Driver].getName
  val url = "jdbc:h2:mem:test"
  val user = "sa"
  val pass = ""

  val xa = Transactor.fromDriverManager[IO](driver, url, user, pass)

  val flag = true

  sql"select one, two from CSVREAD('classpath:test.csv') where flag = ${true: Boolean};"
    .query[(String, String)]    // Query0[String]
    .to[List]         // ConnectionIO[List[String]]
    .transact(xa)     // IO[List[String]]
    .unsafeRunSync    // List[String]
    .take(5)          // List[String]
    .foreach(println) // Unit

  println("it worked")

}
