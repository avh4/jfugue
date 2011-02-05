package org.jfugue.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

import org.jfugue.JFugueException;
import org.jfugue.parsers.Environment;
import org.jfugue.parsers.ParserContext;
import org.jfugue.parsers.ParserError;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ParserContextTest extends ParserContext {
	
//	public ParserContextTest(PushbackReader reader, Environment environment) {
//		super(reader, environment);
//	}

	public ParserContextTest() {
		super(null, new Environment());
	}

	public void setString(String string) {
		reader = new PushbackReader(new StringReader(string));
	}

	@Before
	public void setUp() throws Exception {
		environment.add("BLAH", "50");
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Ignore
	@Test
	public void testReadSymbol() throws JFugueException, IOException, ParserError {
		setString("[Blah_Foo]");
		assertEquals("[Blah_Foo]", readSymbol());
		setString("Blah_Fraz");
		assertEquals("[Blah_Fraz]", readSymbol());
	}

//	@Ignore
	@Test
	public void testReadIdentifier() throws IOException, ParserError {
		String expected = "Blah";
		setString(expected);
		assertEquals(expected, readIdentifier());
		expected = "Foo_Bar";
		setString(expected);
		assertEquals(expected, readIdentifier());
	}

//	@Ignore
	@Test
	public void testReadByte() throws JFugueException, IOException, ParserError {
		byte expected = 50;
		setString("50");
		byte b = readByte();
		assertEquals(expected, b);
		setString("[Blah]");
		b = readByte();
		assertEquals(expected, b);
	}

	@Ignore
	@Test
	public void testReadInt() {
	}

	@Ignore
	@Test
	public void testReadLong() {
	}

//	@Ignore
	@Test
	public void testReadDouble() throws JFugueException, IOException, ParserError {
		double expected = 0.25;
		setString(Double.toString(expected));
		assertEquals(expected, readDouble(), 0.0001);
		environment.add("DOUBLE", Double.toString(expected));
		setString("[Double]");
		assertEquals(expected, readDouble(), 0.0001);
	}

	@Ignore
	@Test
	public void testReadOneOfTheChars() {
	}

	@Ignore
	@Test
	public void testReadCharThenByte() {
	}

	
}
