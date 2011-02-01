package org.jfugue.extras;

import org.jfugue.ChannelPressure;
import org.jfugue.Controller;
import org.jfugue.DefaultElementVistior;
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

public class ListenerToVisitorAdaptor extends DefaultElementVistior implements ParserListener {

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
		visitor.visit(voice);
	}

	public void tempoEvent(Tempo tempo) {
		visitor.visit(tempo);
	}

	public void instrumentEvent(Instrument instrument) {
		visitor.visit(instrument);
	}

	public void layerEvent(Layer layer) {
		visitor.visit(layer);
	}

	public void measureEvent(Measure measure) {
		visitor.visit(measure);
	}

	public void timeEvent(Time time) {
		visitor.visit(time);
	}

	public void keySignatureEvent(KeySignature keySig) {
		visitor.visit(keySig);
	}

	public void systemExclusiveEvent(SystemExclusiveEvent sysex) {
		visitor.visit(sysex);
	}

	public void controllerEvent(Controller controller) {
		visitor.visit(controller);
	}

	public void channelPressureEvent(ChannelPressure channelPressure) {
		visitor.visit(channelPressure);
	}

	public void polyphonicPressureEvent(PolyphonicPressure polyphonicPressure) {
		visitor.visit(polyphonicPressure);
	}

	public void pitchBendEvent(PitchBend pitchBend) {
		visitor.visit(pitchBend);
	}

	public void noteEvent(Note note) {
		visitor.visit(note);
	}

	public void sequentialNoteEvent(Note note) {
		visitor.visit(note);
	}

	public void parallelNoteEvent(Note note) {
		visitor.visit(note);
	}

}
