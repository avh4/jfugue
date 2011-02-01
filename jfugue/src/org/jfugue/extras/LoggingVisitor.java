package org.jfugue.extras;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jfugue.ChannelPressure;
import org.jfugue.Controller;
import org.jfugue.DefaultElementVistior;
import org.jfugue.Instrument;
import org.jfugue.JFugueElement;
import org.jfugue.KeySignature;
import org.jfugue.Layer;
import org.jfugue.Measure;
import org.jfugue.Note;
import org.jfugue.Note.Chord;
import org.jfugue.Pattern;
import org.jfugue.PitchBend;
import org.jfugue.PolyphonicPressure;
import org.jfugue.SystemExclusiveEvent;
import org.jfugue.Tempo;
import org.jfugue.Time;
import org.jfugue.Voice;

public class LoggingVisitor extends DefaultElementVistior {
	
	public static class Entry {
		protected String name;
		protected JFugueElement element;
		
		/**
		 * @param name
		 * @param element
		 */
		public Entry(String name, JFugueElement element) {
			super();
			this.name = name;
			this.element = element;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the element
		 */
		public JFugueElement getElement() {
			return element;
		}
		
		public String toString() {
			return getName() + "(" + getElement().getMusicString() + ")";
		}
		
	}
	
	protected List<Entry> log = new LinkedList<Entry>();
	protected List<Entry> iLog = Collections.unmodifiableList(log);
	
	/**
	 * @return the log
	 */
	public List<Entry> getLog() {
		return iLog;
	}
	
	public void clearLog() {
		log.clear();
	}
	
	public String toString() {
		if (log.isEmpty())
			return "[]";
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (Iterator<Entry> iterator = log.iterator(); iterator.hasNext();) {
			Entry entry = iterator.next();
			sb.append(entry.toString());
			if (iterator.hasNext())
				sb.append(", ");
		}
		sb.append(']');
		return sb.toString();
	}

	public void visit(ChannelPressure channelPressure) {
		log.add(new Entry("visitChannelPressure", channelPressure));
	}

	public void visit(Controller controller) {
		log.add(new Entry("visitController", controller));
	}

	public void visit(Instrument instrument) {
		log.add(new Entry("visitInstrument", instrument));
	}

	public void visit(KeySignature keySignature) {
		log.add(new Entry("visitKeySignature", keySignature));
	}

	public void visit(Layer layer) {
		log.add(new Entry("visitLayer", layer));
	}

	public void visit(Measure measure) {
		log.add(new Entry("visitMeasure", measure));
	}

	public void visit(Note note) {
		log.add(new Entry("visitNote", note));
	}

	public void visit(Pattern pattern) {
		log.add(new Entry("visitPattern", pattern));
	}

	public void visitVoice(Voice voice) {
		log.add(new Entry("visitVoice", voice));
	}

	public void visitSystemExclusiveEvent(
			SystemExclusiveEvent systemExclusiveEvent) {
		log.add(new Entry("visitSystemExclusiveEvent", systemExclusiveEvent));
	}

	public void visitTempo(Tempo tempo) {
		log.add(new Entry("visitTempo", tempo));
	}

	public void visitTime(Time time) {
		log.add(new Entry("visitTime", time));
	}

	public void visitPolyphonicPressure(
			PolyphonicPressure polyphonicPressure) {
		log.add(new Entry("visitPolyphonicPressure", polyphonicPressure));
	}

	public void visitChord(Chord chord) {
		log.add(new Entry("visitChord", chord));
	}

	public void visitParallelNote(Note note) {
		log.add(new Entry("visitParallelNote", note));
	}

	public void visitSequentialNote(Note note) {
		log.add(new Entry("visitSequentialNote", note));
	}

	public void visitPitchBend(PitchBend pitchBend) {
		log.add(new Entry("visitPitchBend", pitchBend));
	}

}