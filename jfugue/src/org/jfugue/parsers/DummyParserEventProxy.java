package org.jfugue.parsers;

import org.jfugue.ChannelPressure;
import org.jfugue.Controller;
import org.jfugue.Instrument;
import org.jfugue.KeySignature;
import org.jfugue.Layer;
import org.jfugue.Measure;
import org.jfugue.Note;
import org.jfugue.ParserListener;
import org.jfugue.PitchBend;
import org.jfugue.PolyphonicPressure;
import org.jfugue.SystemExclusiveEvent;
import org.jfugue.Tempo;
import org.jfugue.Time;
import org.jfugue.Voice;

public class DummyParserEventProxy implements org.jfugue.parsers.FireParserEventProxy {

	public void addParserListener(ParserListener listener) {
		// TODO Auto-generated method stub

	}

	public void removeParserListener(ParserListener listener) {
		// TODO Auto-generated method stub

	}

	public ParserListener[] getParserListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	public void clearParserListeners() {
		// TODO Auto-generated method stub

	}

	public void fireVoiceEvent(Voice event) {
		// TODO Auto-generated method stub

	}

	public void fireTempoEvent(Tempo event) {
		// TODO Auto-generated method stub

	}

	public void fireInstrumentEvent(Instrument event) {
		// TODO Auto-generated method stub

	}

	public void fireLayerEvent(Layer event) {
		// TODO Auto-generated method stub

	}

	public void fireTimeEvent(Time event) {
		// TODO Auto-generated method stub

	}

	public void fireSystemExclusiveEvent(SystemExclusiveEvent event) {
		// TODO Auto-generated method stub

	}

	public void fireKeySignatureEvent(KeySignature event) {
		// TODO Auto-generated method stub

	}

	public void fireMeasureEvent(Measure event) {
		// TODO Auto-generated method stub

	}

	public void fireControllerEvent(Controller event) {
		// TODO Auto-generated method stub

	}

	public void fireChannelPressureEvent(ChannelPressure event) {
		// TODO Auto-generated method stub

	}

	public void firePolyphonicPressureEvent(PolyphonicPressure event) {
		// TODO Auto-generated method stub

	}

	public void firePitchBendEvent(PitchBend event) {
		// TODO Auto-generated method stub

	}

	public void fireNoteEvent(Note event) {
		// TODO Auto-generated method stub

	}

	public void fireSequentialNoteEvent(Note event) {
		// TODO Auto-generated method stub

	}

	public void fireParallelNoteEvent(Note event) {
		// TODO Auto-generated method stub

	}

}
