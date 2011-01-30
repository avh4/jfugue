package org.jfugue.extras;

import org.jfugue.ChannelPressure;
import org.jfugue.Controller;
import org.jfugue.ElementVisitor;
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

public class ListenerToVisitorAdaptor implements ParserListener {

	protected ElementVisitor visitor;
	
	/**
	 * @return the visitor
	 */
	public ElementVisitor getVisitor() {
		return visitor;
	}

	/**
	 * @param visitor the visitor to set
	 */
	public void setVisitor(ElementVisitor visitor) {
		this.visitor = visitor;
	}

	public ListenerToVisitorAdaptor(ElementVisitor visitor) {
		this.visitor = visitor;
	}
	
	public void voiceEvent(Voice voice) {
		visitor.visitVoice(voice);
	}

	public void tempoEvent(Tempo tempo) {
		visitor.visitTempo(tempo);
	}

	public void instrumentEvent(Instrument instrument) {
		visitor.visitInstrument(instrument);
	}

	public void layerEvent(Layer layer) {
		visitor.visitLayer(layer);
	}

	public void measureEvent(Measure measure) {
		visitor.visitMeasure(measure);
	}

	public void timeEvent(Time time) {
		visitor.visitTime(time);
	}

	public void keySignatureEvent(KeySignature keySig) {
		visitor.visitKeySignature(keySig);
	}

	public void systemExclusiveEvent(SystemExclusiveEvent sysex) {
		visitor.visitSystemExclusiveEvent(sysex);
	}

	public void controllerEvent(Controller controller) {
		visitor.visitController(controller);
	}

	public void channelPressureEvent(ChannelPressure channelPressure) {
		visitor.visitChannelPressure(channelPressure);
	}

	public void polyphonicPressureEvent(PolyphonicPressure polyphonicPressure) {
		visitor.visitPolyphonicPressure(polyphonicPressure);
	}

	public void pitchBendEvent(PitchBend pitchBend) {
		visitor.visitPitchBend(pitchBend);
	}

	public void noteEvent(Note note) {
		visitor.visitNote(note);
	}

	public void sequentialNoteEvent(Note note) {
		visitor.visitSequentialNote(note);
	}

	public void parallelNoteEvent(Note note) {
		visitor.visitParallelNote(note);
	}

}
