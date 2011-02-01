package org.jfugue;

public interface ElementVisitor {
	
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

	public void visit(SystemExclusiveEvent systemExclusiveEvent);
//	public void visitSystemExclusiveEvent(
//			SystemExclusiveEvent systemExclusiveEvent);

	public void visit(Tempo tempo);
//	public void visitTempo(Tempo tempo);

	public void visit(Time time);
//	public void visitTime(Time time);

	public void visit(PolyphonicPressure polyphonicPressure);
//	public void visitPolyphonicPressure(PolyphonicPressure polyphonicPressure);

	public void visit(Note note);
//	public void visitNote(Note note);

	// public void visitChord(Chord chord);
//	public void visitParallelNote(Note n);
//
//	public void visitSequentialNote(Note note);
}
