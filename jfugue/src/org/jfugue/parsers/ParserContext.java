package org.jfugue.parsers;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.jfugue.Environment;
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
 * This class wraps a PushbackReader and Environment and provides helper methods to parsers and factories.  It also has readByte, readInt, readLong, and readDouble methods that take dictionary elements.
 * 
 * <p>Warning: Be careful about passing this class around as it exposes the fire methods from the parser.
 * 
 * @author joshua
 *
 */
public class ParserContext extends FilterReader {

	public static final int CAPACITY = 10;

	protected Reader reader;
	protected Environment environment;
	protected Scanner scanner;
	protected StringBuilder sb = new StringBuilder(CAPACITY);
	
	protected byte keySig = 0;
	public static final double SEQUENCE_RES = 128;
	
	public static final String HEX_BYTE_RE = "^[0-1A-F]{2}";
	public static final String BYTE_RE = "^(?:-?(?:1[01]\\d|12[0-7]|\\d\\d?)|-128)";
	public static final String INT_RE = "^\\d+";
	public static final String LONG_RE = "^\\d+";
	public static final String DOUBLE_RE = "^\\d+\\.\\d+";
	public static final String SYMBOL_RE = "[A-Za-z][A-Za-z_]*";
	public static final String BRACKETED_SYMBOL_RE = "^\\[[A-Za-z][A-Za-z_]*\\]";
	
	public static final Pattern HEX_BYTE_PAT = Pattern.compile(HEX_BYTE_RE, Pattern.CASE_INSENSITIVE);
	public static final Pattern BYTE_PAT = Pattern.compile(BYTE_RE, Pattern.CASE_INSENSITIVE);
	
	/**
	 * @return the keySig
	 */
	public byte getKeySig() {
		return keySig;
	}

	/**
	 * @param keySig the keySig to set
	 */
	public void setKeySig(byte keySig) {
		this.keySig = keySig;
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
//		this.reader = reader;
//		scanner = new Scanner(reader);
		this.environment = environment;
	}
	
	/////////// Implementation of FilterReader ///////////
	public boolean ready() throws IOException {
		return super.ready() || sb.length() > 0;
	}
	
	public int read() throws IOException {
		if (sb.length() != 0) {
			char ch = sb.charAt(0);
			sb.deleteCharAt(0);
			return (int) ch;
		}
		return super.read();
	}

