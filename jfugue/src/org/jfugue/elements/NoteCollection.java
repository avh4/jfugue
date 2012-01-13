/**
 * 
 */
package org.jfugue.elements;

import java.util.List;

import org.jfugue.factories.NoteFactory.NoteContext;
import org.jfugue.visitors.ElementVisitor;

/**
 * @author joshua
 *
 */
@SuppressWarnings("serial")
public class NoteCollection extends Note {

	protected List<Note> notes;

	/**
	 * @param c
	 */
	public NoteCollection(NoteContext c, List<Note> notes) {
		this.notes = notes;
	}

	/* (non-Javadoc)
	 * @see org.jfugue.Note#acceptVisitor(org.jfugue.ElementVisitor)
	 */
	public void acceptVisitor(ElementVisitor visitor) {
		for (Note note : notes) {
			visitor.visit(note);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof NoteCollection))
			return false;
		NoteCollection other = (NoteCollection) obj;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jfugue.Note#getMusicString()
	 */
	public String getMusicString() {
		int cap = 0;
		for (Note n : notes) {
			cap += n.getMusicString().length() + 1;
		}
		StringBuilder sb = new StringBuilder(cap);
		for (Note n : notes) {
			switch (n.getType()) {
			case FIRST:	break;
			case SEQUENTIAL:
				sb.append('_');
				break;
			case PARALLEL:
				sb.append('+');
				break;
			default:
				break;
			}
			sb.append(n.getMusicString());
		}
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see org.jfugue.Note#getVerifyString()
	 */
	public String getVerifyString() {
		// TODO Auto-generated method stub
		return super.getVerifyString();
	}
	
}
