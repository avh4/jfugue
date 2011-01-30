package org.jfugue;

public interface ElementVisitor {
	public void visitChannelPressure(ChannelPressure channelPressure);

	public void visitController(Controller controller);

	public void visitInstrument(Instrument instrument);

	public void visitKeySignature(KeySignature keySignature);

	public void visitLayer(Layer layer);

	public void visitMeasure(Measure measure);

	public void visitPattern(Pattern pattern);

	public void visitPitchBend(PitchBend pitchBend);

	public void visitVoice(Voice voice);

	public void visitSystemExclusiveEvent(
			SystemExclusiveEvent systemExclusiveEvent);

	public void visitTempo(Tempo tempo);

	public void visitTime(Time time);

	public void visitPolyphonicPressure(PolyphonicPressure polyphonicPressure);

	public void visitNote(Note note);

	// public void visitChord(Chord chord);
	public void visitParallelNote(Note n);

	public void visitSequentialNote(Note note);
}
