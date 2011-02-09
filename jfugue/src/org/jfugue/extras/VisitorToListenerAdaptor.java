/**
 * 
 */
package org.jfugue.extras;

import org.jfugue.elements.ChannelPressure;
import org.jfugue.elements.Chord;
import org.jfugue.elements.Controller;
import org.jfugue.elements.ElementVisitor;
import org.jfugue.elements.Instrument;
import org.jfugue.elements.KeySignature;
import org.jfugue.elements.Layer;
import org.jfugue.elements.Measure;
import org.jfugue.elements.Note;
import org.jfugue.elements.NoteCollection;
import org.jfugue.elements.PitchBend;
import org.jfugue.elements.PolyphonicPressure;
import org.jfugue.elements.SystemExclusive;
import org.jfugue.elements.Tempo;
import org.jfugue.elements.Time;
import org.jfugue.elements.Voice;
import org.jfugue.elements.Note.Parallel;
import org.jfugue.elements.Note.Sequential;
import org.jfugue.ParserListener;
import org.jfugue.Pattern;

/**
 * @author joshua
 *
 */
public class VisitorToListenerAdaptor implements ElementVisitor {

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

	public void visit(Pattern pattern) {
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
		case Note.FIRST:
			listener.noteEvent(note);
			break;
		case Note.SEQUENTIAL:
			listener.sequentialNoteEvent(note);
			break;
		case Note.PARALLEL:
			listener.parallelNoteEvent(note);
			break;
		default:
			break;
		}
	}

	public void visitParallelNote(Note n) {
		// TODO Auto-generated method stub
		
	}

	public void visitSequentialNote(Note note) {
		// TODO Auto-generated method stub
		
	}

	public void visit(Chord chord) {
		// TODO Auto-generated method stub
		
	}

	public void visit(NoteCollection collection) {
		// TODO Auto-generated method stub
		
	}

	public void visit(Parallel parallel) {
		// TODO Auto-generated method stub
		
	}

	public void visit(Sequential sequential) {
		// TODO Auto-generated method stub
		
	}
	

}
