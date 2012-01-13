package org.jfugue.parsers;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfugue.Environment;
import org.jfugue.Environment.Error;
import org.jfugue.JFugueDefinitions;
import org.jfugue.JFugueException;
import org.jfugue.ParserListener;
import org.jfugue.elements.ChannelPressure;
import org.jfugue.elements.Controller;
import org.jfugue.elements.Instrument;
import org.jfugue.elements.KeySignature;
import org.jfugue.elements.Layer;
import org.jfugue.elements.Measure;
import org.jfugue.elements.Note;
import org.jfugue.elements.PitchBend;
import org.jfugue.elements.PolyphonicPressure;
import org.jfugue.elements.SystemExclusive;
import org.jfugue.elements.Tempo;
import org.jfugue.elements.Time;
import org.jfugue.elements.Voice;

/**
 * This class wraps a Reader and Environment and provides helper methods to parsers and factories.  It also has readByte, readInt, readLong, and readDouble methods that take dictionary elements.
 * 
 * <p>Warning: Be careful about passing this class around as it exposes the fire methods from the parser.
 * 
 * @author joshua
 *
 */
public class ParserContext extends FilterReader {

	public static final int CAPACITY = 10;

	protected Environment environment;
	
	protected StringBuilder pushbackSB = new StringBuilder(CAPACITY);
	protected StringBuilder sb = new StringBuilder(CAPACITY);
	protected Matcher matcher = null;
	
	protected byte keySig = 0;
	public static final double SEQUENCE_RES = JFugueDefinitions.SEQUENCE_RESOLUTION;
	
	public static final String HEX_BYTE_RE = "\\A([0-9A-F]{2})";
	public static final String BYTE_RE = "\\A(-?(?:1[01]\\d|12[0-7]|\\d\\d?)|-128)";
	public static final String INT_RE = "\\A(-?\\d{1,10})";
	public static final String LONG_RE = "\\A(-?\\d+)";
	public static final String DOUBLE_RE = "\\A(-?\\d+(\\.\\d+)?)";
	public static final String SYMBOL_RE = "\\A([A-Za-z]\\w*)";
	public static final String BRACKETED_SYMBOL_RE = "\\A\\[([A-Za-z]\\w*)\\]";
	
	public static final Pattern HEX_BYTE_PAT = Pattern.compile(HEX_BYTE_RE, Pattern.CASE_INSENSITIVE);
	public static final Pattern BYTE_PAT = Pattern.compile(BYTE_RE, Pattern.CASE_INSENSITIVE);
	public static final Pattern INT_PAT = Pattern.compile(INT_RE, Pattern.CASE_INSENSITIVE);
	public static final Pattern LONG_PAT = Pattern.compile(LONG_RE, Pattern.CASE_INSENSITIVE);
	public static final Pattern DOUBLE_PAT = Pattern.compile(DOUBLE_RE, Pattern.CASE_INSENSITIVE);
	public static final Pattern SYMBOL_PAT = Pattern.compile(SYMBOL_RE, Pattern.CASE_INSENSITIVE);
	public static final Pattern BRACKETED_SYMBOL_PAT = Pattern.compile(BRACKETED_SYMBOL_RE, Pattern.CASE_INSENSITIVE);
	
	/**
	 * @param keySig the keySig to set
	 */
	public void setKeySig(KeySignature keySig) {
		environment.setKeySig(keySig);
	}

	/**
	 * @return the environment
	 */
	public Environment getEnvironment() {
		return environment;
	}
	
	/**
	 * @param reader
	 * @param environment
	 */
	public ParserContext(Reader reader, Environment environment) {
		super(reader);
		this.environment = environment;
	}
	
	/**
	 * @param reader the reader to set
	 */
	public void setReader(Reader reader) {
		in = reader;
	}

	/////////// Implementation of FilterReader ///////////
	public boolean ready() throws IOException {
		return super.ready() || pushbackSB.length() > 0;
	}
	
	public boolean checkReady() throws IOException, ParserError {
		if (!ready())
			throw new ParserError(ParserError.NOT_READY);
		else
			return true;
	}