	public int read(char[] cbuf, int off, int len) throws IOException {
		int blen = sb.length();
		int overflow = len - blen;
		int cutoff = (overflow > 0) ? blen : len;
		int count = cutoff;
		if (cutoff > 0) {
			sb.getChars(0, cutoff, cbuf, off);
			sb.delete(0, cutoff);
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
		sb.insert(0, ch);
	}
	
	public void unread(int cp) throws IOException {
		unread((char) cp);
	}
	
	public void unread(CharSequence cs) {
		sb.insert(0, cs);	
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
		} else
			unread(cp);
			return "[" + readIdentifier() + "]";
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
	 * @return
	 * @throws JFugueException
	 * @throws IOException
	 * @throws ParserError 
	 */
	public byte readByte() throws JFugueException, IOException, ParserError {
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

	public byte readHexByte() {
		return Byte.parseByte(scanner.findInLine(HEX_BYTE_PAT), 16);
	}
	
	/**
	 * Tries to read an int or a symbol that resolves to an int.
	 * 
	 * @return
	 * @throws JFugueException
	 * @throws IOException
	 * @throws ParserError 
	 */
	public int readInt() throws JFugueException, IOException, ParserError {
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
			return Integer.parseInt(sb.toString());
		} else {
			unread(cp);
			return environment.getIntFromDictionary(readSymbol());
		}
	}

	/**
	 * Tries to read a long or a symbol that resolves to a long.
	 * 
	 * @return
	 * @throws JFugueException
	 * @throws IOException
	 * @throws ParserError 
	 */
	public long readLong() throws JFugueException, IOException, ParserError {
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
	 * @return
	 * @throws JFugueException
	 * @throws IOException
	 * @throws ParserError 
	 */
	public double readDouble() throws JFugueException, IOException, ParserError {
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
	
	public String readUnicodeAndChars(byte[] bs, char...cs) throws IOException, ParserError {
		StringBuilder sb = new StringBuilder(CAPACITY);
		char ch = readChar();
		boolean match = false;
		while (true) {
			match = false;
			for (byte b : bs) {
				if (Character.getType(ch) == b) {
					match = true;
					break;
				}
			}
			if (match == true) {
				sb.append(ch);
				match = false;
				continue;
			}
			for (char c : cs) {
				if (c == ch) {
					match = true;
					break;
				}
			}
			if (match == true) {
				sb.append(ch);
				match = false;
				continue;
			}
			unread(ch);
			break;
		}
		return sb.toString();
	}

	public char readOneOfTheChars(char...cs) throws IOException, JFugueException, ParserError {
			checkReady();
			char ch = readChar();
			for (char d : cs) {
				if (ch == d)
					return d;
			}
			unread(ch);
			throw new ParserError(ParserError.CHAR_UNEXPECTED, ch, cs.toString());
	}

	public boolean checkReady() throws IOException, ParserError {
		if (!ready())
			throw new ParserError(ParserError.NOT_READY);
		else
			return true;
	}

	// TODO Make a simple set of combinatorial parsers
	
	/**
	 * Tries to read one of the {@code cs} and then a byte.
	 * 
	 * @param cs the acceptable chars
	 * @return
	 * @throws IOException
	 * @throws ParserError 
	 * @throws JFugueException 
	 */
	public CharThen<Byte> readCharThenByte(char... cs) throws IOException, JFugueException, ParserError {
		char c = readOneOfTheChars(cs);
		return new CharThen<Byte>(c, readByte());
	}

	public static class CharThen<T> {
		protected char ch;

		public char getChar() {
			return ch;
		}

		protected T then;

		public T getThen() {
			return then;
		}

		/**
		 * @param ch
		 * @param then
		 */
		public CharThen(char ch, T then) {
			super();
			this.ch = ch;
			this.then = then;
		}

	}
	
//	@SuppressWarnings("serial")
//	public static class ParserError extends Exception {
//		public ParserError(String format, Object...objects) {
//			super(String.format(format, objects));
//		}
//		
//		public ParserError() {
//			super();
//		}
//		
//		public ParserError(String exc) {
//			super(exc);
//		}
//	}

	public static interface ParserElement<T> {
		public T getTerminal();
	}

	public static class ParserPair<F extends ParserElement<?>, S extends ParserElement<?>>
			implements
			ParserElement<ParserPair<? extends ParserElement<?>, ? extends ParserElement<?>>> {
		protected F first;
		protected S second;

		/**
		 * @return the first
		 */
		public F getFirst() {
			return first;
		}

		/**
		 * @return the second
		 */
		public S getSecond() {
			return second;
		}

		/**
		 * @param first
		 * @param second
		 */
		public ParserPair(F first, S second) {
			super();
			this.first = first;
			this.second = second;
		}

		public ParserPair<? extends ParserElement<?>, ? extends ParserElement<?>> getTerminal() {
			return null;
		}

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
<<<<<<< .mine
	 * @see org.jfugue.Environment#fireSystemExclusiveEvent(org.jfugue.SystemExclusiveEvent)
=======
	 * @see org.jfugue.elements.Environment#fireSystemExclusiveEvent(org.jfugue.elements.SystemExclusive)
>>>>>>> .r102
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
	public byte readByte(int radix) throws JFugueException, IOException, ParserError {
//		final char[] HEX = new char[] {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		if (radix == 10)
			return readByte();
		else if (radix == 16) {
			String rb = new String(new char[] { readOneOfTheChars(HEX), readOneOfTheChars(HEX) });
			return Byte.parseByte(rb, radix);
		} else
			throw new ParserError();
	}

	public void addParserListener(ParserListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void removeParserListener(ParserListener listener) {
		// TODO Auto-generated method stub
		
	}

	public ParserListener[] getParserListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	public void clearParserListeners() {
		// TODO Auto-generated method stub
		
	}
}
