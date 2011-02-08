package org.jfugue.extras;

import org.jfugue.DefaultElementVistior;
import org.jfugue.ParserListener;
import org.jfugue.SystemExclusiveEvent;
import org.jfugue.elements.ChannelPressure;
import org.jfugue.elements.Controller;
import org.jfugue.elements.ElementVisitor;
import org.jfugue.elements.Instrument;
import org.jfugue.elements.KeySignature;
import org.jfugue.elements.Layer;
import org.jfugue.elements.Measure;
import org.jfugue.elements.Note;
import org.jfugue.elements.PitchBend;
import org.jfugue.elements.PolyphonicPressure;
import org.jfugue.elements.Tempo;
import org.jfugue.elements.Time;
import org.jfugue.elements.Voice;

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
