package utest.runner
import sbt.testing._


import scala.scalajs.sbtplugin.environment.rhino.{Utilities, CodeBlock}
import scala.scalajs.sbtplugin.ScalaJSEnvironment
import org.mozilla.javascript.RhinoException

/**
 * Wraps a Scala callback in a Java-ish object that has the right signature
 * for Rhino to call.
 */
case class JsCallback(f: String => Unit){
  def apply__O__O(x: String) = f(x)
}

class JsRunner(val args: Array[String],
               val remoteArgs: Array[String],
               environment: ScalaJSEnvironment)
               extends GenericRunner{

  def doStuff(s: Seq[String], loggers: Seq[Logger], name: String) = {
    environment.runInContextAndScope { (context, scope) =>
      new CodeBlock(context, scope) with Utilities {
        try {
          println("--------------------", name.replace('.', '_'))
          val module = getModule("utest_package")
          val target = getModule(name.replace('.', '_'))
          val tests = callMethod(target, "tests__Lutest_util_Tree")
          println("::::::::::::::::::::", module)
          val results = callMethod(
            module,
            "runSuite__Lutest_util_Tree__AT__AT__Lscala_Function1__Lscala_Function1__Lscala_Function1__T",
            tests,
            toScalaJSArray(s.toArray),
            toScalaJSArray(args),
            JsCallback(s => if(s.toBoolean) success.incrementAndGet() else failure.incrementAndGet()),
            JsCallback(msg => loggers.foreach(_.info(progressString + name + "." + msg))),
            JsCallback(s => total.addAndGet(s.toInt))
          )
          addResult(results.toString)
        } catch {
          case t: RhinoException =>
            println(t.details, t.getScriptStack())
        }
      }
    }
  }
}

