package org.jfugue.parsers.msp5.elements

import org.jfugue.parsers.ParserError
import org.jfugue.JFugueDefinitions

abstract class AbstChord
object Chord {
  lazy val CHORDS_MAP = org.jfugue.factories.NoteFactory.CHORDS_MAP
  def apply(name: String): AbstChord = {
    val intervals = CHORDS_MAP.get(name.toUpperCase)
    if (intervals == null)
      new InvalidChord(name)
    else
      new Chord(name, intervals)
  }
}
case class Chord(val name: String, private val intervals: Array[Byte]) extends AbstChord {
  def getName() = name
  def getIntervals() = java.util.Arrays.copyOf(intervals, intervals.length)
}
case class InvalidChord(val name: String) extends AbstChord {
  lazy val e = new ParserError("Invalid chord %s", name)
  def getName() = throw e
  def getIntervals() = throw e
}

abstract class NoteValue
case class LetterNote(val letter: String, val value : Byte, val octave: Option[Byte],
                      val natural: Boolean) extends NoteValue {
  private lazy val _octave = octave match {
    case Some(o) => o
    case _ => (0).toByte
  }
  def getValue() = value
  def getOctave() = _octave
  def isNatural() = natural
}
object CLetterNote {
  val ValidNoteRE = """^(?i)([A-G])([B#]{0,2}|N)$""".r
  val letterInvalid = "Letter notes must be A-G possibly followed by at most two of B,# or N, but it was %s"
  val octaveInvalid = "The octave of a note must be 0-10, but it was %s"
  def apply(letter: String, octave: Option[Byte]) : NoteValue = {
    def modify(mods: String) = mods.toUpperCase.foldLeft(0){
      (m, c) =>
      c match {
        case 'B' => m - 1
        case '#' => m + 1
        case _ => m
      }
    }
    val value = letter match {
      case ValidNoteRE(root, mods) => Some(modify(mods) + root.toUpperCase match {
        case "C" => 0
        case "D" => 2
        case "E" => 4
        case "F" => 5
        case "G" => 7
        case "A" => 9
        case "B" => 11
      })
      case _ => None
    }
    val isOctaveValid : Boolean = octave match {
      case Some(b) => (b >= 0 && b <= 10)
      case _ => true
    }
    if (!isOctaveValid)
    	new InvalidNoteValue(octaveInvalid.format(octave))
    else
    	value match {
    	  case Some(v) => new LetterNote(letter, v.toByte, octave, letter.toUpperCase.contains("N"))
    	  case _ => new InvalidNoteValue(letterInvalid.format(letter))
    	}
    	
  }
}
case class NumericNote(val value: Byte) extends NoteValue {
  private lazy val octave = (value / 12).toByte
  def getValue() = value
  def getOctave() = octave
  def isNatural() = true
}
object CNumericNote {
  val invMsg = "Numeric note values must be between 0 and 60, but it was %s"
  def apply(value : Byte) : NoteValue = {
    if (value >= 0 && value <= 60)
    	new NumericNote(value)
    else
        new InvalidNoteValue(invMsg.format(value))
  }
}
case class InvalidNoteValue(val message: String) extends NoteValue {
  lazy val e = new ParserError(message)
  def getValue(): Byte = throw e
  def getOctave(): Byte = throw e
  def isNatural(): Boolean = throw e
}

abstract class NoteDuration {
  def getMSDuration(): Long
  def getDecimalDuration(): Double
}
trait DurationConverter {
  def getMSFromDecimal(decimal: Double): Long = (JFugueDefinitions.SEQUENCE_RESOLUTION * decimal).toLong
}
case class LetterDuration(val value: String) extends NoteDuration with DurationConverter {
  lazy val ldRE = "([WHQISTXO])(\\.?)".r
  lazy val decimalDuration = ldRE.findAllIn(value).foldLeft(0.0){
      (acc, part) =>
      val f = part.charAt(0) match {
      	case 'W' => 1
      	case 'H' => 2
      	case 'Q' => 4
      	case 'I' => 8
      	case 'S' => 16
      	case 'T' => 32
      	case 'X' => 64
      	case 'O' => 128
      }
      val d = 1.0/f
      acc + d + (if (part.length > 1) d/2.0 else 0.0)
    }
  def getDecimalDuration() = decimalDuration
  lazy val duration = getMSFromDecimal(decimalDuration)
  def getMSDuration(): Long = duration
}
object CLetterDuration {
  val ValidDuration = "^(?i)([WHQISTXO]\\.?)+$".r
  val invMsg = """Letter durations must consist of the characters "WHQISTXO" possibly followed by ".", but it was """
  def apply(value: String): NoteDuration = value match {
    case ValidDuration => new LetterDuration(value)
    case _ => InvalidDuration(invMsg + value)
  }
}
case class NumericDuration(val value: Double) extends NoteDuration with DurationConverter {
  lazy val duration = getMSFromDecimal(value)
  def getMSDuration(): Long = duration
  def getDecimalDuration() = value 
}
object CNumericDuration {
  def apply(value: Double): NoteDuration = {
    if (value > 0)
    	new NumericDuration(value)
    else
        new InvalidDuration("Numeric durations must be greater than zero, but it was " + value.toString)
  }
}
case class InvalidDuration(val message: String) extends NoteDuration {
  lazy val e = new ParserError(message)
  def getMSDuration() = throw e
  def getDecimalDuration() = throw e
}

abstract class AbstVelocity
case class Velocity(val attack: Byte, val decay: Byte) extends AbstVelocity
object CVelocity {
  val velRegex = "(?i)[AD<>][0-9]+".r
  val AccRE = "[Aa<]([0-9]+)".r
  val DecRE = "[Dd>]([0-9]+)".r
  def apply(velocity: String): AbstVelocity = {
    val parts = velRegex.findAllIn(velocity)
    val (attack, decay) = parts.foldLeft((0,0)) {
      (vel, part) =>
      val (a,d) = vel
      part match {
        case AccRE(x) => (a + x.toInt, d)
        case DecRE(x) => (a, d + x.toInt)
        case _ => (a, d)
      }
    }
    if (0 > attack || 127 < attack || 0 > decay || 127 < decay)
      new InvalidVelocity("Velocities must be in the range 0-127")
    else
      new Velocity(attack.toByte, decay.toByte)
  }
}
case class InvalidVelocity(val message: String) extends AbstVelocity
