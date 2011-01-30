package org.jfugue.extras;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jfugue.ChannelPressure;
import org.jfugue.Controller;
import org.jfugue.ElementVisitor;
import org.jfugue.Instrument;
import org.jfugue.JFugueElement;
import org.jfugue.KeySignature;
import org.jfugue.Layer;
import org.jfugue.Measure;
import org.jfugue.Note;
import org.jfugue.Pattern;
import org.jfugue.PitchBend;
import org.jfugue.PolyphonicPressure;
import org.jfugue.SystemExclusiveEvent;
import org.jfugue.Tempo;
import org.jfugue.Time;
import org.jfugue.Voice;

public class LoggingVisitor implements ElementVisitor {
	
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

	public void visitChannelPressure(ChannelPressure channelPressure) {
		log.add(new Entry("visitChannelPressure", channelPressure));
	}

	public void visitController(Controller controller) {
		log.add(new Entry("visitController", controller));
	}

	public void visitInstrument(Instrument instrument) {
		log.add(new Entry("visitInstrument", instrument));
	}

	public void visitKeySignature(KeySignature keySignature) {
		log.add(new Entry("visitKeySignature", keySignature));
	}

	public void visitLayer(Layer layer) {
		log.add(new Entry("visitLayer", layer));
	}

	public void visitMeasure(Measure measure) {
		log.add(new Entry("visitMeasure", measure));
	}

	public void visitNote(Note note) {
		log.add(new Entry("visitNote", note));
	}

	public void visitPattern(Pattern pattern) {
		log.add(new Entry("visitPattern", pattern));
	}

	public void visitPichBend(PitchBend pitchBend) {
		log.add(new Entry("visitPichBend", pitchBend));
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