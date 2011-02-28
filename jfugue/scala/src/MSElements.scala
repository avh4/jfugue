package org.jfugue.parsers.msp5.elements
import org.jfugue.parsers.msp5.expressions._

abstract class Element

case class Voice(voice : ByteExp) extends Element
case class Tempo(tempo : IntExp) extends Element
case class Instrument(instrument : ByteExp) extends Element
case class Layer(layer : ByteExp) extends Element
case class KeySignature(letterNote : String, scale : String) extends Element
case class Controller(index : IntExp, value : IntExp) extends Element
case class Time(time : LongExp) extends Element
case class PolyphonicPressure(key : ByteExp, pressure : ByteExp) extends Element
case class ChannelPressure(pressure : ByteExp) extends Element
case class PitchBend(lsb : ByteExp, msb : ByteExp) extends Element
case class Measure() extends Element
case class Note(value : NoteValue, chord : Option[AbstChord], duration : Option[NoteDuration], velocity : Option[String]) extends Element

case class DictAdd(key : String, value : String) extends Element
case class AnyElem(s : Any) extends Element
case class Comment(comment : String) extends Element
case class PropertyComment(key : String, value : String) extends Element
case class OrdinaryComment(comment : String) extends Element
