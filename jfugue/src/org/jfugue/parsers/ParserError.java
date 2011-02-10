package org.jfugue.parsers;

import org.jfugue.JFugueException;
import org.jfugue.Messages;

@SuppressWarnings("serial")
public class ParserError extends JFugueException {

	public static final String CHAR_NOT_START = Messages.getString("ParserError.CHAR_NOT_START"); //$NON-NLS-1$
	public static final String CHAR_UNEXPECTED = Messages.getString("ParserError.CHAR_UNEXPECTED"); //$NON-NLS-1$
	public static final String INVERSION_EXC = Messages.getString("ParserError.INVERSION_EXC"); //$NON-NLS-1$
	public static final String NOT_READY = Messages.getString("ParserError.NOT_READY"); //$NON-NLS-1$
	public static final String NOTE_OCTAVE_EXC = Messages.getString("ParserError.NOTE_OCTAVE_EXC"); //$NON-NLS-1$
	public static final String NOTE_VELOCITY_EXC = Messages.getString("ParserError.NOTE_VELOCITY_EXC"); //$NON-NLS-1$
	public static final String NOTE_EXC = "Note %s is not a valid drum sound name, or is not in the range 0 - 127.";
	public static final String OCTAVE_EXC = "Octave %s is not a number, or is not in the range 0 - 10.";
    public static final String PARSER_SPACES_EXC = "The token %s sent to Parser.parse() contains spaces.  A token is one unit of musical data, and should not contain a space.";
	
	protected Object[] args = null;
	
	public ParserError() {
		super();
	}
	
	public ParserError(String arg0) {
		super(arg0);
	}
	
	public ParserError(String format, Object...objects) {
		super(format, objects);
//		args = objects;
	}

//	public ParserError(String description, Throwable arg1) {
//		super(description, arg1);
//	}
//
//	public ParserError(Throwable arg0) {
//		super(arg0);
//	}
//
//	public ParserError(Throwable throwable, String format, Object...objects) {
//		super(format, throwable);
//		args = objects;
//	}
//
//	public String toString() {
//		if (args != null)
//			return this.getClass().getName() + ": " + String.format(getLocalizedMessage(), args); //$NON-NLS-1$
//		else
//			return super.toString();
//	}
}
