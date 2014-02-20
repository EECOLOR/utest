package utest

import scala.concurrent.{Await, ExecutionContext, Future}

import concurrent.duration._
import java.io.{PrintWriter, StringWriter}
import acyclic.file
/**
 * Platform specific stuff that differs between JVM and JS
 */
object PlatformShims {
  def flatten[T](f: Future[Future[T]])(implicit ec: ExecutionContext): Future[T] = f.flatMap(x => x)

  def await[T](f: Future[T]): T = Await.result(f, 10.hours)

  def getTrace(e: Throwable): String = {
    val sw = new StringWriter()
    val pw = new PrintWriter(sw)
    pw.write("\n")
    e.printStackTrace(pw)
    sw.toString
  }
}
