package utest.runner

import sbt.testing._
import utest.framework.TestSuite

class JvmRunner(val args: Array[String],
                val remoteArgs: Array[String])
                extends GenericRunner{
  def doStuff(s: Seq[String], loggers: Seq[Logger], name: String) = {
    val cls = Class.forName(name + "$")
    val suite = cls.getField("MODULE$").get(cls).asInstanceOf[TestSuite]
    val res = utest.runSuite(
      suite.tests,
      s.toArray,
      args,
      s => if(s.toBoolean) success.incrementAndGet() else failure.incrementAndGet(),
      msg => loggers.foreach(_.info(progressString + name + "." + msg)),
      s => total.addAndGet(s.toInt)
    )

    addResult(res)
  }
}

