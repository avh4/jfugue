package org.jfugue.test;

import static org.junit.Assert.*;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.CharBuffer;
import java.util.Scanner;

import org.jfugue.Environment;
import org.jfugue.JFugueException;
import org.jfugue.parsers.ParserContext;
import org.jfugue.parsers.ParserContext.CharThen;
import org.jfugue.parsers.ParserError;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ParserContextTest {
	
//	public ParserContextTest(PushbackReader reader, Environment environment) {
//		super(reader, environment);
//	}

	protected Environment environment; 
	protected Reader reader;
	protected ParserContext context;

	public ParserContextTest() {
		environment = new Environment();
		context = new ParserContext(new StringReader(""), environment);
	}

	public void setString(String string) {
//		context.setReader(new StringReader(string));
		context = new ParserContext(new StringReader(string), environment);
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
		setString(expected + " bar baz");
		assertEquals(expected, readIdentifier());
		expected = "Foo_Bar";
		setString(expected + " bar baz");
		assertEquals(expected, readIdentifier());
	}
	
//	@Ignore
	@Test
	public void testReadToken() throws Exception {
		String exp = "foo";
		String expplus = "foo bar";
		setString(expplus);
		assertEquals(exp, readToken());
	}

//	@Ignore
	@Test
	public void testReadByte() throws JFugueException, IOException, ParserError {
		byte expected = 50;
		setString("50");
		byte actual = readByte();
		assertEquals(expected, actual);
		setString("[Blah]");
		actual = readByte();
		assertEquals(expected, actual);
	}

//	@Ignore
	@Test
	public void testReadInt() throws Exception {
		int expected = 50;
		setString(String.valueOf(expected));
		int actual = readInt();
		assertEquals(expected, actual);
		setString("[Blah]");
		actual = readInt();
		assertEquals(expected, actual);
	}

//	@Ignore
	@Test
	public void testReadHexByte() throws Exception {
		setString("5D");
		assertEquals(93L, (long) context.readHexByte());
	}

//	@Ignore
	@Test
	public void testReadLong()  throws Exception {
		setString("93 blah");
		assertEquals(93L, readLong());
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
		expected = 10;
		setString(Double.toString(expected) + " bar baz");
		assertEquals(expected, readDouble(), 0.0001);
	}


	/**
	 * @return
	 * @throws JFugueException
	 * @throws IOException
	 * @throws ParserError
	 * @see org.jfugue.parsers.ParserContext#readSymbol()
	 */
	public String readSymbol() throws JFugueException, IOException, ParserError {
		return context.readSymbol();
	}

	/**
	 * @return
	 * @throws IOException
	 * @throws ParserError
	 * @see org.jfugue.parsers.ParserContext#readIdentifier()
	 */
	public String readIdentifier() throws IOException, ParserError {
		return context.readIdentifier();
	}

	/**
	 * @return
	 * @throws JFugueException
	 * @throws IOException
	 * @throws ParserError
	 * @see org.jfugue.parsers.ParserContext#readInt()
	 */
	public int readInt() throws JFugueException, IOException, ParserError {
		return context.readInt();
	}

	/**
	 * @return
	 * @throws JFugueException
	 * @throws IOException
	 * @throws ParserError
	 * @see org.jfugue.parsers.ParserContext#readLong()
	 */
	public long readLong() throws JFugueException, IOException, ParserError {
		return context.readLong();
	}

	/**
	 * @return
	 * @throws JFugueException
	 * @throws IOException
	 * @throws ParserError
	 * @see org.jfugue.parsers.ParserContext#readDouble()
	 */
	public double readDouble() throws JFugueException, IOException, ParserError {
		return context.readDouble();
	}

	/**
	 * @param cs
	 * @return
	 * @throws IOException
	 * @throws JFugueException
	 * @throws ParserError
	 * @see org.jfugue.parsers.ParserContext#readChar(char[])
	 */
	public char readOneOfTheChars(char... cs) throws IOException,
			JFugueException, ParserError {
		return context.readChar(cs);
	}

	/**
	 * @return
	 * @throws JFugueException
	 * @throws IOException
	 * @throws ParserError
	 * @see org.jfugue.parsers.ParserContext#readByte()
	 */
	public byte readByte() throws JFugueException, IOException, ParserError {
		return context.readByte();
	}

	/**
	 * @param radix
	 * @return
	 * @throws JFugueException
	 * @throws IOException
	 * @throws ParserError
	 * @see org.jfugue.parsers.ParserContext#readByte(int)
	 */
	public byte readByte(int radix) throws JFugueException, IOException,
			ParserError {
		return context.readByte(radix);
	}


	public String readToken() throws ParserError, IOException {
		return context.readToken();
	}

}
