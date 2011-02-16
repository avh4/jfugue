package org.jfugue;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.jfugue.elements.JFugueElement;
import org.jfugue.extras.ReversePatternTransformer;
import org.jfugue.visitors.ElementVisitor;

public interface PatternInterface extends JFugueElement {

	public static final String TITLE = "Title";

	/**
	 * Sets the music string kept by this pattern.
	 * @param musicString the music string
	 */
	public abstract void setMusicString(String musicString);

	/**
	 * Inserts a MusicString before this music string.
	 * NOTE - this does not call fragmentAdded!
	 * @param musicString the string to insert
	 */
	public abstract void insert(String musicString);

	/**
	 * Adds an additional pattern to the end of this pattern.
	 * @param pattern the pattern to add
	 */
	public abstract void add(Pattern pattern);

	/**
	 * Adds a music string to the end of this pattern.
	 * @param musicString the music string to add
	 */
	public abstract void add(String musicString);

	/**
	 * Adds an additional pattern to the end of this pattern.
	 * @param pattern the pattern to add
	 * @param numTimes the number of times to add it
	 */
	public abstract void add(Pattern pattern, int numTimes);

	/**
	 * Adds a music string to the end of this pattern.
	 * @param musicString the music string to add
	 * @param numTimes the number of times to add it
	 */
	public abstract void add(String musicString, int numTimes);

	/**
	 * Adds a number of patterns sequentially
	 * 
	 * @param musicStrings the music string to add
	 */
	public abstract void add(String... musicStrings);

	/**
	 * Adds the {@link JFugueElement}s to the pattern.
	 * 
	 * @param elements
	 */
	public abstract void add(JFugueElement... elements);

	/**
	 * Adds an individual element to the pattern.  This takes into
	 * account the possibility that the element may be a sequential or
	 * parallel note, in which case no space is placed before it.
	 * 
	 * @param element the element to add
	 */
	public abstract void addElement(JFugueElement element);

	/**
	 * Sets the title for this Pattern.
	 * As of JFugue 4.0, the title is set as a property with the key Pattern.TITLE
	 * @param title the title for this Pattern
	 */
	public abstract void setTitle(String title);

	/**
	 * Returns the title of this Pattern
	 * As of JFugue 4.0, the title is set as a property with the key Pattern.TITLE
	 * @return the title of this Pattern
	 */
	public abstract String getTitle();

	/**
	 * Get a property on this pattern, such as "author" or "date".
	 */
	public abstract String getProperty(String key);

	/**
	 * Set a property on this pattern, such as "author" or "date".
	 */
	public abstract void setProperty(String key, String value);

	/**
	 * Get all properties set on this pattern, such as "author" or "date".
	 */
	public abstract Map<String, String> getProperties();

	/**
	 * Returns a String containing key-value pairs stored in this object's properties,
	 * separated by semicolons and spaces.
	 * Values are returned in the following form:
	 * key1: value1; key2: value2; key3: value3
	 *
	 * @return a String containing key-value pairs stored in this object's properties, separated by semicolons and spaces
	 */
	public abstract String getPropertiesAsSentence();

	/**
	 * Returns a String containing key-value pairs stored in this object's properties,
	 * separated by newline characters.
	 *
	 * Values are returned in the following form:
	 * key1: value1\n
	 * key2: value2\n
	 * key3: value3\n
	 *
	 * @return a String containing key-value pairs stored in this object's properties, separated by newline characters
	 */
	public abstract String getPropertiesAsParagraph();

	/**
	 * Changes all timestamp values by the offsetTime passed in.
	 * NOTE: This method is only useful for patterns that have been converted from a MIDI file
	 * (i.e., that have time tokens, indicated with an @ sign).
	 * @param offsetTime
	 */
	public abstract void offset(long offsetTime);

	/**
	 * Repeats the music string in this pattern
	 * by the given number of times.
	 * Example: If the pattern is "A B", calling <code>repeat(4)</code> will
	 * make the pattern "A B A B A B A B".
	 */
	public abstract void repeat(int times);

