package org.jfugue.parsers.msp5;

import java.io.Reader;
import java.util.List;

import org.jfugue.Environment;
import org.jfugue.elements.JFugueElement;
import org.jfugue.parsers.ParserInterface;

public class MusicStringParser implements ParserInterface {

	private ScalaMusicStringParser parser = new ScalaMusicStringParser();

	public List<JFugueElement> parse(Reader reader, Environment environment) {
		return parser.parseAndEval(reader, environment);
	}
	
}
