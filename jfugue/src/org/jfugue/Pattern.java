/*
 * JFugue - API for Music Programming
 * Copyright (C) 2003-2008  David Koelle
 *
 * http://www.jfugue.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */

package org.jfugue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.event.EventListenerList;

import org.jfugue.extras.GetInstrumentsUsedTool;
import org.jfugue.extras.ReversePatternTransformer;

/**
 * This class represents a segment of music.  By representing segments of music
 * as patterns, JFugue gives users the opportunity to play around with pieces
 * of music in new and interesting ways.  Patterns may be added together, transformed,
 * or otherwise manipulated to expand the possibilities of creative music.
 * 
 * TODO Some methods such as <code>repeat</code> may belong elsewhere?
 *
 * @author David Koelle
 * @version 2.0
 * @version 4.0 - Added Pattern Properties
 * @version 4.0.3 - Now implements Serializable
 * @version 4.0.4 - properties and listenerList now use lazy initialization
 * @version 4.1 - Improved substring/replace methods to work on token index, instead of character index
 */
public class Pattern implements JFugueElement
{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 4334276178935154938L;
	private StringBuilder musicString;
    private Map<String, String> properties;  // uses lazy initialization, so access only through getProperties()

    public static final String TITLE = "Title";

	/**
     * Instantiates a new pattern
     */
    public Pattern()
    {
        this("");
    }

    /**
     * Instantiates a new pattern using the given music string
     * @param s the music string
     */
    public Pattern(String musicString)
    {
        setMusicString(musicString);
    }

