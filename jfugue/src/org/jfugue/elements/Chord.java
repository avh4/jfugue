package org.jfugue.elements;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jfugue.factories.NoteFactory;

@SuppressWarnings("serial")
public class Chord extends Note {

		protected String fullname;
    	protected String chordName;
    	protected String inversion;
    	protected byte[] halfsteps;
    	protected List<Note> noteList = null;

    	public Chord(NoteFactory.NoteContext c) {
    		super(c);
			fullname = c.getOtoken();
    		chordName = c.getsChord();
    		halfsteps = c.getChord();
    		createNotes(c);
    	}
    	
		public Chord(Chord chord) {
			super(chord);
			fullname = chord.getFullname();
			chordName = chord.getChordName();
			inversion = chord.getInversion();
			halfsteps = chord.getHalfsteps().clone();
			noteList = new LinkedList<Note>();
			for (Note note : chord.getNoteList()) {
				noteList.add(new Note(note));
			}
			noteList = Collections.unmodifiableList(noteList);
		}
		
		private void createNotes(NoteFactory.NoteContext c) {
			noteList = new LinkedList<Note>();
			type = PARALLEL; 
			for (byte hs : halfsteps) {
				Note note = new Note(c);
				note.setValue((byte) (note.getValue() + hs));
				noteList.add(note);
			}
			noteList = Collections.unmodifiableList(noteList);
		}

		public String getInversion() {
			return inversion;
		}

		public byte[] getHalfsteps() {
			return halfsteps;
		}
		
		public List<Note> getNoteList() {
			return noteList;
		}
    	
    	public String getChordName() {
			return chordName;
		}
    	
		/**
		 * @return the fullname
		 */
		public String getFullname() {
			return fullname;
		}

		public String getMusicString() {
			return getFullname();
		}

		public String getVerifyString() {
			return String.format(
					"Chord[name=%s, chord=%s, inversion=%s, halfsteps=%s]",
					getFullname(), getChordName(), getInversion(),
					getHalfsteps());
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + Arrays.hashCode(halfsteps);
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Chord))
				return false;
			if (!((Note) obj).equals(this))
				return false;
			Chord other = (Chord) obj;
			if (!Arrays.equals(halfsteps, other.halfsteps))
				return false;
			return true;
		}

		public void acceptVisitor(ElementVisitor visitor) {
			super.acceptVisitor(visitor);
			for (Note n : getNoteList()) {
				n.acceptVisitor(visitor);
			}
		}

	}