	/**
	 * Only repeats the portion of this music string
	 * that starts at the string index
	 * provided.  This allows some initial header information to only be specified
	 * once in a repeated pattern.
	 * Example: If the pattern is "T0 A B", calling <code>repeat(4, 3)</code> will
	 * make the pattern "T0 A B A B A B A B".
	 * 
	 * In Version 4.1, this is fixed to work on the index of the token in the pattern, 
	 * as opposed to the index of the string.
	 *   
	 **/
	public abstract void repeat(int times, int beginIndex);

	/**
	 * Only repeats the portion of this music string
	 * that starts and ends at the
	 * string indices provided.  This allows some initial header information and
	 * trailing information to only be specified once in a repeated pattern.
	 * Example: If the pattern is "T0 A B C", calling <code>repeat(4, 3, 5)</code>
	 * will make the pattern "T0 A B A B A B A B C".
	 * 
	 * In Version 4.1, this is fixed to work on the index of the token in the pattern, 
	 * as opposed to the index of the string.
	 *   
	 */
	public abstract void repeat(int times, int beginIndex, int endIndex);

	/**
	 * Returns a new Pattern that is a subpattern of this pattern.
	 * @return subpattern of this pattern.
	 * 
	 * Version 4.1.0 improves on previous versions of this method by
	 * returning tokens instead of substring of the pattern.
	 */
	public abstract PatternInterface getSubPattern(int beginIndex);

	/**
	 * Returns a new Pattern that is a subpattern of this pattern.
	 * @return subpattern of this pattern
	 * 
	 * Version 4.1.0 improves on previous versions of this method by
	 * returning tokens instead of substring of the pattern.
	 */
	public abstract PatternInterface getSubPattern(int beginIndex, int endIndex);

	/**
	 * Returns a Pattern that replaces the token at the provided index with the new token.
	 * 
	 * Use this if, for example, you have two musical phrases that are very similar except for a few notes.
	 * 
	 * @param index The index of the token to replace
	 * @param newToken The new token to place at the specified index
	 * @return The new Pattern
	 */
	public abstract PatternInterface replace(int index, String newToken);

	/**
	 * Returns a Pattern that replaces a series of tokens with the new tokens.
	 * 
	 * @param startingIndex The index of the first token to replace
	 * @param newTokens An array of tokens that will be placed into the pattern
	 * @return The new Pattern
	 */
	public abstract PatternInterface replace(int startingIndex, String... newTokens);

	/**
	 * Saves {@code this} to {@code writer} in {@code .jfugue} format.
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public abstract void savePattern(Writer writer) throws IOException;

	/**
	 * Saves {@code this} to {@code out} in {@code .jfugue} format.
	 * 
	 * @param out
	 * @throws IOException
	 */
	public abstract void savePattern(OutputStream out) throws IOException;

	/**
	 * Saves {@code this} to {@code file} in {@code .jfugue} format.
	 * The {@code file} should ideally have an extension of {@code .jfugue}.
	 * 
	 * @param file
	 * @throws IOException
	 */
	public abstract void savePattern(File file) throws IOException;

	/**
	 * Returns an array of strings representing each token in the Pattern.
	 * @return the array of String tokens
	 */
	public abstract String[] getTokens();

	/**
	 * Indicates whether this pattern is composed of valid elements
	 * that can be parsed by the MusicStringParser.
	 * @return whether the musicString is valid
	 */
	public abstract boolean isValid();

	/**
	 */
	public abstract List<Byte> getInstruments();

	/**
	 */
	public abstract Class<ReversePatternTransformer> getReversePatternTransformerClass();

	/**
	 */
	public abstract PatternInterface reverse();

	/**
	 * Adds a <code>PatternListener</code>.  The listener will receive events when new
	 * parts are added to the pattern.
	 *
	 * @param listener the listener that is to be notified when new parts are added to the pattern
	 */
	public abstract void addPatternListener(PatternListener listener);

	/**
	 * Removes a <code>PatternListener</code>.
	 *
	 * @param listener the listener to remove
	 */
	public abstract void removePatternListener(PatternListener listener);

	/**
	 */
	public abstract String toString();

	public abstract void acceptVisitor(ElementVisitor visitor);

}