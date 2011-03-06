package org.jfugue.test;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.List;

import org.jfugue.Environment;
import org.jfugue.elements.JFugueElement;
import org.jfugue.parsers.ParserInterface;
import org.jfugue.parsers.msp5.MusicStringParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScalaMusicStringParserTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testVoice() throws Exception {
		List<JFugueElement> pr = parse("V4");
		assertEquals("V4", pr.get(0).getMusicString());
	}
	
	@Test
	public void testMSP() throws Exception {
		String expected = "V7 T250 I[Electric_Piano_2] L3";
		String actual = parseAndReconstruct(expected);
		System.out.println(actual);
		assertEquals(expected, actual);
	}
	
	private String parseAndReconstruct(String string) {
		List<JFugueElement> pr = parse(string);
		StringBuilder sb = new StringBuilder(string.length());
		for (JFugueElement element : pr) {
			sb.append(element.getMusicString());
			sb.append(' ');
		}
		return sb.substring(0, sb.length() - 1);
	}
	
	private List<JFugueElement> parse(String string) {
		Environment env = new Environment();
		ParserInterface parser = new MusicStringParser();
		return parser.parse(new StringReader(string), env);
	}
	
}
