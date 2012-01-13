package org.jfugue.elements.interfaces;

public interface ElementVisitor {
	public void visitChannelPressure(ChannelPressure presure);
	public void visitComment(Comment comment);
	public void visitCommentProperty(CommentProperty property);
	public void visitController(Controller controller);
	public void visitDictAdd(DictAdd dictAdd);
	public void visitInstrument(Instrument instrument);
	public void visitKeySignature(KeySignature keySignature);
	public void visitLayer(Layer layer);
	public void visitMeasure(Measure measure);
	public void visitNote(Note note);
	public void visitPitchBend(PitchBend bend);
	public void visitPolyphonicPressure(PolyphonicPressure pressure);
	public void visitSystemExclusive(SystemExclusive systemExclusive);
	public void visitTempo(Tempo tempo);
	public void visitTime(Time time);
	public void visitVoice(Voice voice);
}
