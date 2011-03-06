package org.jfugue.parsers;

import java.io.Reader;
import java.util.List;
import org.jfugue.Environment;

import org.jfugue.elements.JFugueElement;

public interface ParserInterface {
	public List<JFugueElement> parse(Reader reader, Environment environment);
}
