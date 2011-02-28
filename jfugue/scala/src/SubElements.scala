package org.jfugue.parsers.msp5.elements

abstract class AbstChord
case class Chord(name: String, intervals: Byte*) extends AbstChord
case class InvalidChord(name: String) extends AbstChord

abstract class NoteValue
case class LetterNote(letter: String, octave: Option[Byte]) extends NoteValue
case class NumericNote(value: Byte) extends NoteValue

abstract class NoteDuration
case class LetterDuration(value: String) extends NoteDuration
case class NumericDuration(value: Double) extends NoteDuration

object Factories {
  def createChord(name: String): AbstChord = name.toUpperCase match {
    case "MAJ" => Chord(name, 4, 7)
    case "MIN" => Chord(name, 3, 7)
    case "AUG" => Chord(name, 4, 8)
    case "DIM" => Chord(name, 3, 6)
    case "DOM7" => Chord(name, 4, 7, 10)
    case "MAJ7" => Chord(name, 4, 7, 11)
    case "MIN7" => Chord(name, 3, 7, 10)
    case "SUS4" => Chord(name, 5, 7)
    case "SUS2" => Chord(name, 2, 7)
    case "MAJ6" => Chord(name, 4, 7, 9)
    case "MIN6" => Chord(name, 3, 7, 9)
    case "DOM9" => Chord(name, 4, 7, 10, 14)
    case "MAJ9" => Chord(name, 4, 7, 11, 14)
    case "MIN9" => Chord(name, 3, 7, 10, 14)
    case "DIM7" => Chord(name, 3, 6, 9)
    case "ADD9" => Chord(name, 4, 7, 14)
    case "DAVE" => Chord(name, 7, 14, 21)
    case "MIN11" => Chord(name, 7, 10, 14, 15, 17)
    case "DOM11" => Chord(name, 7, 10, 14, 17)
    case "DOM13" => Chord(name, 7, 10, 14, 16, 21)
    case "MIN13" => Chord(name, 7, 10, 14, 15, 21)
    case "MAJ13" => Chord(name, 7, 11, 14, 16, 21)
    case "DOM7<5" => Chord(name, 4, 6, 10)
    case "DOM7>5" => Chord(name, 4, 8, 10)
    case "MAJ7<5" => Chord(name, 4, 6, 11)
    case "MAJ7>5" => Chord(name, 4, 8, 11)
    case "MINMAJ7" => Chord(name, 3, 7, 11)
    case "DOM7<5<9" => Chord(name, 4, 6, 10, 13)
    case "DOM7<5>9" => Chord(name, 4, 6, 10, 15)
    case "DOM7>5<9" => Chord(name, 4, 8, 10, 13)
    case "DOM7>5>9" => Chord(name, 4, 8, 10, 15)
    case _ => InvalidChord(name)
  }

}

