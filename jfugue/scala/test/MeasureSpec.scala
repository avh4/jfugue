import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import org.scalatest.matchers.ShouldMatchers
//import org.scalatest.mock.EasyMockSugar
import org.jfugue.elements.Measure
import org.jfugue.visitors.ElementVisitor


class MeasureSpec extends WordSpec with MustMatchers {
  val m = new Measure()

  "A Measure" should {

    "have \"|\" as its MusicString" in {
      m.getMusicString() must be === "|"
    }

    "have \"Measure\" as its VerifyString" in {
      m.getVerifyString() must be === "Measure"
    }

    "not be null" in {
      m must not be (null)
    }

    "not be equal a non-maesure" in {
      m must not equal (new String())
    }

    "equal self" in {
      m must equal (m)
    }

    "equal a different measure" in {
      m must equal (new Measure())
    }

 /*   "call a visitor" in {
      val v = mock[ElementVisitor]
      expecting {
	v.visit(m)
      }
      whenExecuting(v) {
	m.acceptVisitor(v)
      }
    }
*/
  }

}
