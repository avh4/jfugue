package org.jfugue.elements;

@SuppressWarnings("serial")
public class AbstractNote implements JFugueElement {

	protected String noteString;
	protected boolean accompanyingNotes = false;
	protected long duration = 0;
	protected boolean isEndOfTie = false;
	protected boolean isStartOfTie = false;
	protected byte type = 0;

	public String getMusicString() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getVerifyString() {
		// TODO Auto-generated method stub
		return null;
	}

	public void acceptVisitor(ElementVisitor visitor) {
		// TODO Auto-generated method stub

	}
	
	public AbstractNote adjustForKey(KeySignature keySignature) {
		return this;
	}
	
	/**
	 * Returns the note type - either First, Sequential, or Parallel.
	 * @return the note type
	 */
	public byte getType() {
	    return this.type;
	}

	/**
	 * Returns whether this Note will have other Notes (sequential or parallel) associated with it.
	 */
	public boolean hasAccompanyingNotes() {
	    return this.accompanyingNotes;
	}

	/**
	 * @return the accompanyingNotes
	 */
	public boolean isAccompanyingNotes() {
		return accompanyingNotes;
	}

	/**
	 * Returns whether this note is tied to some past note.
	 * @return true is the note is tied, false if not
	 */
	public boolean isEndOfTie() {
	    return this.isEndOfTie;
	}

	/**
	 * Returns whether this note has a tie to some future note.
	 * @return true is the note is tied, false if not
	 */
	public boolean isStartOfTie() {
	    return this.isStartOfTie;
	}

}
