package org.jfugue.elements;

import org.jfugue.JFugueDefinitions;

@SuppressWarnings("serial")
public abstract class AbstractNote implements JFugueElement {

	public static final double DEFAULT_DECIMAL_DURATION = JFugueDefinitions.DEFAULT_NOTE_DECIMAL_DURATION;
	public static final long DEFAULT_MS_DURATION = JFugueDefinitions.DEFAULT_NOTE_MS_DURATION;
	
	public enum NoteTypes {
		FIRST, SEQUENTIAL, PARALLEL
	}
	
	protected String noteString;
	protected boolean accompanyingNotes = false;
	protected double decimalDuration = DEFAULT_DECIMAL_DURATION;
	protected long msDuration = DEFAULT_MS_DURATION;
	protected boolean isEndOfTie = false;
	protected boolean isStartOfTie = false;
	protected NoteTypes type = NoteTypes.FIRST;

	public AbstractNote adjustForKey(KeySignature keySignature) {
		return this;
	}
	
	
	public double getDecimalDuration() {
		return decimalDuration;
	}

	public void setDecimalDuration(double decimalDuration) {
		this.decimalDuration = decimalDuration;
		msDuration = JFugueDefinitions.msFromDecimalDuration(decimalDuration);
	}

	public long getMsDuration() {
		return msDuration;
	}

	public void setMsDuration(long msDuration) {
		this.msDuration = msDuration;
		decimalDuration = JFugueDefinitions.decimalFromMSDuration(msDuration);
	}

	/**
	 * Returns the note type - either First, Sequential, or Parallel.
	 * @return the note type
	 */
	public NoteTypes getType() {
	    return this.type;
	}
	
	public void setType(NoteTypes type) {
		this.type = type;
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
	
    /**
     * Returns the decimal duration that is equal to the given MusicString representation.
     * This code currently only converts single duration values representing whole, half,
     * quarter, eighth, etc. durations; and dotted durations associated with those
     * durations (such as "h.", equal to 0.75).  This method does not convert
     * combined durations (for example, "hi" for 0.625) or anything greater
     * than a duration of 1.0 (for example, "wwww" for 4.0).  For these values,
     * the original decimal duration is returned in a string, prepended with a "/"
     * to make the returned value a valid MusicString duration indicator.
     *
     * @param stringDuration The MusicString duration character (or dotted character)
     * @return a decimal value representing the duration, expressed as a fraction of a whole note
     */
    public static double getDecimalForDuration(String stringDuration)
    {
        String stringDuration2 = stringDuration.toLowerCase();
        if (stringDuration2.equals("w")) return 1.0;
        else if (stringDuration2.equals("h.")) return 0.75;
        else if (stringDuration2.equals("h")) return 0.5;
        else if (stringDuration2.equals("q.")) return 0.375;
        else if (stringDuration2.equals("q")) return 0.25;
        else if (stringDuration2.equals("i.")) return 0.1875;
        else if (stringDuration2.equals("i")) return 0.125;
        else if (stringDuration2.equals("s.")) return 0.09375;
        else if (stringDuration2.equals("s")) return 0.0625;
        else if (stringDuration2.equals("t.")) return 0.046875;
        else if (stringDuration2.equals("t")) return 0.03125;
        else if (stringDuration2.equals("x.")) return 0.0234375;
        else if (stringDuration2.equals("x")) return 0.015625;
        else if (stringDuration2.equals("o.")) return 0.01171875;
        else if (stringDuration2.equals("o")) return 0.0078125;
        else return 0.0;
    }

    /**
     * Returns a MusicString representation of a decimal duration.  This code
     * currently only converts single duration values representing whole, half,
     * quarter, eighth, etc. durations; and dotted durations associated with those
     * durations (such as "h.", equal to 0.75).  This method does not convert
     * combined durations (for example, "hi" for 0.625) or anything greater
     * than a duration of 1.0 (for example, "wwww" for 4.0).  For these values,
     * the original decimal duration is returned in a string, prepended with a "/"
     * to make the returned value a valid MusicString duration indicator.
     *
     * @param decimalDuration The decimal value of the duration to convert
     * @return a MusicString fragment representing the duration
     */
    public static String getStringForDuration(double decimalDuration)
    {
        if (decimalDuration == 1.0) return "w";
        else if (decimalDuration == 0.75) return "h.";
        else if (decimalDuration == 0.5) return "h";
        else if (decimalDuration == 0.375) return "q.";
        else if (decimalDuration == 0.25) return "q";
        else if (decimalDuration == 0.1875) return "i.";
        else if (decimalDuration == 0.125) return "i";
        else if (decimalDuration == 0.09375) return "s.";
        else if (decimalDuration == 0.0625) return "s";
        else if (decimalDuration == 0.046875) return "t.";
        else if (decimalDuration == 0.03125) return "t";
        else if (decimalDuration == 0.0234375) return "x.";
        else if (decimalDuration == 0.015625) return "x";
        else if (decimalDuration == 0.01171875) return "o.";
        else if (decimalDuration == 0.0078125) return "o";
        else return "/" + decimalDuration;
    }
    
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (accompanyingNotes ? 1231 : 1237);
		long temp;
		temp = Double.doubleToLongBits(decimalDuration);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (isEndOfTie ? 1231 : 1237);
		result = prime * result + (isStartOfTie ? 1231 : 1237);
		result = prime * result
				+ ((noteString == null) ? 0 : noteString.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}


	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractNote))
			return false;
		AbstractNote other = (AbstractNote) obj;
		if (accompanyingNotes != other.accompanyingNotes)
			return false;
		if (Double.doubleToLongBits(decimalDuration) != Double
				.doubleToLongBits(other.decimalDuration))
			return false;
		if (isEndOfTie != other.isEndOfTie)
			return false;
		if (isStartOfTie != other.isStartOfTie)
			return false;
		if (noteString == null) {
			if (other.noteString != null)
				return false;
		} else if (!noteString.equals(other.noteString))
			return false;
		if (type != other.type)
			return false;
		return true;
	}


	public void setAccompanyingNotes(boolean accompanyingNotes) {
		this.accompanyingNotes = accompanyingNotes;
	}


	public void setEndOfTie(boolean isEndOfTie) {
		this.isEndOfTie = isEndOfTie;
	}


	public void setStartOfTie(boolean isStartOfTie) {
		this.isStartOfTie = isStartOfTie;
	}

}
