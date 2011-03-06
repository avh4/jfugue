package org.jfugue.parsers.msp5.elements

import org.jfugue.parsers.msp5.expressions._
import org.jfugue.Environment
import org.jfugue.elements.JFugueElement


abstract class Element {
	def toJFElement(env : Environment) : JFugueElement
}

case class Voice(voice : ByteExp) extends Element {
	def toJFElement(env : Environment) = new org.jfugue.elements.Voice(voice.eval(env))
}
case class Tempo(tempo : IntExp) extends Element {
	def toJFElement(env : Environment) = new org.jfugue.elements.Tempo(tempo.eval(env))
}
case class Instrument(instrument : ByteExp) extends Element {
	def toJFElement(env : Environment) = new org.jfugue.elements.Instrument(instrument.eval(env))
}
case class Layer(layer : ByteExp) extends Element {
	def toJFElement(env : Environment) = new org.jfugue.elements.Layer(layer.eval(env))
}
case class KeySignature(letterNote : String, scale : String) extends Element {
	def toJFElement(env : Environment) = new org.jfugue.elements.KeySignature(letterNote, scale)
}
case class Controller(index : IntExp, value : IntExp) extends Element {
	def toJFElement(env : Environment) = new org.jfugue.elements.Controller(index.eval(env), value.eval(env))
}
case class Time(time : LongExp) extends Element {
	def toJFElement(env : Environment) = new org.jfugue.elements.Time(time.eval(env))
}
case class SystemExclusive(radix: Int, bytes: List[Byte]) extends Element {
	def toJFElement(env : Environment) = new org.jfugue.elements.SystemExclusive(bytes.toArray)
}
case class PolyphonicPressure(key : ByteExp, pressure : ByteExp) extends Element {
	def toJFElement(env : Environment) = new org.jfugue.elements.PolyphonicPressure(key.eval(env), pressure.eval(env))
}
case class ChannelPressure(pressure : ByteExp) extends Element {
	def toJFElement(env : Environment) = new org.jfugue.elements.ChannelPressure(pressure.eval(env))
}
case class PitchBend(lsb : ByteExp, msb : ByteExp) extends Element {
	def toJFElement(env : Environment) = new org.jfugue.elements.PitchBend(lsb.eval(env), msb.eval(env))
}
case class Measure() extends Element {
	def toJFElement(env : Environment) = new org.jfugue.elements.Measure()
}
case class Note(value : NoteValue, chord : Option[AbstChord], duration : Option[NoteDuration], velocity : Option[AbstVelocity]) extends Element {
	def toJFElement(env : Environment) = {
	  val (noKey,nVal): (Boolean,Int) = value match {
	    case LetterNote(l, v, o) => (l.toUpperCase.contains("N"),
	    	o match {
	    	  case Some(oc) => v + oc * 12
	    	  case None => if (chord == None) v + 5*12 else v + 3*12
	    })
	    case NumericNote(v) => (true,v)
	  }
	  val dur = duration match {
	    case Some(dd) => dd.getMSDuration
	    case None => (org.jfugue.JFugueDefinitions.SEQUENCE_RESOLUTION * 0.25).toLong
	  }
	  val (attack, decay) = velocity match {
	    case Some(Velocity(a,d)) => (a, d)
	    case _ => (org.jfugue.elements.Note.DEFAULT_VELOCITY, org.jfugue.elements.Note.DEFAULT_VELOCITY)
	  }
	  chord match {
	    case Some(Chord(name,bytes)) =>
	      new org.jfugue.elements.Chord(nVal.toByte, dur, attack.toByte,
				decay.toByte, noKey, name, bytes)
	    case _ =>
	      new org.jfugue.elements.Note(nVal.toByte, dur, attack.toByte, decay.toByte, noKey)
	  }
	}
}

case class DictAdd(key : String, value : String) extends Element {
	def toJFElement(env : Environment) = new org.jfugue.elements.DictAdd(key, value)
}
//  case class AnyElem(s : Any) extends Element
case class Comment(comment : String) extends Element {
	def toJFElement(env : Environment) = new org.jfugue.elements.Comment(comment)
}
//  case class PropertyComment(key : String, value : String) extends Element
//  case class OrdinaryComment(comment : String) extends Element

