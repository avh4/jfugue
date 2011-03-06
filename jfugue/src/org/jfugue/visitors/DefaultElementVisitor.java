package org.jfugue.visitors;

import org.jfugue.Pattern;
import org.jfugue.elements.ChannelPressure;
import org.jfugue.elements.Chord;
import org.jfugue.elements.CommentProperty;
import org.jfugue.elements.Controller;
import org.jfugue.elements.DictAdd;
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

public class DefaultElementVisitor implements ElementVisitor {

	public void visit(ChannelPressure channelPressure) {
	}

	public void visit(Controller controller) {
	}

	public void visit(Instrument instrument) {
	}

	public void visit(KeySignature keySignature) {
	}

	public void visit(Layer layer) {
	}

	public void visit(Measure measure) {
	}

	public void visit(Pattern pattern) {
	}

	public void visit(PitchBend pitchBend) {
	}

	public void visit(Voice voice) {
	}

	public void visit(SystemExclusive systemExclusiveEvent) {
	}

	public void visit(Tempo tempo) {
	}

	public void visit(Time time) {
	}

	public void visit(PolyphonicPressure polyphonicPressure) {
	}

	public void visit(Note note) {
	}

	public void visitParallelNote(Note n) {
	}

	public void visitSequentialNote(Note note) {
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

	public void visit(DictAdd dictAdd) {
		// TODO Auto-generated method stub
		
	}

	public void visit(CommentProperty property) {
		// TODO Auto-generated method stub
		
	}

}
