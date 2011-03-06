/**
 * 
 */
package org.jfugue.visitors;

import org.jfugue.elements.*;
import org.jfugue.ParserListener;

/**
 * @author joshua
 *
 */
public class VisitorToListenerAdaptor extends DefaultElementVisitor {

	protected ParserListener listener;
	
	public VisitorToListenerAdaptor(ParserListener listener) {
		this.listener = listener;
	}

	public void visit(ChannelPressure channelPressure) {
		listener.channelPressureEvent(channelPressure);
	}

	public void visit(Controller controller) {
		listener.controllerEvent(controller);
	}

	public void visit(Instrument instrument) {
		listener.instrumentEvent(instrument);
	}

	public void visit(KeySignature keySignature) {
		listener.keySignatureEvent(keySignature);
	}

	public void visit(Layer layer) {
		listener.layerEvent(layer);
	}

	public void visit(Measure measure) {
		listener.measureEvent(measure);
	}

	public void visit(PitchBend pitchBend) {
		listener.pitchBendEvent(pitchBend);
	}

	public void visit(Voice voice) {
		listener.voiceEvent(voice);
	}

	public void visit(SystemExclusive systemExclusiveEvent) {
		listener.systemExclusiveEvent(systemExclusiveEvent);
	}

	public void visit(Tempo tempo) {
		listener.tempoEvent(tempo);
	}

	public void visit(Time time) {
		listener.timeEvent(time);
	}

	public void visit(PolyphonicPressure polyphonicPressure) {
		listener.polyphonicPressureEvent(polyphonicPressure);
	}

	public void visit(Note note) {
		switch (note.getType()) {
		case FIRST:
			listener.noteEvent(note);
			break;
		case SEQUENTIAL:
			listener.sequentialNoteEvent(note);
			break;
		case PARALLEL:
			listener.parallelNoteEvent(note);
			break;
		default:
			break;
		}
	}

}
