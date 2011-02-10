package org.jfugue.visitors;

import org.jfugue.NoteVisitor;
import org.jfugue.Pattern;
import org.jfugue.elements.ChannelPressure;
import org.jfugue.elements.Controller;
import org.jfugue.elements.Instrument;
import org.jfugue.elements.KeySignature;
import org.jfugue.elements.Layer;
import org.jfugue.elements.Measure;
import org.jfugue.elements.PitchBend;
import org.jfugue.elements.PolyphonicPressure;
import org.jfugue.elements.SystemExclusive;
import org.jfugue.elements.Tempo;
import org.jfugue.elements.Time;
import org.jfugue.elements.Voice;

public interface ElementVisitor extends NoteVisitor {
	
	public void visit(ChannelPressure channelPressure);
//	public void visitChannelPressure(ChannelPressure channelPressure);

	public void visit(Controller controller);
//	public void visitController(Controller controller);

	public void visit(Instrument instrument);
//	public void visitInstrument(Instrument instrument);

	public void visit(KeySignature keySignature);
//	public void visitKeySignature(KeySignature keySignature);

	public void visit(Layer layer);
//	public void visitLayer(Layer layer);

	public void visit(Measure measure);
//	public void visitMeasure(Measure measure);

	public void visit(Pattern pattern);
//	public void visitPattern(Pattern pattern);

	public void visit(PitchBend pitchBend);
//	public void visitPitchBend(PitchBend pitchBend);

	public void visit(Voice voice);
//	public void visitVoice(Voice voice);

	public void visit(SystemExclusive systemExclusiveEvent);
//	public void visitSystemExclusiveEvent(
//			SystemExclusiveEvent systemExclusiveEvent);

	public void visit(Tempo tempo);
//	public void visitTempo(Tempo tempo);

	public void visit(Time time);
//	public void visitTime(Time time);

	public void visit(PolyphonicPressure polyphonicPressure);
//	public void visitPolyphonicPressure(PolyphonicPressure polyphonicPressure);

//	public void visit(Note note);
////	public void visitNote(Note note);
//
//	public void visit(Chord chord);
//	public void visit(NoteCollection collection);
//	public void visit(Note.Parallel parallel);
//	public void visit(Note.Sequential sequential);
}