	public int read() throws IOException {
		if (pushbackSB.length() != 0) {
			char ch = pushbackSB.charAt(0);
			pushbackSB.deleteCharAt(0);
			return (int) ch;
		}
		return super.read();
	}

	public int read(char[] cbuf, int off, int len) throws IOException {
		int blen = pushbackSB.length();
		int overflow = len - blen;
		int cutoff = (overflow > 0) ? blen : len;
		int count = cutoff;
		if (cutoff > 0) {
			pushbackSB.getChars(0, cutoff, cbuf, off);
			pushbackSB.delete(0, cutoff);
		}
		if (overflow > 0)
			count += super.read(cbuf, cutoff, len - cutoff);
		return count;
	}
	
	/**
	 * Reads a {@code char} if possible.
	 * 
	 * @return the {@code char} read
	 * @throws IOException
	 */
	public char readChar() throws IOException {
		return (char) read();
	}
	
	/**
	 * Tries to unread a {@code char}, otherwise throws.
	 * 
	 * @param ch
	 * @throws IOException
	 */
	public void unread(char ch) throws IOException {
		pushbackSB.insert(0, ch);
	}
	
	/**
	 * Tries to unread a code point, otherwise throws.
	 * 
	 * @param cp
	 * @throws IOException
	 */
	public void unread(int cp) throws IOException {
		unread((char) cp);
	}
	
	/**
	 * Tries to unread a {@link CharSequence}.
	 * 
	 * @param cs
	 */
	public void unread(CharSequence cs) {
		pushbackSB.insert(0, cs);	
	}
	
	/**
	 * Tries to unread an array of {@code char}.
	 * 
	 * @param cs the char array to copy back into the push back buffer
	 * @param offset the position in the array to start
	 * @param len the number of chars to unread
	 */
	public void unread(char[] cs, int offset, int len) {
		unread(CharBuffer.allocate(len).put(cs, offset, len));		
	}

	/**
	 * Tries to unread an array of {@code char}.
	 * 
	 * @param cs the char array to copy back into the push back buffer
	 */
	public void unread(char[] cs) {
		unread(cs, 0, cs.length);
	}
	
	/////////// End implementation of FilterReader ///////////
	
	/**
	 * Tries to read a symbol for use in looking up dictionary elements.
	 * 
	 * @return the symbol read
	 * @throws JFugueException
	 * @throws IOException
	 * @throws ParserError 
	 */
	public String readSymbol() throws JFugueException, IOException, ParserError {
		final String type = "symbol";
		checkReady();
		int cp = read();
		if (cp == '[') {
			String id = readIdentifier();
			cp = read();
			if (cp == ']') {
				return "[" + id + "]";
			} else {
				unread(cp);
				throw new ParserError(ParserError.CHAR_UNEXPECTED, (char) cp, type);
			}
		} else {
			unread(cp);
			String id = readIdentifier();
			return "[" + id + "]";
		}
	}

	public String readIdentifier() throws IOException, ParserError {
		StringBuilder sb = new StringBuilder(CAPACITY);
		final String type = "identifier";
		char ch = readChar();
		if (!Character.isLetter(ch)) {
			unread(ch);
			throw new ParserError(ParserError.CHAR_NOT_START, ch, type);
		}
		while (Character.isJavaIdentifierPart(ch)) {
			sb.append(ch);
			ch = readChar();
		}
		unread(ch);
		return sb.toString();
	}
	
	public String readToken(boolean unreadLastChar, char...cs) throws IOException, ParserError {
		@SuppressWarnings("unused")
		final String type = "token";
		if (cs.length == 1)
			cs = new char[] { ' ' };
		StringBuilder sb = new StringBuilder(CAPACITY);
		char ch = readChar();
		while (isNot(ch, cs)) {
			sb.append(ch);
			ch = readChar();
		}
		if (unreadLastChar)
			unread(ch);
		return sb.toString();
	}
	
	public String readToken(char...cs) throws IOException, ParserError {
		return readToken(true, cs);
	}
	
