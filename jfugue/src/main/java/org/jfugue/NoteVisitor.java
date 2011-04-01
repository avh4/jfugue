package org.jfugue;

import org.jfugue.elements.Chord;
import org.jfugue.elements.Note;
import org.jfugue.elements.NoteCollection;

public interface NoteVisitor {

	public void visit(Note note);

	public void visit(Chord chord);

	public void visit(NoteCollection collection);

	public void visit(Note.Parallel parallel);

	public void visit(Note.Sequential sequential);
}
