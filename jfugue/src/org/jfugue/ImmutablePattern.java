package org.jfugue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ImmutablePattern extends Pattern {

	private static final long serialVersionUID = 1L;

    public ImmutablePattern(String musicString, Map<String, String> properties) {
    	this.musicString = new StringBuilder(musicString);
    	if (properties == null) {
    		this.properties = Collections.emptyMap();
    	} else {
    		this.properties = Collections.unmodifiableMap(new HashMap<String, String>(properties));
    	}
    }
	
    public ImmutablePattern(String musicString)
    {
        this(musicString, null);
    }
    
    public ImmutablePattern() {
		this("");
	}
    
    public ImmutablePattern(String... strings) {
		this();
		boolean first = true;
		for (String string : strings) {
			if (first)
				first = false;
			else
				musicString.append(' ');
			musicString.append(string);
		}
	}
    
    public ImmutablePattern(JFugueElement... elements) {
    	this();
    	boolean first = true;
		for (JFugueElement element : elements) {
			String elemStr = element.getMusicString();
			if (first)
				first = false;
			else if (elemStr.charAt(0) != '+' && elemStr.charAt(0) != '_')
				musicString.append(' ');
			musicString.append(elemStr);
		}
    }
    
	public ImmutablePattern(Pattern pattern) {
		this(pattern.getMusicString(), pattern.getProperties());
	}
	
	public void setMusicString(String musicString)
    {
        throw new UnsupportedOperationException("This is an ImmutablePattern");
    }

    @SuppressWarnings("unused")
	private void appendMusicString(String appendString)
    {
    	throw new UnsupportedOperationException("This is an ImmutablePattern");
    }
    
    public void insert(String musicString)
    {
    	throw new UnsupportedOperationException("This is an ImmutablePattern");
    }

    public void add(Pattern pattern)
    {
    	throw new UnsupportedOperationException("This is an ImmutablePattern");
    }

    public void add(String musicString)
    {
    	throw new UnsupportedOperationException("This is an ImmutablePattern");
    }

    public void add(Pattern pattern, int numTimes)
    {
    	throw new UnsupportedOperationException("This is an ImmutablePattern");
    }

    public void add(String musicString, int numTimes)
    {
    	throw new UnsupportedOperationException("This is an ImmutablePattern");
    }

    public void add(String... musicStrings)
    {
    	throw new UnsupportedOperationException("This is an ImmutablePattern");
    }
    
    public void add(JFugueElement... elements) {
    	throw new UnsupportedOperationException("This is an ImmutablePattern");
	}

    public void addElement(JFugueElement element)
    {
    	throw new UnsupportedOperationException("This is an ImmutablePattern");
    }

    public void setTitle(String title)
    {
    	throw new UnsupportedOperationException("This is an ImmutablePattern");
    }
    
    public void setProperty(String key, String value)
    {
    	throw new UnsupportedOperationException("This is an ImmutablePattern");
    }

    public Map<String, String> getProperties()
    {
        return Collections.unmodifiableMap(super.getProperties());
    }

    public void offset(long offsetTime)
    {
    	throw new UnsupportedOperationException("This is an ImmutablePattern");
    }

    public void repeat(int times)
    {
    	throw new UnsupportedOperationException("This is an ImmutablePattern");
    }

    public void repeat(int times, int beginIndex)
    {
    	throw new UnsupportedOperationException("This is an ImmutablePattern");
    }

    public void repeat(int times, int beginIndex, int endIndex)
    {
    	throw new UnsupportedOperationException("This is an ImmutablePattern");
    }

    @SuppressWarnings("unused")
	private void repeat(String header, String repeater, int times, String trailer)
    {
    	throw new UnsupportedOperationException("This is an ImmutablePattern");
    }

}