	public String readToken() throws IOException, ParserError {
		return readToken(' ', '\n', '\r');
	}

	private boolean isNot(char ch, char[] cs) {
		for (char c : cs) {
			if (c == ch)
				return false;
		}
		return true;
	}

	/**
	 * Tries to read a byte or a symbol that resolves to a byte.
	 * 
	 * @return byte
	 * @throws ParserError 
	 */
	public byte readByte() throws IOException, ParserError {
		StringBuilder sb = new StringBuilder(CAPACITY);
		char ch = readChar();
		if (Character.isDigit(ch)) {
			sb.append(ch);
			while (checkReady()) {
				ch = readChar();
				if (Character.isDigit(ch))
					sb.append(ch);
				else {
					unread(ch);
					break;
				}
			}
			return Byte.parseByte(sb.toString());
		} else {
			unread(ch);
			return environment.getByteFromDictionary(readSymbol());
		}
	}

	/**
	 * Tries to read a byte in hex format.
	 * 
	 * @return byte
	 * @throws ParserError
	 * @throws IOException
	 */
	public byte readHexByte() throws IOException, ParserError {
		char[] b = new char[2];
		read(b);
		try {
			return Byte.parseByte(String.valueOf(b), 16);
		} catch (NumberFormatException e) {
			unread(b);
			throw new ParserError(e, ParserError.EXPECTED_BYTE, new String(b));
		}
	}
	
	/**
	 * Tries to read an int or a symbol that resolves to an int.
	 * 
	 * @return int
	 * @throws ParserError 
	 */
	public int readInt() throws IOException, ParserError {
		StringBuilder sb = new StringBuilder(CAPACITY);
		try {
			while (checkReady()) {
				char ch = readChar();
				if (Character.isDigit(ch))
					sb.append(ch);
				else {
					unread(ch);
					break;
				}
			}
		} catch (IOException e) {
		}
		if (sb.length() > 0)
			return Integer.parseInt(sb.toString());
		else
			try {
				return getIntFromDictionary(readSymbol());
			} catch (Environment.Error e) {
				throw new ParserError(ParserError.EXPECTED_INT);
			}
	}

	/**
	 * Tries to read a long or a symbol that resolves to a long.
	 * 
	 * @return long
	 * @throws ParserError 
	 */
	public long readLong() throws IOException, ParserError {
		StringBuilder sb = new StringBuilder(CAPACITY);
		checkReady();
		int cp = read();
		char ch = (char) cp;
		if (Character.isDigit(ch)) {
			sb.append(ch);
			while (checkReady()) {
				cp = read();
				ch = (char) cp;
				if (Character.isDigit(ch))
					sb.append(ch);
				else {
					unread(cp);
					break;
				}
			}
			return Long.parseLong(sb.toString());
		} else {
			unread(cp);
			return environment.getLongFromDictionary(readSymbol());
		}
	}

	/**
	 * Tries to read a double or a symbol that resolves to a double.
	 * 
	 * @return double
	 * @throws ParserError 
	 */
	public double readDouble() throws IOException, ParserError {
		StringBuilder sb = new StringBuilder(CAPACITY);
		final String type = "double";
		checkReady();
		int cp = read();
		char ch = (char) cp;
		if (Character.isDigit(ch)) {
			sb.append(ch);
			while (checkReady()) {
				cp = read();
				ch = (char) cp;
				if (Character.isDigit(ch))
					sb.append(ch);
				else if (ch == '.') {
					sb.append(ch);
					break;
				} else {
					unread(cp);
					throw new ParserError(ParserError.CHAR_UNEXPECTED, ch, type);
				}
			}
			while (checkReady()) {
				cp = read();
				ch = (char) cp;
				if (Character.isDigit(ch))
					sb.append(ch);
				else {
					unread(cp);
					break;
				}
			}
			return Double.parseDouble(sb.toString());
		} else {
			unread(cp);
			return environment.getDoubleFromDictionary(readSymbol());
		}
	}
	
