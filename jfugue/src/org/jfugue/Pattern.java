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

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import org.jfugue.elements.AbstractPattern;
import org.jfugue.elements.JFugueElement;
import org.jfugue.extras.GetInstrumentsUsedTool;
import org.jfugue.parsers.MusicStringParser;
import org.jfugue.visitors.ElementVisitor;
import org.jfugue.visitors.ListenerToVisitorAdaptor;

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
public class Pattern extends AbstractPattern implements JFugueElement, PatternInterface
{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 4334276178935154938L;
	protected StringBuilder musicString;
	protected List<JFugueElement> elements = new LinkedList<JFugueElement>();
	protected List<JFugueElement> iElements = Collections.unmodifiableList(elements);

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
    	/* This is to make it work with ImmutablePatterns.  I'm
    	 * not quite sure why I need this.  -ska
    	 */
//        setMusicString(musicString);
    	this.musicString = new StringBuilder();
        this.musicString.append(musicString);
    }
    
    public Pattern(String... strings) {
		this();
		add(strings);
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


//    /**
//     * This constructor creates a new Pattern that contains each of the given patterns
//     * @version 4.0
//     * */
//    public Pattern(Pattern... patterns)
//    {
//        this();
//        for (Pattern p : patterns) {
//            this.add(p);
//        }
//    }
    
    public Pattern(JFugueElement... elements) {
    	this();
    	add(elements);
    }

    /**
     * 
     * @version 4.0
     * */
    public static PatternInterface createPattern(Map<String, Pattern> context, PatternInterface pattern)
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
    public static PatternInterface createPattern(Map<String, Pattern> context, Pattern... patterns)
    {
    	
    	PatternInterface allPatterns = new Pattern();
    	for (Pattern p : patterns) {
    		allPatterns.add(p);
    	}
    	
    	return createPattern(context, allPatterns);
    }
    
    /* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#setMusicString(java.lang.String)
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

    /* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#insert(java.lang.String)
	 */
    public void insert(String musicString)
    {
       this.musicString.insert(0, " ");
       this.musicString.insert(0, musicString);
    }

    /* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#add(org.jfugue.Pattern)
	 */
    public void add(Pattern pattern)
    {
        fireFragmentAdded(pattern);
        appendMusicString(" ");
        appendMusicString(pattern.getMusicString());
    }

    /* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#add(java.lang.String)
	 */
    public void add(String musicString)
    {
        add(new Pattern(musicString));
    }

    /* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#add(org.jfugue.Pattern, int)
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

    /* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#add(java.lang.String, int)
	 */
    public void add(String musicString, int numTimes)
    {
        add(new Pattern(musicString), numTimes);
    }

//    /**
//     * Adds a number of patterns sequentially
//     * @param musicString the music string to add
//     * @version 4.0
//     */
//    public void add(Pattern... patterns)
//    {
//        for (Pattern pattern : patterns) {
//            add(pattern);
//        }
//    }

    /* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#add(java.lang.String)
	 */
    public void add(String... musicStrings)
    {
        for (String string : musicStrings) {
            add(string);
        }
    }
    
    /* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#add(org.jfugue.JFugueElement)
	 */
    public void add(JFugueElement... elements) {
    	// TODO Might we want to just add the contents of addElement?
		for (JFugueElement jFugueElement : elements) {
			if (jFugueElement instanceof Pattern)
				add((Pattern) jFugueElement);
			else
				addElement(jFugueElement);
		}
	}

    /* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#addElement(org.jfugue.JFugueElement)
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
    
    /* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#offset(long)
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
    
    /* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#repeat(int)
	 */
    public void repeat(int times)
    {
        repeat(null, getMusicString(), times, null);
    }

    /* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#repeat(int, int)
	 */
    public void repeat(int times, int beginIndex)
    {
        PatternInterface patternUpToBeingIndex = getSubPattern(0, beginIndex-1);
        PatternInterface repeatingPattern = getSubPattern(beginIndex);
        
        repeat(patternUpToBeingIndex.toString(), repeatingPattern.toString(), times, null);
    }

    /* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#repeat(int, int, int)
	 */
    public void repeat(int times, int beginIndex, int endIndex)
    {
        PatternInterface patternUpToBeingIndex = getSubPattern(0, beginIndex-1);
        PatternInterface repeatingPattern = getSubPattern(beginIndex, endIndex);
        PatternInterface patternAfterEndIndex = getSubPattern(endIndex + 1);
        
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

    /* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#getSubPattern(int)
	 */
    public PatternInterface getSubPattern(int beginIndex)
    {
    	String[] tokens = getTokens();
    	return getSubPattern(beginIndex, tokens.length);
    }

    /* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#getSubPattern(int, int)
	 */
    public PatternInterface getSubPattern(int beginIndex, int endIndex)
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

    /* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#replace(int, java.lang.String)
	 */
    public PatternInterface replace(int index, String newToken)
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

	/* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#replace(int, java.lang.String)
	 */
    public PatternInterface replace(int startingIndex, String... newTokens)
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

    /* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#isValid()
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
    
    /* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#getInstruments()
	 */
    public List<Byte> getInstruments()
    {
        GetInstrumentsUsedTool ifinder = new GetInstrumentsUsedTool();
        return ifinder.getInstrumentsUsedInPattern(this);
    }
    
    /* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#reverse()
	 */
    public PatternInterface reverse()
    {
        PatternInterface reverse = null;
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

    /** Tells all PatternListener interfaces that a fragment has been added. */
    private void fireFragmentAdded(PatternInterface fragment)
    {
        Object[] listeners = getEventListenerList().getListenerList ();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PatternListener.class) {
                ((PatternListener)listeners[i + 1]).fragmentAdded(fragment);
            }
        }
    }

    /* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#toString()
	 */
    public String toString()
    {
        return getMusicString();
    }

    public String getVerifyString() {
		return "Pattern: " + toString();
	}

	/* (non-Javadoc)
	 * @see org.jfugue.PatternInterface#acceptVisitor(org.jfugue.ElementVisitor)
	 */
	public void acceptVisitor(ElementVisitor visitor) {
		// TODO	make this not use the parser
		visitor.visit(this);
		MusicStringParser msp = new MusicStringParser();
		msp.addParserListener(new ListenerToVisitorAdaptor(visitor));
		msp.parse(this);
	}
}
