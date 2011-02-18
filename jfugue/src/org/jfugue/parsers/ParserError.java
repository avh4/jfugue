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
	public static final String NOTE_EXC = Messages.getString("ParserError.NOTE_EXC"); //$NON-NLS-1$
	public static final String OCTAVE_EXC = Messages.getString("ParserError.OCTAVE_EXC"); //$NON-NLS-1$
    public static final String PARSER_SPACES_EXC = Messages.getString("ParserError.PARSER_SPACES_EXC"); //$NON-NLS-1$
	public static final String EXPECTED_SYMBOL = "Expected symbol but got %s";
	public static final String PATTERN_NOT_MATCH = "Regex failed: %s";

	public static final String EXPECTED_BYTE   = "The parser expected a byte.";
    public static final String EXPECTED_LONG   = "The parser expected a long.";
    public static final String EXPECTED_INT    = "The parser expected an int.";
    public static final String EXPECTED_DOUBLE = "The parser expected a double.";

    /**
	 * @see JFugueException#JFugueException()
	 */
	public ParserError() {
		super();
	}

	/**
	 * @see JFugueException#JFugueException(String, Object...)
	 */
	public ParserError(String format, Object... objects) {
		super(format, objects);
	}

	/**
	 * @see JFugueException#JFugueException(Throwable, String, Object...)
	 */
	public ParserError(Throwable cause, String format, Object... objects) {
		super(cause, format, objects);
	}

	/**
	 * @see JFugueException#JFugueException(Throwable)
	 */
	public ParserError(Throwable cause) {
		super(cause);
	}
	
	
//	public ParserError() {
//		super();
//	}
//	
//	public ParserError(String arg0) {
//		super(arg0);
//	}
//	
//	public ParserError(String format, Object...objects) {
//		super(format, objects);
//	}

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
