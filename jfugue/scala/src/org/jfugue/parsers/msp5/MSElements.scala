package org.jfugue.parsers.msp5

abstract class Element

case class Voice(voice : Byte) extends Element
case class Tempo(tempo : Int) extends Element
case class Instrument(instrument : Byte) extends Element
case class Layer(layer : Byte) extends Element
case class KeySignature(letterNote : String, scale : String) extends Element
case class Controller(index : Int, value : Int) extends Element
case class Time(time : Long) extends Element
case class PolyphonicPressure(key : Byte, pressure : Byte) extends Element
case class ChannelPressure(pressure : Byte) extends Element
case class PitchBend(lsb : Byte, msb : Byte) extends Element
case class Measure() extends Element
case class Note(value : NoteValue, chord : Option[AbstChord], duration : Option[NoteDuration], velocity : Option[String]) extends Element

case class DictAdd(key : String, value : String) extends Element
case class AnyElem(s : Any) extends Element
case class Comment(comment : String) extends Element
case class PropertyComment(key : String, value : String) extends Element
case class OrdinaryComment(comment : String) extends Element
