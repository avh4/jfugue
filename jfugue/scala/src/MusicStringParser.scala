package org.jfugue.parsers.msp5
import scala.util.parsing.combinator._
import org.jfugue.parsers.msp5.expressions._
import org.jfugue.parsers.msp5.elements._

class MusicStringParser extends JavaTokenParsers {

  def parse(s : String) : ParseResult[_] = parseAll(musicstring, s)

  lazy val musicstring : Parser[List[_]] = rep(element)

  lazy val element = voice | tempo | instrument | layer | key | controller | time | poly_pressure | channel_pressure | pitch_bend | measure | dict_add | collected_note | note | comment

  lazy val voice = "[Vv]".r ~> byteExp ^^ { Voice(_) }

  lazy val tempo = "[Tt]".r ~> intExp ^^ { Tempo(_) }

  lazy val instrument = "[Ii]".r ~> byteExp ^^ { Instrument(_) }

  lazy val layer = "[Ll]".r ~> byteExp ^^ { Layer(_) }

  lazy val key = "[Kk]".r ~> letterNote ~ scale ^^ { case r ~ s => KeySignature(r,s) }
  lazy val scale = "(?i)(MAJ|MIN)".r

  lazy val controller = "[Xx]".r ~> intExp ~ "=" ~ intExp ^^ { case i ~ _ ~ v => Controller(i, v) }

  lazy val time = "@" ~> longExp ^^ { Time(_) }

  lazy val poly_pressure = "*" ~> byteExp ~ "," ~ byteExp ^^ { case k ~ _ ~ p => PolyphonicPressure(k,p) }

  lazy val channel_pressure = "+" ~> byteExp ^^ { ChannelPressure(_) }

  lazy val pitch_bend = "&" ~> (int_pitch_bend | byte_pitch_bend)
  lazy val int_pitch_bend = intExp ^^ { i : IntExp => PitchBend(LSB(i), MSB(i)) }
  lazy val byte_pitch_bend = byteExp ~ "," ~ byteExp ^^ { case lsb ~ _ ~ msb => PitchBend(lsb, msb) }

  lazy val measure = "|" ^^ { _ => new Measure }

  lazy val dict_add = "$" ~> symbol ~ "=" ~ ( decimalNumber | floatingPointNumber ) ^^ { case key ~ _ ~ value => DictAdd(key, value) }

//  lazy val system_exclusive = "^" ~> ( dec_sysex | hex_sysex )
//  lazy val dec_sysex = "(?i)DEC".r <~ ":" ~> repsep(decimalNumber, ",")
//  lazy val hex_sysex = "(?i)HEX".r <~ ":" ~> repsep(hex_byte. ",")

  lazy val collected_note = "(" ~> repsep(root_note, "+") <~ ")" ~> duration

  lazy val note = root_note ~ (chord?) ~ (duration?) ~ (velocity?) ^^ { case r ~ c ~ d ~ v => Note(r, c, d, v) }
  lazy val root_note = ((letterNote ~ (octave?) ^^ { case n ~ o => LetterNote(n,o) })
			| (numericNote ^^ { NumericNote(_) }))
  lazy val octave = "10|[0-9]".r ^^ { _.toByte }
  
  lazy val chord = (regex("(?i)MAJ|MIN|AUG|DIM|DOM7|MAJ7|MIN7|SUS4|SUS2|MAJ6|MIN6|DOM9|MAJ9|MIN9|DIM7|ADD9|DAVE|MIN11|DOM11|DOM13|MIN13|MAJ13|DOM7<5|DOM7>5|MAJ7<5|MAJ7>5|MINMAJ7|DOM7<5<9".r)
		    ^^ { Factories.createChord(_) })

  lazy val duration = letterDuration | numericDuration
  lazy val letterDuration = "(?i)[WHQISTXO]+\\.?".r ^^ { LetterDuration(_) }
  lazy val numericDuration = "/" ~> doubleNumber ^^ { NumericDuration(_) }
  lazy val doubleNumber = "[0-9]+(.[0-9+)?".r ^^ { _.toDouble }

  lazy val velocity = "(?i)([AD<>][0-9]+)+".r

  lazy val byteExp = (dec_byte ^^ { x => LByte(x.toByte) }
		      | inDict ^^ { x => ByteInDict(x) } )

  lazy val intExp = (decimalNumber ^^ { x => LInt(x.toInt) }
		     | inDict ^^ { x => IntInDict(x) } )

  lazy val longExp = (decimalNumber ^^ { x => LLong(x.toLong) }
		     | inDict ^^ { x => LongInDict(x) } )


  lazy val bytePossInDict = dec_byte ^^ { _.toByte }
			   //  | inDict ^^ { ByteInDict(_) })
			  //| (hex_byte ^^ { Byte.parseByte(_, 16) }) | inDict
  lazy val dec_byte = "-?[0-9]{1,3}".r
  lazy val hex_byte = "(?i)[0-9A-F]{2}".r

  lazy val intPossInDict = decimalNumber ^^ { _.toInt } //| inDict

  lazy val longPossInDict = decimalNumber ^^ { _.toLong } //| inDict

  lazy val inDict = "[" ~> symbol <~ "]"

  lazy val symbol = ident

//  lazy val root_note = letterNote | numericNote

  lazy val letterNote = "(?i)[A-G][BN#]{0,2}".r

  lazy val numericNote = "[" ~> dec_byte <~ "]" ^^ { _.toByte }

  lazy val comment = "(?m)^#".r ~> (property_comment | ordinary_comment)
  lazy val ordinary_comment = "(?m).*?$".r ^^ { OrdinaryComment(_) }
  lazy val property_comment = "\\s*".r ~> ident ~ ": " ~ "(?m).*?$".r ^^ { case k ~ _ ~ v => PropertyComment(k,v) }

}
