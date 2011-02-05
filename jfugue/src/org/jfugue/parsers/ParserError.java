package org.jfugue.parsers;

@SuppressWarnings("serial")
public class ParserError extends Exception {

	public static final String CHAR_UNEXPECTED = "The character '%s' was unexpected while parsing %s";
	public static final String NOT_READY = "The reader is not ready for some reason";
	public static final String CHAR_NOT_START = "'%s' is not the start of an %s";
	
	protected Object[] args = null;
	
	public ParserError() {
		super();
	}
	
	public ParserError(String format, Object...objects) {
		super(format);
		args = objects;
	}
	
	public ParserError(Throwable throwable, String format, Object...objects) {
		super(format, throwable);
		args = objects;
	}

	public ParserError(String arg0) {
		super(arg0);
	}

	public ParserError(Throwable arg0) {
		super(arg0);
	}

	public ParserError(String description, Throwable arg1) {
		super(description, arg1);
	}

	public String toString() {
		if (args != null)
			return this.getClass().getName() + ": " + String.format(getLocalizedMessage(), args);
		else
			return super.toString();
	}
}
