package org.jfugue.extras;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jfugue.Pattern;
import org.jfugue.PatternInterface;
import org.jfugue.PatternTransformer;
import org.jfugue.elements.ChannelPressure;
import org.jfugue.elements.Controller;
import org.jfugue.elements.Instrument;
import org.jfugue.elements.JFugueElement;
import org.jfugue.elements.KeySignature;
import org.jfugue.elements.Layer;
import org.jfugue.elements.Measure;
import org.jfugue.elements.Note;
import org.jfugue.elements.PitchBend;
import org.jfugue.elements.PolyphonicPressure;
import org.jfugue.elements.SystemExclusive;
import org.jfugue.elements.Tempo;
import org.jfugue.elements.Time;
import org.jfugue.elements.Voice;

/**
 * EXPIRIMENTAL: This {@link PatternTransformer} converts a {@link Pattern} to a
 * more canonical form which can be (hopefully) compared to others.
 * 
 * @author joshua
 * 
 */
public class CanonicalPatternTransformer extends PatternTransformer {

	private LinkedList<JFugueElement> elements = new LinkedList<JFugueElement>();

	protected List<JFugueElement> getElements() {
		return elements;
	}

	protected void addElement(JFugueElement element) {
		elements.add(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jfugue.PatternTool#processResult(java.lang.Object)
	 */
	protected PatternInterface processResult(PatternInterface result) {
		for (JFugueElement element : getElements()) {
			result.add(element);
		}
		return result;
	}

	private Map<Class<? extends JFugueElement>, JFugueElement> last = new HashMap<Class<? extends JFugueElement>, JFugueElement>();

	protected JFugueElement addUniqueSinceLast(JFugueElement element) {
		Class<? extends JFugueElement> clazz = element.getClass();
		if (!element.equals(last.get(clazz))) {
			addElement(element);
			last.put(clazz, element);
			return element;
		} else {
			return (JFugueElement) last.get(clazz);
		}
	}

	public void voiceEvent(Voice voice) {
		addUniqueSinceLast(voice);
	}

	public void tempoEvent(Tempo tempo) {
		addUniqueSinceLast(tempo);
	}

	public void instrumentEvent(Instrument instrument) {
		// TODO This is a slight problem
		addElement(instrument);
	}

	public void layerEvent(Layer layer) {
		addUniqueSinceLast(layer);
	}

	public void timeEvent(Time time) {
		JFugueElement prev = (JFugueElement) getElements().get(
				getElements().size());
		if (prev instanceof Time) {
			time = new Time(time.getTime() + ((Time) prev).getTime());
		}
		if (time.getTime() == 0L)
			addElement(time);
	}

	public void keySignatureEvent(KeySignature keySig) {
		addUniqueSinceLast(keySig);
	}

	public void systemExclusiveEvent(SystemExclusive sysex) {
		addElement(sysex);
	}

	public void measureEvent(Measure measure) {
		addElement(measure);
	}

	public void controllerEvent(Controller controller) {
		addElement(controller);
	}

	public void channelPressureEvent(ChannelPressure channelPressure) {
		addUniqueSinceLast(channelPressure);
	}

	public void polyphonicPressureEvent(PolyphonicPressure polyphonicPressure) {
		addElement(polyphonicPressure);
	}

	public void pitchBendEvent(PitchBend pitchBend) {
		addElement(pitchBend);
	}

	public void noteEvent(Note note) {
		addElement(note);
	}

	public void sequentialNoteEvent(Note note) {
		addElement(note);
	}

	public void parallelNoteEvent(Note note) {
		addElement(note);
	}

	public static final Map<Class<? extends JFugueElement>, JFugueElement> lastDef;

	static {
		Map<Class<? extends JFugueElement>, JFugueElement> last = new HashMap<Class<? extends JFugueElement>, JFugueElement>();
		last.put(Tempo.class, new Tempo(120));
		last.put(Voice.class, new Voice((byte) 0));
		lastDef = Collections.unmodifiableMap(last);
		;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jfugue.PatternTransformer#initResult(org.jfugue.Pattern)
	 */
	protected PatternInterface initResult(Pattern pattern) {
		elements.clear();
		last.clear();

		// The defaults
		last.putAll(lastDef);

		PatternInterface nPattern = new Pattern();
		nPattern.setMusicString("");
		return nPattern;
	}

}
