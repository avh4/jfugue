/*
 * JFugue - API for Music Programming
 * Copyright (C) 2003-2008  David Koelle
 *
 * http://www.jfugue.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */

package org.jfugue;

import java.util.Map;

/**
 * This class is used to transform a pattern. Extend this class to create your
 * own PatternTransformer, which listens to parser events and can modify the
 * events that are fired off by the parser. Some sample PatternTransformer
 * subclasses are packaged with JFugue; refer to those to see examples of
 * transformers in action.
 * 
 * This feature is covered in detail in The Complete Guide to JFugue.
 * 
 * @see org.jfugue.extras.DiatonicIntervalPatternTransformer
 * @see org.jfugue.extras.DurationPatternTransformer
 * @see org.jfugue.extras.IntervalPatternTransformer
 * @see org.jfugue.extras.ReversePatternTransformer
 * @author David Koelle
 * @version 2.0
 */
public class PatternTransformer extends PatternTool<PatternInterface> {

	/**
	 * Transforms the pattern, according to the event method that you have
	 * presumably extended. Just a conveince method to
	 * {@link PatternTool#execute(Pattern)}.
	 * 
	 * @see PatternTool#execute(Pattern)
	 */
	public PatternInterface transform(Pattern pattern) {
		return execute(pattern);
		// setReturnPattern(new Pattern());
		// MusicStringParser parser = new MusicStringParser();
		// parser.addParserListener(this);
		// try {
		// parser.parse(p);
		// } catch (JFugueException e)
		// {
		// e.printStackTrace();
		// }
		// return getReturnPattern();
	}

	/** Extend this method to make your transformer modify the voice. */
	@Override
	public void voiceEvent(Voice voice) {
		getResult().addElement(voice);
	}

	/** Extend this method to make your transformer modify the tempo. */
	@Override
	public void tempoEvent(Tempo tempo) {
		getResult().addElement(tempo);
	}

	/** Extend this method to make your transformer modify the instrument. */
	@Override
	public void instrumentEvent(Instrument instrument) {
		getResult().addElement(instrument);
	}

	/** Extend this method to make your transformer modify the layer. */
	@Override
	public void layerEvent(Layer layer) {
		getResult().addElement(layer);
	}

	/** Extend this method to make your transformer modify the time. */
	@Override
	public void timeEvent(Time time) {
		getResult().addElement(time);
	}

	/** Extend this method to make your transformer modify the key signature. */
	@Override
	public void keySignatureEvent(KeySignature keySig) {
		getResult().addElement(keySig);
	}

	/** Extend this method to make your transformer modify sysex events. */
	@Override
	public void systemExclusiveEvent(SystemExclusiveEvent sysex) {
		getResult().addElement(sysex);
	}

	/** Extend this method to make your transformer modify the measure. */
	@Override
	public void measureEvent(Measure measure) {
		getResult().addElement(measure);
	}

	/**
	 * Extend this method to make your transformer modify the controller
	 * messages.
	 */
	@Override
	public void controllerEvent(Controller controller) {
		getResult().addElement(controller);
	}

	/**
	 * Extend this method to make your transformer modify the channel pressure
	 * messages.
	 */
	@Override
	public void channelPressureEvent(ChannelPressure channelPressure) {
		getResult().addElement(channelPressure);
	}

	/**
	 * Extend this method to make your transformer modify the polyphonic
	 * pressure messages.
	 */
	@Override
	public void polyphonicPressureEvent(PolyphonicPressure polyphonicPressure) {
		getResult().addElement(polyphonicPressure);
	}

	/**
	 * Extend this method to make your transformer modify the pitch bend
	 * messages.
	 */
	@Override
	public void pitchBendEvent(PitchBend pitchBend) {
		getResult().addElement(pitchBend);
	}

	/**
	 * Extend this method to make your transformer modify the note. Don't forget
	 * to also extend sequentialNoteEvent and parallelNoteEvent.
	 */
	@Override
	public void noteEvent(Note note) {
		getResult().addElement(note);
	}

	/**
	 * Extend this method to make your transformer modify the note. Don't forget
	 * to also extend noteEvent and parallelNoteEvent.
	 */
	@Override
	public void sequentialNoteEvent(Note note) {
		getResult().addElement(note);
	}

	/**
	 * Extend this method to make your transformer modify the note. Don't forget
	 * to also extend noteEvent and sequentialNoteEvent.
	 */
	@Override
	public void parallelNoteEvent(Note note) {
		getResult().addElement(note);
	}

	@Override
	protected PatternInterface initResult(PatternInterface pattern) {
		PatternInterface nPattern = new Pattern();
		for (Map.Entry<String, String> entries: pattern.getProperties().entrySet()) {
			nPattern.setProperty(entries.getKey(), entries.getValue());
		}
		return nPattern;
	}

	/**
	 * Returns the pattern that the transformer is modifying
	 * 
	 * @version 4.0
	 * @deprecated
	 * @see PatternTool#getResult()
	 */
	@Deprecated
	protected PatternInterface getReturnPattern() {
		return getResult();
	}

	/**
	 * Sets the pattern that the transformer is modifying
	 * 
	 * @version 4.0
	 * @deprecated
	 * @see PatternTool#setResult(Object)
	 */
	@Deprecated
	protected void setReturnPattern(Pattern pattern) {
		setResult(pattern);
	}
}
