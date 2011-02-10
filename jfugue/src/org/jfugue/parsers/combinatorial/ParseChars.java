package org.jfugue.parsers.combinatorial;

import java.io.IOException;

import org.jfugue.JFugueException;
import org.jfugue.parsers.ParserContext;
import org.jfugue.parsers.ParserError;

public class ParseChars implements ParserNode<String> {

	protected char[] lookingFor = null;
	protected byte[] unicodeTypes = null;
	protected int count = 0;
	protected boolean strict = true;
	
	/**
	 * @param strict do you want it to be strictly {@code count}
	 * @param lookingFor the array of {@code char}s you want, or null to not use this filter
	 * @param unicodeTypes the array of unicode char types you want, or null to not use this filter
	 * @param count the number of {@code chars} you want or -1 if you don't care
	 */
	public ParseChars(boolean strict, char[] lookingFor, byte[] unicodeTypes, int count) {
		super();
		this.strict = strict;
		this.lookingFor = lookingFor;
		this.unicodeTypes = unicodeTypes;
		this.count = count;
	}

	/**
	 * @param count
	 * @param lookingFor
	 */
	public ParseChars(int count, char...lookingFor) {
		this(true, lookingFor, null, count);
	}

	/**
	 * @param count
	 * @param unicodeTypes
	 */
	public ParseChars(int count, byte...unicodeTypes) {
		this(true, null, unicodeTypes, count);
	}
	
	/**
	 * @param lookingFor
	 */
	public ParseChars(char[] lookingFor) {
		this(1, lookingFor);
	}
	
	/**
	 * @param unicodeTypes
	 */
	public ParseChars(byte[] unicodeTypes) {
		this(1, unicodeTypes);
	}

	/**
	 * @param count
	 */
	public ParseChars(int count) {
		this(true, null, null, count);
	}

	public String execute(ParserContext context) throws ParserError,
			IOException, JFugueException {
		return context.readUnicodeAndChars(getCount(), isStrict(), getUnicodeTypes(), getLookingFor());
	}

	/**
	 * @return the lookingFor
	 */
	public char[] getLookingFor() {
		return lookingFor;
	}

	/**
	 * @param lookingFor the lookingFor to set
	 */
	public void setLookingFor(char[] lookingFor) {
		this.lookingFor = lookingFor;
	}

	/**
	 * @return the unicodeTypes
	 */
	public byte[] getUnicodeTypes() {
		return unicodeTypes;
	}

	/**
	 * @param unicodeTypes the unicodeTypes to set
	 */
	public void setUnicodeTypes(byte[] unicodeTypes) {
		this.unicodeTypes = unicodeTypes;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the strict
	 */
	public boolean isStrict() {
		return strict;
	}

	/**
	 * @param strict the strict to set
	 */
	public void setStrict(boolean strict) {
		this.strict = strict;
	}

}
