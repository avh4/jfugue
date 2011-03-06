package org.jfugue.parsers.msp5.elements

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
case class Chord(name: String, intervals: Array[Byte]) extends AbstChord
case class InvalidChord(name: String) extends AbstChord

abstract class NoteValue
case class LetterNote(letter: String, value : Byte, octave: Option[Byte]) extends NoteValue
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
    	  case Some(v) => new LetterNote(letter, v.toByte, octave)
    	  case _ => new InvalidNoteValue(letterInvalid.format(letter))
    	}
    	
  }
}
case class NumericNote(value: Byte) extends NoteValue
object CNumericNote {
  val invMsg = "Numeric note values must be between 0 and 60, but it was %s"
  def apply(value : Byte) : NoteValue = {
    if (value >= 0 && value <= 60)
    	new NumericNote(value)
    else
        new InvalidNoteValue(invMsg.format(value))
  }
}
case class InvalidNoteValue(message: String) extends NoteValue

abstract class NoteDuration {
  def getMSDuration(): Long
}
case class LetterDuration(value: String) extends NoteDuration {
  val ldRE = "([WHQISTXO])(\\.?)".r
  def getMSDuration(): Long = {
    val decimalDuration = ldRE.findAllIn(value).foldLeft(0.0){
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
    (org.jfugue.JFugueDefinitions.SEQUENCE_RESOLUTION * decimalDuration).toLong
  }
}
object CLetterDuration {
  val ValidDuration = "^(?i)([WHQISTXO]\\.?)+$".r
  val invMsg = """Letter durations must consist of the characters "WHQISTXO" possibly followed by ".", but it was """
  def apply(value: String): NoteDuration = value match {
    case ValidDuration => new LetterDuration(value)
    case _ => InvalidDuration(invMsg + value)
  }
}
case class NumericDuration(value: Double) extends NoteDuration {
  def getMSDuration(): Long =
    (org.jfugue.JFugueDefinitions.SEQUENCE_RESOLUTION * value).toLong
}
object CNumericDuration {
  def apply(value: Double): NoteDuration = {
    if (value > 0)
    	new NumericDuration(value)
    else
        new InvalidDuration("Numeric durations must be greater than zero, but it was " + value.toString)
  }
}
case class InvalidDuration(message: String) extends NoteDuration {
  def getMSDuration(): Long = 0L
}

abstract class AbstVelocity
case class Velocity(attack: Byte, decay: Byte) extends AbstVelocity
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
case class InvalidVelocity(message: String) extends AbstVelocity