    /** Copy constructor */
    public Pattern(Pattern pattern)
    {
        this(new String(pattern.getMusicString()));
        Iterator<String> iter = pattern.getProperties().keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            String value = pattern.getProperty(key);
            setProperty(key, value);
        }
    }


    /**
     * This constructor creates a new Pattern that contains each of the given patterns
     * @version 4.0
     * */
    public Pattern(Pattern... patterns)
    {
        this();
        for (Pattern p : patterns) {
            this.add(p);
        }
    }
    
    public Pattern(JFugueElement... elements) {
    	this();
    	add(elements);
    }

    /**
     * 
     * @version 4.0
     * */
    public static Pattern createPattern(Map<String, Pattern> context, Pattern pattern)
    {
    	StringBuilder buddy = new StringBuilder();
    	
    	String[] tokens = pattern.getTokens();
    	for (String token : tokens) {
    		if ((token.charAt(0) == '{') && (token.charAt(token.length()-1) == '}')) {
    			String key = token.substring(1, token.length()-1);
    			Pattern p = context.get(key);
    			buddy.append(p.getMusicString());
    			buddy.append(" ");
    		} else { 
    			buddy.append(token);
    			buddy.append(" ");
    		}
    	}
    	
    	return new Pattern(buddy.toString());
    }
    
    /**
     * 
     * @version 4.0
     * */
    public static Pattern createPattern(Map<String, Pattern> context, Pattern... patterns)
    {
    	
    	Pattern allPatterns = new Pattern();
    	for (Pattern p : patterns) {
    		allPatterns.add(p);
    	}
    	
    	return createPattern(context, allPatterns);
    }
    
    /**
     * Sets the music string kept by this pattern.
     * @param s the music string
     */
    public void setMusicString(String musicString)
    {
        this.musicString = new StringBuilder();
        this.musicString.append(musicString);
    }

    /**
     * Adds to the music string kept by this pattern.
     * @param s the music string to add
     */
    private void appendMusicString(String appendString)
    {
        this.musicString.append(appendString);
    }

    /**
     * Returns the music string kept in this pattern
     * @return the music string
     */
    public String getMusicString()
    {
        return this.musicString.toString();
    }

    /**
     * Inserts a MusicString before this music string.
     * NOTE - this does not call fragmentAdded!
     * @param musicString the string to insert
     */
    public void insert(String musicString)
    {
       this.musicString.insert(0, " ");
       this.musicString.insert(0, musicString);
    }

    /**
     * Adds an additional pattern to the end of this pattern.
     * @param pattern the pattern to add
     */
    public void add(Pattern pattern)
    {
        fireFragmentAdded(pattern);
        appendMusicString(" ");
        appendMusicString(pattern.getMusicString());
    }

    /**
     * Adds a music string to the end of this pattern.
     * @param musicString the music string to add
     */
    public void add(String musicString)
    {
        add(new Pattern(musicString));
    }

    /**
     * Adds an additional pattern to the end of this pattern.
     * @param pattern the pattern to add
     */
    public void add(Pattern pattern, int numTimes)
    {
        for (int i=0; i < numTimes; i++)
        {
            fireFragmentAdded(pattern);
            appendMusicString(" ");
            appendMusicString(pattern.getMusicString());
        }
    }

    /**
     * Adds a music string to the end of this pattern.
     * @param musicString the music string to add
     */
    public void add(String musicString, int numTimes)
    {
        add(new Pattern(musicString), numTimes);
    }

    /**
     * Adds a number of patterns sequentially
     * @param musicString the music string to add
     * @version 4.0
     */
    public void add(Pattern... patterns)
    {
        for (Pattern pattern : patterns) {
            add(pattern);
        }
    }

    /**
     * Adds a number of patterns sequentially
     * @param musicString the music string to add
     * @version 4.0
     */
    public void add(String... musicStrings)
    {
        for (String string : musicStrings) {
            add(string);
        }
    }
    
    /**
     * Adds the {@link JFugueElement}s to the pattern.
     * 
     * @param elements
     */
    public void add(JFugueElement... elements) {
    	// TODO Might we want to just add the contents of addElement?
		for (JFugueElement jFugueElement : elements) {
			addElement(jFugueElement);
		}
	}

    /**
     * Adds an individual element to the pattern.  This takes into
     * account the possibility that the element may be a sequential or
     * parallel note, in which case no space is placed before it.
     * @param element the element to add
     */
    public void addElement(JFugueElement element)
    {
        String elementMusicString = element.getMusicString();

        // Don't automatically add a space if this is a continuing note event
        if ((elementMusicString.charAt(0) == '+') ||
            (elementMusicString.charAt(0) == '_')) {
            appendMusicString(elementMusicString);
        } else {
            appendMusicString(" ");
            appendMusicString(elementMusicString);
            fireFragmentAdded(new Pattern(elementMusicString));
        }
    }

    //
    //  PROPERTIES
    //
    
    /**
     * Sets the title for this Pattern.
     * As of JFugue 4.0, the title is set as a property with the key Pattern.TITLE
     * @param title the title for this Pattern
     */
    public void setTitle(String title)
    {
        setProperty(TITLE, title);
    }

    /**
     * Returns the title of this Pattern
     * As of JFugue 4.0, the title is set as a property with the key Pattern.TITLE
     * @return the title of this Pattern
     */
    public String getTitle()
    {
        return getProperty(TITLE);
    }

    /**
     * Get a property on this pattern, such as "author" or "date".
     * @version 4.0
     */
    public String getProperty(String key)
    {
        return getProperties().get(key);
    }

    /**
     * Set a property on this pattern, such as "author" or "date".
     * @version 4.0
     */
    public void setProperty(String key, String value)
    {
        getProperties().put(key, value);
    }

    /**
     * Get all properties set on this pattern, such as "author" or "date".
     * @version 4.0
     */
    public Map<String, String> getProperties()
    {
        if (properties == null) {
            properties = new HashMap<String, String>();
        }
        return properties;
    }

    /**
     * Returns a String containing key-value pairs stored in this object's properties,
     * separated by semicolons and spaces.
     * Values are returned in the following form:
     * key1: value1; key2: value2; key3: value3
     *
     * @return a String containing key-value pairs stored in this object's properties, separated by semicolons and spaces
     * @version 4.0
     */
    public String getPropertiesAsSentence()
    {
        StringBuilder buddy = new StringBuilder();
        Iterator<String> iter = getProperties().keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            String value = getProperty(key);
            buddy.append(key);
            buddy.append(": ");
            buddy.append(value);
            buddy.append("; ");
        }
        String result = buddy.toString();
        return result.endsWith("; ")
        			? result.substring(0, result.length()-2) // Take off the last semicolon-space
        			: result;
    }

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
     * @version 4.0
     */
    public String getPropertiesAsParagraph()
    {
        StringBuilder buddy = new StringBuilder();
        Iterator<String> iter = getProperties().keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            String value = getProperty(key);
            buddy.append(key);
            buddy.append(": ");
            buddy.append(value);
            buddy.append("\n");
        }
        String result = buddy.toString();
        return result.substring(0, result.length());
    }

    /**
     * Changes all timestamp values by the offsetTime passed in.
     * NOTE: This method is only useful for patterns that have been converted from a MIDI file
     * (i.e., that have time tokens, indicated with an @ sign).
     * @param offsetTime
     */
    public void offset(long offsetTime)
    {
        StringBuilder buddy = new StringBuilder();
        String[] tokens = getMusicString().split(" ");
        for (int i=0; i < tokens.length; i++)
        {
            if ((tokens[i].length() > 0) && (tokens[i].charAt(0) == '@')) {
                String timeNumberString = tokens[i].substring(1,tokens[i].length());
                if (timeNumberString.indexOf("[") == -1) {
                    long timeNumber = new Long(timeNumberString).longValue();
                    long newTime = timeNumber + offsetTime;
                    if (newTime < 0) newTime = 0;
                    buddy.append("@" + newTime);
                } else {
                    buddy.append(tokens[i]);
                }
            } else {
                buddy.append(tokens[i]);
            }
            buddy.append(" ");
        }
        setMusicString(buddy.toString());
    }

    //
    //  PATTERN MANIPULATION
    //
    
    /**
     * Repeats the music string in this pattern
     * by the given number of times.
     * Example: If the pattern is "A B", calling <code>repeat(4)</code> will
     * make the pattern "A B A B A B A B".
     * @version 3.0
     */
    public void repeat(int times)
    {
        repeat(null, getMusicString(), times, null);
    }

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
     * @version 3.0
     **/
    public void repeat(int times, int beginIndex)
    {
        Pattern patternUpToBeingIndex = getSubPattern(0, beginIndex-1);
        Pattern repeatingPattern = getSubPattern(beginIndex);
        
        repeat(patternUpToBeingIndex.toString(), repeatingPattern.toString(), times, null);
    }

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
     * @version 3.0
     */
    public void repeat(int times, int beginIndex, int endIndex)
    {
        Pattern patternUpToBeingIndex = getSubPattern(0, beginIndex-1);
        Pattern repeatingPattern = getSubPattern(beginIndex, endIndex);
        Pattern patternAfterEndIndex = getSubPattern(endIndex + 1);
        
        repeat(patternUpToBeingIndex.toString(), repeatingPattern.toString(), times, patternAfterEndIndex.toString());
    }

    private void repeat(String header, String repeater, int times, String trailer)
    {
        StringBuilder buddy = new StringBuilder();

        // Add the header, if it exists
        if (header != null)
        {
            buddy.append(header);
        }

        // Repeat and add the repeater
        for (int i=0; i < times; i++)
        {
            buddy.append(repeater);
            if (i < times-1) {
                buddy.append(" ");
            }
        }

        // Add the trailer, if it exists
        if (trailer != null)
        {
            buddy.append(trailer);
        }

        // Create the new Pattern and return it
        this.setMusicString(buddy.toString());
    }

    /**
     * Returns a new Pattern that is a subpattern of this pattern.
     * @return subpattern of this pattern.
     * 
     * Version 4.1.0 improves on previous versions of this method by
     * returning tokens instead of substring of the pattern.
     * @version 4.1.0
     */
    public Pattern getSubPattern(int beginIndex)
    {
    	String[] tokens = getTokens();
    	return getSubPattern(beginIndex, tokens.length);
    }

    /**
     * Returns a new Pattern that is a subpattern of this pattern.
     * @return subpattern of this pattern
     * 
     * Version 4.1.0 improves on previous versions of this method by
     * returning tokens instead of substring of the pattern.
     * @version 4.1.0
     */
    public Pattern getSubPattern(int beginIndex, int endIndex)
    {
    	String[] tokens = getTokens();
    	if (endIndex >= tokens.length) {
    		throw new ArrayIndexOutOfBoundsException("endIndex is greater than the number of tokens in this Pattern");
    	}
    	
    	StringBuilder buddy = new StringBuilder();
    	for (int i = beginIndex; i < endIndex; i++) {
    		buddy.append(tokens[i]);
    		if (i < endIndex-1) { 
    			buddy.append(" ");
    		}
    	}
    	
        return new Pattern(buddy.toString());
    }

    /**
     * Returns a Pattern that replaces the token at the provided index with the new token.
     * 
     * Use this if, for example, you have two musical phrases that are very similar except for a few notes.
     * 
     * @param index The index of the token to replace
     * @param newToken The new token to place at the specified index
     * @return The new Pattern
     */
    public Pattern replace(int index, String newToken)
    {
    	StringBuilder buddy = new StringBuilder();
    	String[] tokens = getTokens();
    	for (int i = 0; i < tokens.length; i++) {
    		if (i == index) {
    			buddy.append(newToken);
    		} else {
    			buddy.append(tokens[i]);
    		}
    		if (i < tokens.length - 1) {
    			buddy.append(" ");
    		}
    	}
    	
    	return new Pattern(buddy.toString());
    }

    /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((musicString == null) ? 0 : musicString.toString().hashCode());
		result = prime * result
				+ ((properties == null) ? 0 : getPropertiesAsSentence().hashCode());
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
		if (!(obj instanceof Pattern))
			return false;
		return hashCode() == obj.hashCode();
	}

	/**
     * Returns a Pattern that replaces a series of tokens with the new tokens.
     * 
     * @param index The index of the first token to replace
     * @param newTokens An array of tokens that will be placed into the pattern
     * @return The new Pattern
     */
    public Pattern replace(int startingIndex, String... newTokens)
    {
    	String[] tokens = getTokens();
    	if (startingIndex + newTokens.length > tokens.length) {
    		throw new ArrayIndexOutOfBoundsException("startingIndex plus the number of newTokens is greater than the number of tokens in this Pattern");
    	}
    	
    	StringBuilder buddy = new StringBuilder();
    	int counter = 0;
    	for (int i = 0; i < tokens.length; i++) {
    		if ((i >= startingIndex) && (i < startingIndex + newTokens.length)) {
    			buddy.append(newTokens[counter]);
    			counter++;
    		} else {
    			buddy.append(tokens[i]);
    		}
    		if (i < tokens.length - 1) {
    			buddy.append(" ");
    		}
    	}
    	
    	return new Pattern(buddy.toString());
    }

    /**
     * Loads a pattern from a text file.  Each line of the file should be either a
     * collection of MusicString tokens, or a line that begins with a # character, indicating
     * that the line is a comment.
     * 
     * Commented lines may contain properties in the form of key:value.  For example,
     * <pre>
     * # Title: Inventio 13
     * </pre>
     * would create a property called 'Title' that would contain the value 'Inventio 13'.
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static Pattern loadPattern(File file) throws IOException
    {
        StringBuilder buddy = new StringBuilder();

        Pattern pattern = new Pattern();

        BufferedReader bread = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        while (bread.ready()) {
            String s = bread.readLine();
            if ((s != null) && (s.length() > 1)) {
                if (s.charAt(0) != '#') {
                    buddy.append(" ");
                    buddy.append(s);
                } else {
                    if (s.indexOf(':') > -1) {
                        String key = s.substring(1, s.indexOf(':')).trim();
                        String value = s.substring(s.indexOf(':')+1, s.length()).trim();
                        if (key.equalsIgnoreCase(TITLE)) {
                            pattern.setTitle(value);
                        } else {
                            pattern.setProperty(key, value);
                        }
                    }
                }
            }
        }
        bread.close();
        pattern.setMusicString(buddy.toString());

        return pattern;
    }

    /**
     * Saves the pattern as a text file.  The file passed in should ideally be named
     * with a .jfugue extension.
     * 
     * @param filename the filename to save under
     */
    public void savePattern(File file) throws IOException
    {
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        if ((getProperties().size() > 0) || (getTitle() != null)) {
            out.write("#\n");
            if (getTitle() != null) {
                out.write("# ");
                out.write("Title: ");
                out.write(getTitle());
                out.write("\n");
            }
            Iterator<String> iter = getProperties().keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                if (!key.equals(TITLE)) {
                    String value = getProperty(key);
                    out.write("# ");
                    out.write(key);
                    out.write(": ");
                    out.write(value);
                    out.write("\n");
                }
            }
            out.write("#\n");
            out.write("\n");
        }
        String musicString = getMusicString();
        while (musicString.length() > 0) {
            if ((musicString.length() > 80) && (musicString.indexOf(' ', 80) > -1)) {
                int indexOf80ColumnSpace = musicString.indexOf(' ', 80);
                out.write(musicString.substring(0, indexOf80ColumnSpace));
                out.newLine();
                musicString = musicString.substring(indexOf80ColumnSpace, musicString.length());
            } else {
                out.write(musicString);
                musicString = "";
            }
        }
        out.close();
    }

    /**
     * Returns an array of strings representing each token in the Pattern.
     * @return
     */
    public String[] getTokens()
    {
        StringTokenizer strtok = new StringTokenizer(musicString.toString()," \n\t");

        List<String> list = new ArrayList<String>();
        while (strtok.hasMoreTokens()) {
            String token = strtok.nextToken();
            if (token != null) {
                list.add(token);
            }
        }

        String[] retVal = new String[list.size()];
        list.toArray(retVal);
        return retVal;
    }

    /**
     * Indicates whether this pattern is composed of valid elements
     * that can be parsed by the MusicStringParser.
     * @param musicString the musicString to test
     * @return whether the musicString is valid
     * @version 4.1
     */
    public boolean isValid()
    {
        try {
            MusicStringParser parser = new MusicStringParser();
            parser.parse(this);
        } catch (JFugueException e)
        {
            return false;
        }
        return true;
    }

    //
    //  TRANSFORMERS AND TOOLS
    //
    
    /**
     * @version 4.1
     */
    public List<Byte> getInstruments()
    {
        GetInstrumentsUsedTool ifinder = new GetInstrumentsUsedTool();
        return ifinder.getInstrumentsUsedInPattern(this);
    }
    
    /**
     * @version 4.1
     */
    public Class<ReversePatternTransformer> getReversePatternTransformerClass() 
    {
        return ReversePatternTransformer.class;
    }
    
    /**
     * @version 4.1
     */
    public Pattern reverse()
    {
        Pattern reverse = null;
        try {
            PatternTransformer rpt = (PatternTransformer)getReversePatternTransformerClass().newInstance();
            reverse = rpt.transform(this);
        } catch (IllegalAccessException e) {
            throw new JFugueException(e);
        } catch (InstantiationException e) {
            throw new JFugueException(e);
        }
        return reverse;
    }
    
    //
    //  LISTENERS
    //

    /** List of ParserListeners */
    protected EventListenerList listenerList; // uses lazy initialization, so access only through getEventListenerList()

    /**
     * Adds a <code>PatternListener</code>.  The listener will receive events when new
     * parts are added to the pattern.
     *
     * @param listener the listener that is to be notified when new parts are added to the pattern
     */
    public void addPatternListener(PatternListener l) {
        getEventListenerList().add(PatternListener.class, l);
    }

    /**
     * Removes a <code>PatternListener</code>.
     *
     * @param listener the listener to remove
     */
    public void removePatternListener(PatternListener l) {
        getEventListenerList().remove(PatternListener.class, l);
    }

    protected void clearPatternListeners() {
        EventListener[] l = getEventListenerList().getListeners (PatternListener.class);
        int numListeners = l.length;
        for (int i = 0; i < numListeners; i++) {
            getEventListenerList().remove(PatternListener.class, (PatternListener)l[i]);
        }
    }

    /** @version 4.0.4 */
    protected EventListenerList getEventListenerList()
    {
        if (listenerList == null) {
            listenerList = new EventListenerList();
        }
        return listenerList;
    }
    
    /** Tells all PatternListener interfaces that a fragment has been added. */
    private void fireFragmentAdded(Pattern fragment)
    {
        Object[] listeners = getEventListenerList().getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PatternListener.class) {
                ((PatternListener)listeners[i + 1]).fragmentAdded(fragment);
            }
        }
    }

    /**
     * @version 3.0
     */
    public String toString()
    {
        return getMusicString();
    }

    public String getVerifyString() {
		return "Pattern: " + toString();
	}
}
