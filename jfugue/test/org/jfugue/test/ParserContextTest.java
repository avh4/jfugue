package org.jfugue.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.CharBuffer;

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
	}

	public void setString(String string) {
		reader = new PushbackReader(new StringReader(string));
		context = new ParserContext(reader, environment);
	}

	@Ignore
	@Test
	public void testReadHexByte() throws Exception {
		setString("5D");
		assertEquals(93L, (long) context.readHexByte());
	}
	/**
	 * @return
	 * @throws IOException
	 * @see org.jfugue.parsers.ParserContext#read()
	 */
	public int read() throws IOException {
		return context.read();
	}

	/**
	 * @param cbuf
	 * @param off
	 * @param len
	 * @return
	 * @throws IOException
	 * @see org.jfugue.parsers.ParserContext#read(char[], int, int)
	 */
	public int read(char[] cbuf, int off, int len) throws IOException {
		return context.read(cbuf, off, len);
	}

	/**
	 * @return
	 * @throws IOException
	 * @see org.jfugue.parsers.ParserContext#readChar()
	 */
	public char readChar() throws IOException {
		return context.readChar();
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
	 * @see org.jfugue.parsers.ParserContext#readOneOfTheChars(char[])
	 */
	public char readOneOfTheChars(char... cs) throws IOException,
			JFugueException, ParserError {
		return context.readOneOfTheChars(cs);
	}

	/**
	 * @return
	 * @throws IOException
	 * @throws ParserError
	 * @see org.jfugue.parsers.ParserContext#checkReady()
	 */
	public boolean checkReady() throws IOException, ParserError {
		return context.checkReady();
	}

	/**
	 * @param cbuf
	 * @return
	 * @throws IOException
	 * @see java.io.Reader#read(char[])
	 */
	public int read(char[] cbuf) throws IOException {
		return context.read(cbuf);
	}

	/**
	 * @param target
	 * @return
	 * @throws IOException
	 * @see java.io.Reader#read(java.nio.CharBuffer)
	 */
	public int read(CharBuffer target) throws IOException {
		return context.read(target);
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
	 * @param cs
	 * @return
	 * @throws IOException
	 * @throws JFugueException
	 * @throws ParserError
	 * @see org.jfugue.parsers.ParserContext#readCharThenByte(char[])
	 */
	public CharThen<Byte> readCharThenByte(char... cs) throws IOException,
			JFugueException, ParserError {
		return context.readCharThenByte(cs);
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
	
	@Test
	public void testReadToken() throws Exception {
		String exp = "foo";
		String expplus = "foo bar";
		setString(expplus);
		assertEquals(exp, readToken(' '));
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

	/**
	 * @param unreadLastChar
	 * @param cs
	 * @return
	 * @throws IOException
	 * @throws ParserError
	 * @see org.jfugue.parsers.ParserContext#readToken(boolean, char[])
	 */
	public String readToken(boolean unreadLastChar, char... cs)
			throws IOException, ParserError {
		return context.readToken(unreadLastChar, cs);
	}

	/**
	 * @param cs
	 * @return
	 * @throws IOException
	 * @throws ParserError
	 * @see org.jfugue.parsers.ParserContext#readToken(char[])
	 */
	public String readToken(char... cs) throws IOException, ParserError {
		return context.readToken(cs);
	}

	/**
	 * @param bs
	 * @param cs
	 * @return
	 * @throws IOException
	 * @throws ParserError
	 * @see org.jfugue.parsers.ParserContext#readUnicodeAndChars(byte[], char[])
	 */
	public String readUnicodeAndChars(byte[] bs, char... cs)
			throws IOException, ParserError {
		return context.readUnicodeAndChars(bs, cs);
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
