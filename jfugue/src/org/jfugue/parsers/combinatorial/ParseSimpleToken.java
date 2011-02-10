package org.jfugue.parsers.combinatorial;

import java.io.IOException;
import java.util.Arrays;

import org.jfugue.JFugueException;
import org.jfugue.parsers.ParserContext;
import org.jfugue.parsers.ParserError;

public class ParseSimpleToken implements ParserNode<String> {

	protected String[] strings;
	protected char[] delimeters;
	protected boolean consumeDelimiter;
	
	/**
	 * @param strings
	 * @param delimeters
	 * @param consumeDelimiter
	 */
	public ParseSimpleToken(String[] strings, char[] delimeters,
			boolean consumeDelimiter) {
		super();
		this.strings = Arrays.copyOf(strings, strings.length);
		this.delimeters = Arrays.copyOf(delimeters, delimeters.length);
		this.consumeDelimiter = consumeDelimiter;
	}

	/**
	 * @param strings
	 * @param delimeters
	 * @param consumeDelimiter
	 */
	public ParseSimpleToken(boolean consumeDelimiter, char[] delimeters, String...strings) {
		this(strings, delimeters, consumeDelimiter);
	}
	
	/**
	 * @param strings
	 * @param consumeDelimiter
	 */
	public ParseSimpleToken(boolean consumeDelimiter, String...strings) {
		this(strings, defaultDelims(), consumeDelimiter);
	}
	
	/**
	 * @param strings
	 * @param delimeters
	 */
	public ParseSimpleToken(char[] delimeters, String...strings) {
		this(strings, delimeters, false);
	}

	/**
	 * @param strings
	 */
	public ParseSimpleToken(String...strings) {
		this(strings, defaultDelims(), false);
	}
	
	private static char[] defaultDelims() {
		return new char[] { ' ' };
	}

	public String execute(ParserContext context) throws ParserError,
			IOException, JFugueException {
		String token = context.readToken(consumeDelimiter, delimeters);
		for (String string : strings) {
			if (string.equals(token))
				return token;
		}
		throw new ParserError("Tokens %s not found", strings.toString());
	}

}