	/**
	 * Tries to match a regex against the first {@code horizon} chars
	 * in the input.
	 * 
	 * @param pattern the pattern to match against
	 * @param horizon the maximum number on chars to read
	 * @return the matched string
	 * @throws ParserError
	 */
	public String findWithinHorison(Pattern pattern, int horizon) throws ParserError {
		char[] cb = new char[horizon];
		int howMany = 0;
		try {
			howMany = read(cb);
		} catch (IOException e) {
			unread(cb);
			throw new ParserError(e);
		}
		String s = String.copyValueOf(cb, 0, howMany);
		matcher = pattern.matcher(s);
		if (matcher.matches()) {
			unread(cb, matcher.end(), cb.length - matcher.end());
			return matcher.group();
		} else {
			throw new ParserError(ParserError.PATTERN_NOT_MATCH_ON, pattern, s);
		}
	}

	/**
	 * Tries to match a regex against the first {@code horizon} chars
	 * in the input.
	 * 
	 * @param pattern the pattern to match against
	 * @param horizon the maximum number on chars to read
	 * @return the matched string
	 * @throws ParserError
	 */
	public String findWithinHorison(String pattern, int horizon) throws ParserError {
		return findWithinHorison(Pattern.compile(pattern), horizon);
	}
	
	public char readOneUnicodeAndChar(byte[] bs, char...cs) throws IOException, ParserError {
		char ch = readChar();
		if (cs != null)
			for (char c : cs) {
				if (ch == c)
					return ch;
			}
		if (bs != null)
			for (byte b : bs) {
				if (Character.getType(ch) == b)
					return ch;
			}
		unread(ch);
		throw new ParserError();
	}
	
	public String readUnicodeAndChars(int count, boolean strict, byte[] bs, char...cs) throws IOException, ParserError {
		StringBuilder sb = new StringBuilder(count <= 0 ? CAPACITY : count);
		int i = 0;
		ParserError err = null;
		try {
			for (i = 0; i == count; i++) {
				sb.append(readOneUnicodeAndChar(bs, cs));
			}
		} catch (Exception e) {
		} finally {
			if (strict && i != count) {
				err = new ParserError("The count was strict but wasn't met");
				unread(sb.toString());
			} else
				return sb.toString();
		}
		throw err;
	}

	public char readChar(char...cs) throws IOException, ParserError {
		checkReady();
		char ch = readChar();
		for (char d : cs) {
			if (ch == d)
				return d;
		}
		unread(ch);
		throw new ParserError(ParserError.CHAR_UNEXPECTED, ch, cs.toString());
	}
	
	public void readPastWhitespace() {
		try {
			char ch;
			while (checkReady()) {
				ch = readChar();
				if (!Character.isWhitespace(ch)) {
					unread(ch);
					break;
				}
			}
		} catch (Exception e) {
		}
	}

	

	/**
	 * @param bracketedString
	 * @return byte
	 * @throws Error
	 * @see org.jfugue.Environment#getByteFromDictionary(java.lang.String)
	 */
	public byte getByteFromDictionary(String bracketedString) throws Error {
		return environment.getByteFromDictionary(bracketedString);
	}

	/**
	 * @param bracketedString
	 * @return double
	 * @throws Error
	 * @see org.jfugue.Environment#getDoubleFromDictionary(java.lang.String)
	 */
	public double getDoubleFromDictionary(String bracketedString) throws Error {
		return environment.getDoubleFromDictionary(bracketedString);
	}

	/**
	 * @param bracketedString
	 * @return int
	 * @throws Error
	 * @see org.jfugue.Environment#getIntFromDictionary(java.lang.String)
	 */
	public int getIntFromDictionary(String bracketedString) throws Error {
		return environment.getIntFromDictionary(bracketedString);
	}

	/**
	 * @return key
	 * @see org.jfugue.Environment#getKeySig()
	 */
	public KeySignature getKeySig() {
		return environment.getKeySig();
	}

	/**
	 * @param bracketedString
	 * @return long
	 * @throws JFugueException
	 * @see org.jfugue.Environment#getLongFromDictionary(java.lang.String)
	 */
	public long getLongFromDictionary(String bracketedString)
			throws JFugueException {
		return environment.getLongFromDictionary(bracketedString);
	}

