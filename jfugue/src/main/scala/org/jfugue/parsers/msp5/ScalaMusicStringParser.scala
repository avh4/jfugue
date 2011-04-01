package org.jfugue.parsers.msp5

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer
import org.jfugue.Environment
import org.jfugue.parsers.ParserInterface
import org.jfugue.elements.JFugueElement
import scala.util.parsing.combinator._
import org.jfugue.parsers.msp5.expressions._
import org.jfugue.parsers.msp5.elements._
import java.lang.{ Byte => JByte }

class ScalaMusicStringParser extends JavaTokenParsers {

  override def skipWhitespace = false

  def parseAndEval(r : java.io.Reader, env : Environment) : java.util.List[JFugueElement] =
    parseWithoutEval(r).map { e =>
      val jfe = e.eval(env)
      jfe.acceptVisitor(env) 
      jfe
    }
  def parseWithoutEval(r : java.io.Reader) : List[Element] = {
    parse(r) match {
      case this.Success(pr, _) => pr.flatten
      case x => null
    }
  }

  def parse(s : String) : ParseResult[List[List[Element]]] = parseAll(musicstring, s)
  def parse(r : java.io.Reader) : ParseResult[List[List[Element]]] = parseAll(musicstring, r)
  
  lazy val musicstring : Parser[List[List[Element]]] = repsep(element, """\s+""".r)

  lazy val element : Parser[List[Element]] = voice | tempo | instrument | layer | key | controller | time | poly_pressure | channel_pressure | pitch_bend | measure | dict_add | system_exclusive | note | comment

  lazy val voice = "[Vv]".r ~> byteExp ^^ { v => List(Voice(v)) }

  lazy val tempo = "[Tt]".r ~> intExp ^^ { t => List(Tempo(t)) }

  lazy val instrument = "[Ii]".r ~> byteExp ^^ { i => List(Instrument(i)) }

  lazy val layer = "[Ll]".r ~> byteExp ^^ { l => List(Layer(l)) }

  lazy val key = "[Kk]".r ~> letterNote ~ scale ^^ { case r ~ s => List(KeySignature(r,s)) }
  lazy val scale = "(?i)(MAJ|MIN)".r

  lazy val controller = "[Xx]".r ~> intExp ~ "=" ~ intExp ^^
    { case i ~ _ ~ v => List(Controller(i, v)) }

  lazy val time = "@" ~> longExp ^^ { t => List(Time(t)) }

  lazy val poly_pressure = "*" ~> byteExp ~ "," ~ byteExp ^^
    { case k ~ _ ~ p => List(PolyphonicPressure(k,p)) }

  lazy val channel_pressure = "+" ~> byteExp ^^ { p => List(ChannelPressure(p)) }

  lazy val pitch_bend = "&" ~> (int_pitch_bend | byte_pitch_bend)
  lazy val int_pitch_bend = intExp ^^ { i : IntExp => List(PitchBend(LSB(i), MSB(i))) }
  lazy val byte_pitch_bend = byteExp ~ "," ~ byteExp ^^
    { case lsb ~ _ ~ msb => List(PitchBend(lsb, msb)) }

  lazy val measure = "|" ^^ { _ => List(new Measure) }

  lazy val dict_add = "$" ~> symbol ~ "=" ~ ( decimalNumber | floatingPointNumber ) ^^
    { case key ~ _ ~ value => List(DictAdd(key, value)) }

  lazy val system_exclusive = "^" ~> ( dec_sysex | hex_sysex ) ^^ { se => List(se) }
  lazy val dec_sysex = "(?i)DEC".r ~ ":" ~ repsep(dec_byte, ",") ^^
    { case _ ~ _ ~ bytes => SystemExclusive(10, bytes) }
  lazy val hex_sysex = "(?i)HEX".r ~ ":" ~ repsep(hex_byte, ",") ^^
    { case _ ~ _ ~ bytes => SystemExclusive(16, bytes) }

  lazy val collected_note = "(" ~ repsep(root_note, "+") ~ ")" ~ (duration?) ^^
    { case _ ~ notes ~ _ ~ dur =>
      	notes.map { root => Note(root, None, dur, None) }
    }

  lazy val note = root_note ~ (chord?) ~ (duration?) ~ (velocity?) ^^
    { case r ~ c ~ d ~ v => List(Note(r, c, d, v)) }
  lazy val root_note = ((letterNote ~ (octave?) ^^ { case n ~ o => CLetterNote(n,o) })
                        | (numericNote ^^ { CNumericNote(_) }))
  lazy val octave = "10|[0-9]".r ^^ { _.toByte }
  
  lazy val chord = (regex("(?i)MAJ|MIN|AUG|DIM|DOM7|MAJ7|MIN7|SUS4|SUS2|MAJ6|MIN6|DOM9|MAJ9|MIN9|DIM7|ADD9|DAVE|MIN11|DOM11|DOM13|MIN13|MAJ13|DOM7<5|DOM7>5|MAJ7<5|MAJ7>5|MINMAJ7|DOM7<5<9".r)
		    ^^ { Chord(_) })

  lazy val duration = letterDuration | numericDuration
  lazy val letterDuration = "(?i)([WHQISTXO]\\.?)+".r ^^ { CLetterDuration(_) }
  lazy val numericDuration = "/" ~> doubleNumber ^^ { CNumericDuration(_) }
  lazy val doubleNumber = "[0-9]+(.[0-9]+)?".r ^^ { _.toDouble }

  lazy val velocity = "(?i)([AD<>][0-9]+)+".r ^^ { CVelocity(_) }

  lazy val byteExp = (dec_byte ^^ { x => LByte(x) }
		      | inDict ^^ { x => ByteInDict(x) } )

  lazy val intExp = (decimalNumber ^^ { x => LInt(x.toInt) }
		     | inDict ^^ { x => IntInDict(x) } )

  lazy val longExp = (decimalNumber ^^ { x => LLong(x.toLong) }
		     | inDict ^^ { x => LongInDict(x) } )

  lazy val dec_byte = "-?[0-9]{1,3}".r ^^ { _.toByte }
  lazy val hex_byte = "(?i)[0-9A-F]{2}".r ^^ { x => JByte.parseByte(x, 16) }

  lazy val inDict = "[" ~> symbol <~ "]"

  lazy val symbol = ident

  lazy val letterNote = "(?i)[A-G][BN#]{0,2}".r

  lazy val numericNote = "[" ~> dec_byte <~ "]" // ^^ { _.toByte }

  lazy val comment = "(?m)^#.*?$".r ^^ { c => List(Comment(c)) }
//  lazy val ordinary_comment = "(?m).*?$".r ^^ { OrdinaryComment(_) }
//  lazy val property_comment = "\\s*".r ~> ident ~ ": " ~ "(?m).*?$".r ^^ { case k ~ _ ~ v => PropertyComment(k,v) }

}
