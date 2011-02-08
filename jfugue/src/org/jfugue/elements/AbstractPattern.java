package org.jfugue.elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.event.EventListenerList;

import org.jfugue.Pattern;
import org.jfugue.PatternInterface;
import org.jfugue.PatternListener;
import org.jfugue.extras.ReversePatternTransformer;

@SuppressWarnings("serial")
public abstract class AbstractPattern implements PatternInterface {

	protected Map<String, String> properties;

	/**
	 * Load a {@code Pattern} from an {@code Reader} in {@code .jfugue} format.
	 * Each line be either a collection of MusicString tokens, or a line that begins
	 * with a # character, indicating that the line is a comment.
	 * 
	 * Commented lines may contain properties in the form of key:value.  For example,
	 * <pre>
	 * # Title: Inventio 13
	 * </pre>
	 * would create a property called 'Title' that would contain the value 'Inventio 13'.
	 * 
	 * @param reader
	 * @return the {@code Pattern} loaded
	 * @throws IOException
	 */
	public static Pattern loadPattern(Reader reader) throws IOException {
	    StringBuilder buddy = new StringBuilder();
	
	    Pattern pattern = new Pattern();
	
	    BufferedReader bread = new BufferedReader(reader);
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

	public void setTitle(String title) {
	    setProperty(TITLE, title);
	}

	public String getTitle() {
	    return getProperty(TITLE);
	}

	public String getProperty(String key) {
	    return getProperties().get(key);
	}

	public void setProperty(String key, String value) {
	    getProperties().put(key, value);
	}

	public Map<String, String> getProperties() {
	    if (properties == null) {
	        properties = new HashMap<String, String>();
	    }
	    return properties;
	}

	public String getPropertiesAsSentence() {
	    StringBuilder buddy = new StringBuilder();
	    for (Map.Entry<String, String> entry : getProperties().entrySet()) {
			buddy.append(String.format("%s: %s; ", entry.getKey(), entry.getValue()));
		}
//	    Iterator<String> iter = getProperties().keySet().iterator();
//	    while (iter.hasNext()) {
//	        String key = iter.next();
//	        String value = getProperty(key);
//	        buddy.append(key);
//	        buddy.append(": ");
//	        buddy.append(value);
//	        buddy.append("; ");
//	    }
	    String result = buddy.toString();
	    return result.endsWith("; ")
	    			? result.substring(0, result.length()-2) // Take off the last semicolon-space
	    			: result;
	}

	public String getPropertiesAsParagraph() {
	    StringBuilder buddy = new StringBuilder();
	    for (Map.Entry<String, String> entry : getProperties().entrySet()) {
	    	buddy.append(String.format("%s: %s\n", entry.getKey(), entry.getValue()));
		}
//	    Iterator<String> iter = getProperties().keySet().iterator();
//	    while (iter.hasNext()) {
//	        String key = iter.next();
//	        String value = getProperty(key);
//	        buddy.append(key);
//	        buddy.append(": ");
//	        buddy.append(value);
//	        buddy.append("\n");
//	    }
	    String result = buddy.toString();
	    return result.substring(0, result.length());
	}

	/**
	 * Load a {@code Pattern} from an {@code InputStream} in {@code .jfugue} format.
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static Pattern loadPattern(InputStream in) throws IOException {
		return loadPattern(new InputStreamReader(in));
	}

	/**
	 * Load a {@code Pattern} from an {@code File} in {@code .jfugue} format.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static PatternInterface loadPattern(File file) throws IOException {
		return loadPattern(new FileInputStream(file));
	}

	/** List of ParserListeners */
	protected EventListenerList listenerList;

	public void savePattern(Writer writer) throws IOException {
	    BufferedWriter out = new BufferedWriter(writer);
	    if ((getProperties().size() > 0) || (getTitle() != null)) {
	        out.write("#\n");
	        if (getTitle() != null) {
	            out.write("# ");
	            out.write("Title: ");
	            out.write(getTitle());
	            out.write("\n");
	        }
	        for (Map.Entry<String, String> entry : getProperties().entrySet()) {
				if (entry.getKey().equals(TITLE))
					continue;
				out.write(String.format("# %s: %s\n", entry.getKey(), entry.getValue()));
			}
//	        Iterator<String> iter = getProperties().keySet().iterator();
//	        while (iter.hasNext()) {
//	            String key = iter.next();
//	            if (!key.equals(TITLE)) {
//	                String value = getProperty(key);
//	                out.write("# ");
//	                out.write(key);
//	                out.write(": ");
//	                out.write(value);
//	                out.write("\n");
//	            }
//	        }
	        out.write("#\n\n");
//	        out.write("\n");
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

	public void savePattern(OutputStream out) throws IOException {
		savePattern(new OutputStreamWriter(out));
	}

	public void savePattern(File file) throws IOException {
		savePattern(new FileOutputStream(file));
	}

	public String[] getTokens() {
	    StringTokenizer strtok = new StringTokenizer(getMusicString()," \n\t");
	
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

	public Class<ReversePatternTransformer> getReversePatternTransformerClass() {
	    return ReversePatternTransformer.class;
	}

	public AbstractPattern() {
		super();
	}

	public void addPatternListener(PatternListener l) {
	    getEventListenerList().add(PatternListener.class, l);
	}

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
	protected EventListenerList getEventListenerList() {
	    if (listenerList == null) {
	        listenerList = new EventListenerList();
	    }
	    return listenerList;
	}

}