	/**
	 * @param event
	 * @see org.jfugue.Environment#fireVoiceEvent(org.jfugue.elements.Voice)
	 */
	public Voice fireVoiceEvent(Voice event) {
		environment.fireVoiceEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.Environment#fireTempoEvent(org.jfugue.elements.Tempo)
	 */
	public Tempo fireTempoEvent(Tempo event) {
		environment.fireTempoEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.Environment#fireInstrumentEvent(org.jfugue.elements.Instrument)
	 */
	public Instrument fireInstrumentEvent(Instrument event) {
		environment.fireInstrumentEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.Environment#fireLayerEvent(org.jfugue.elements.Layer)
	 */
	public Layer fireLayerEvent(Layer event) {
		environment.fireLayerEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.Environment#fireTimeEvent(org.jfugue.elements.Time)
	 */
	public Time fireTimeEvent(Time event) {
		environment.fireTimeEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.Environment#fireSystemExclusiveEvent(org.jfugue.elements.SystemExclusive)
	 */
	public SystemExclusive fireSystemExclusiveEvent(SystemExclusive event) {
		environment.fireSystemExclusiveEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.Environment#fireKeySignatureEvent(org.jfugue.elements.KeySignature)
	 */
	public KeySignature fireKeySignatureEvent(KeySignature event) {
		environment.fireKeySignatureEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.Environment#fireMeasureEvent(org.jfugue.elements.Measure)
	 */
	public Measure fireMeasureEvent(Measure event) {
		environment.fireMeasureEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.Environment#fireControllerEvent(org.jfugue.elements.Controller)
	 */
	public Controller fireControllerEvent(Controller event) {
		environment.fireControllerEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.Environment#fireChannelPressureEvent(org.jfugue.elements.ChannelPressure)
	 */
	public ChannelPressure fireChannelPressureEvent(ChannelPressure event) {
		environment.fireChannelPressureEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.Environment#firePolyphonicPressureEvent(org.jfugue.elements.PolyphonicPressure)
	 */
	public PolyphonicPressure firePolyphonicPressureEvent(PolyphonicPressure event) {
		environment.firePolyphonicPressureEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.Environment#firePitchBendEvent(org.jfugue.elements.PitchBend)
	 */
	public PitchBend firePitchBendEvent(PitchBend event) {
		environment.firePitchBendEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.Environment#fireNoteEvent(org.jfugue.elements.Note)
	 */
	public Note fireNoteEvent(Note event) {
		environment.fireNoteEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.Environment#fireSequentialNoteEvent(org.jfugue.elements.Note)
	 */
	public Note fireSequentialNoteEvent(Note event) {
		environment.fireSequentialNoteEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.Environment#fireParallelNoteEvent(org.jfugue.elements.Note)
	 */
	public Note fireParallelNoteEvent(Note event) {
		environment.fireParallelNoteEvent(event);
		return event;
	}

	protected static final char[] HEX = new char[] {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	public byte readByte(int radix) throws IOException, ParserError {
		final char[] HEX = new char[] {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		if (radix == 10)
			return readByte();
		else if (radix == 16) {
			String rb = new String(new char[] { readChar(HEX), readChar(HEX) });
			return Byte.parseByte(rb, radix);
		} else
			throw new ParserError();
	}
//	public byte readByte(int radix) throws ParserError {
//		switch (radix) {
//		case 10:
//			return readByte();
//		case 16:
//			return readHexByte();
//		default:
//			throw new ParserError();
//		}
//	}

	

	public ParserListener[] getParserListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	public void clearParserListeners() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param listener
	 * @see org.jfugue.Environment#removeParserListener(org.jfugue.ParserListener)
	 */
	public void removeParserListener(ParserListener listener) {
		environment.removeParserListener(listener);
	}

	/**
	 * @param listener
	 * @see org.jfugue.Environment#addParserListener(org.jfugue.ParserListener)
	 */
	public void addParserListener(ParserListener listener) {
		environment.addParserListener(listener);
	}
	
}
