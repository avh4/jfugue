package org.jfugue.parsers.msp5

class Environment

abstract class Expression[T] {
  def valueInEnvironment(env : Environment) : T
}

abstract class IntExp extends Expression[Int]
case class LInt(int : Int) extends IntExp {
  def valueInEnvironment(env : Environment) = int
}
case class IntInDict(key : String) extends IntExp {
  def valueInEnvironment(env : Environment) = 10000 // TODO
}

abstract class ByteExp extends Expression[Byte]
case class LByte(byte : Byte) extends ByteExp {
  def valueInEnvironment(env : Environment) = byte
}
case class ByteInDict(key : String) extends ByteExp {
  def valueInEnvironment(env : Environment) = 0 // TODO
}
case class LSB(int : IntExp) extends ByteExp {
  def valueInEnvironment(env : Environment) = (int.valueInEnvironment(env) % 128).asInstanceOf[Byte]
}
case class MSB(int : IntExp) extends ByteExp {
  def valueInEnvironment(env : Environment) = (int.valueInEnvironment(env) / 128).asInstanceOf[Byte]
}

abstract class LongExp extends Expression[Long]
case class LLong(long : Long) extends LongExp {
  def valueInEnvironment(env : Environment) = long
}
case class LongInDict(key : String) extends LongExp {
  def valueInEnvironment(env : Environment) = "1".toLong // TODO
}

abstract class AbstChord
case class Chord(name : String, intervals : Byte*) extends AbstChord
case class InvalidChord(name : String) extends AbstChord

abstract class NoteValue
case class LetterNote(letter : String, octave : Option[Byte]) extends NoteValue
case class NumericNote(value : Byte) extends NoteValue

abstract class NoteDuration
case class LetterDuration(value : String) extends NoteDuration
case class NumericDuration(value : Double) extends NoteDuration

object Factories {
  def createChord(name : String) : AbstChord = name.toUpperCase match {
    case "MAJ" => Chord(name, 4,7)
    case "MIN" => Chord(name, 3,7)
    case "AUG" => Chord(name, 4,8)
    case "DIM" => Chord(name, 3,6)
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


