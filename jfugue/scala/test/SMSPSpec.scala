package org.jfugue.test

import org.specs._
import org.specs.runner._
import org.jfugue._
import org.jfugue.parsers.msp5._
import java.io.StringReader

class SMSPSpecTest extends SpecificationWithJUnit {

  "ScalaMusicStringParser.parseAndEval" should {

    "when taking \"$FOO=100\", add the the key FOO and value 100 to the Environment's dictionary" in {
      val env = new Environment()
      val p = new ScalaMusicStringParser
      val sr = new StringReader("$FOO=100")
      println(p.parse(sr).toString)
      p.parseAndEval(sr, env)
      env.getFromDict("FOO") mustEqual "100"
    }

  }

}
