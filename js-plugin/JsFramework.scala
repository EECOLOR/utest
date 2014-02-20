package utest.runner

import scala.scalajs.sbtplugin.ScalaJSEnvironment


class JsFramework(environment: ScalaJSEnvironment) extends utest.runner.GenericTestFramework{

  def runner(args: Array[String],
             remoteArgs: Array[String],
             testClassLoader: ClassLoader) = {
    new JsRunner(args, remoteArgs, environment)
  }
}
