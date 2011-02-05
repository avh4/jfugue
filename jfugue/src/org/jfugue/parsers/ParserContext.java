package org.jfugue.parsers;

import java.io.IOException;
import java.io.PushbackReader;

import org.jfugue.ChannelPressure;
import org.jfugue.Controller;
import org.jfugue.Instrument;
import org.jfugue.JFugueException;
import org.jfugue.KeySignature;
import org.jfugue.Layer;
import org.jfugue.Measure;
import org.jfugue.Note;
import org.jfugue.PitchBend;
import org.jfugue.PolyphonicPressure;
import org.jfugue.SystemExclusiveEvent;
import org.jfugue.Tempo;
import org.jfugue.Time;
import org.jfugue.Voice;

/**
 * This class wraps a PushbackReader and Environment and provides helper methods to parsers and factories.  It also has readByte, readInt, readLong, and readDouble methods that take dictionary elements.
 * 
 * <p>Warning: Be careful about passing this class around as it exposes the fire methods from the parser.
 * 
 * @author joshua
 *
 */
public class ParserContext {

	public static final int CAPACITY = 10;

	protected PushbackReader reader;
	protected Environment environment;
	
	/**
	 * @return the reader
	 */
	public PushbackReader getReader() {
		return reader;
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
	public ParserContext(PushbackReader reader, Environment environment) {
		this.reader = reader;
		this.environment = environment;
	}
	
	// TODO Make this classes methods use read and unread from this class instead of reader
	
	/**
	 * Reads a {@code char} from the {@code reader} if possible.
	 * 
	 * @return the {@code char} read
	 * @throws IOException
	 * @throws ParserError if the {@code reader} is not ready
	 */
	public char read() throws IOException, ParserError {
		if (reader.ready())
			return (char) reader.read();
		else
			throw new ParserError(ParserError.NOT_READY);
	}
	
	/**
	 * Tries to unread a {@code char}, otherwise throws.
	 * 
	 * @param ch
	 * @throws IOException
	 */
	public void unread(char ch) throws IOException {
		reader.unread((int) ch);
	}
	
	/**
	 * Tries to read a symbol for use in looking up dictionary elements.
	 * 
	 * @return the symbol read from the reader
	 * @throws JFugueException
	 * @throws IOException
	 * @throws ParserError 
	 */
	public String readSymbol() throws JFugueException, IOException, ParserError {
		final String type = "symbol";
		checkReady();
		int cp = reader.read();
		if (cp == '[') {
			String id = readIdentifier();
			cp = reader.read();
			if (cp == ']') {
				return "[" + id + "]";
			} else {
				reader.unread(cp);
				throw new ParserError(ParserError.CHAR_UNEXPECTED, (char) cp, type);
			}
		} else
			reader.unread(cp);
			return "[" + readIdentifier() + "]";
	}

	public String readIdentifier() throws IOException, ParserError {
		StringBuilder sb = new StringBuilder(CAPACITY);
		final String type = "identifier";
		char ch = read();
		if (!Character.isLetter(ch)) {
			unread(ch);
			throw new ParserError(ParserError.CHAR_NOT_START, ch, type);
		}
		while (Character.isJavaIdentifierPart(ch)) {
			sb.append(ch);
			ch = read();
		}
		unread(ch);
		return sb.toString();
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
		char ch = read();
		if (Character.isDigit(ch)) {
			sb.append(ch);
			while (checkReady()) {
				ch = read();
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
		int cp = reader.read();
		char ch = (char) cp;
		if (Character.isDigit(ch)) {
			sb.append(ch);
			while (checkReady()) {
				cp = reader.read();
				ch = (char) cp;
				if (Character.isDigit(ch))
					sb.append(ch);
				else {
					reader.unread(cp);
					break;
				}
			}
			return Integer.parseInt(sb.toString());
		} else {
			reader.unread(cp);
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
		int cp = reader.read();
		char ch = (char) cp;
		if (Character.isDigit(ch)) {
			sb.append(ch);
			while (checkReady()) {
				cp = reader.read();
				ch = (char) cp;
				if (Character.isDigit(ch))
					sb.append(ch);
				else {
					reader.unread(cp);
					break;
				}
			}
			return Long.parseLong(sb.toString());
		} else {
			reader.unread(cp);
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
		int cp = reader.read();
		char ch = (char) cp;
		if (Character.isDigit(ch)) {
			sb.append(ch);
			while (checkReady()) {
				cp = reader.read();
				ch = (char) cp;
				if (Character.isDigit(ch))
					sb.append(ch);
				else if (ch == '.') {
					sb.append(ch);
					break;
				} else {
					reader.unread(cp);
					throw new ParserError(ParserError.CHAR_UNEXPECTED, ch, type);
				}
			}
			while (checkReady()) {
				cp = reader.read();
				ch = (char) cp;
				if (Character.isDigit(ch))
					sb.append(ch);
				else {
					reader.unread(cp);
					break;
				}
			}
			return Double.parseDouble(sb.toString());
		} else {
			reader.unread(cp);
			return environment.getDoubleFromDictionary(readSymbol());
		}
	}
	
	public String readUnicodeAndChars(byte[] bs, char...cs) throws IOException, ParserError {
		StringBuilder sb = new StringBuilder(CAPACITY);
		char ch = read();
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
			char ch = read();
			for (char d : cs) {
				if (ch == d)
					return d;
			}
			unread(ch);
			throw new ParserError(ParserError.CHAR_UNEXPECTED, ch, cs.toString());
	}

	public boolean checkReady() throws IOException, ParserError {
		if (!reader.ready())
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
	 * @see org.jfugue.parsers.Environment#fireVoiceEvent(org.jfugue.Voice)
	 */
	public Voice fireVoiceEvent(Voice event) {
		environment.fireVoiceEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.parsers.Environment#fireTempoEvent(org.jfugue.Tempo)
	 */
	public Tempo fireTempoEvent(Tempo event) {
		environment.fireTempoEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.parsers.Environment#fireInstrumentEvent(org.jfugue.Instrument)
	 */
	public Instrument fireInstrumentEvent(Instrument event) {
		environment.fireInstrumentEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.parsers.Environment#fireLayerEvent(org.jfugue.Layer)
	 */
	public Layer fireLayerEvent(Layer event) {
		environment.fireLayerEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.parsers.Environment#fireTimeEvent(org.jfugue.Time)
	 */
	public Time fireTimeEvent(Time event) {
		environment.fireTimeEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.parsers.Environment#fireSystemExclusiveEvent(org.jfugue.SystemExclusiveEvent)
	 */
	public SystemExclusiveEvent fireSystemExclusiveEvent(SystemExclusiveEvent event) {
		environment.fireSystemExclusiveEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.parsers.Environment#fireKeySignatureEvent(org.jfugue.KeySignature)
	 */
	public KeySignature fireKeySignatureEvent(KeySignature event) {
		environment.fireKeySignatureEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.parsers.Environment#fireMeasureEvent(org.jfugue.Measure)
	 */
	public Measure fireMeasureEvent(Measure event) {
		environment.fireMeasureEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.parsers.Environment#fireControllerEvent(org.jfugue.Controller)
	 */
	public Controller fireControllerEvent(Controller event) {
		environment.fireControllerEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.parsers.Environment#fireChannelPressureEvent(org.jfugue.ChannelPressure)
	 */
	public ChannelPressure fireChannelPressureEvent(ChannelPressure event) {
		environment.fireChannelPressureEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.parsers.Environment#firePolyphonicPressureEvent(org.jfugue.PolyphonicPressure)
	 */
	public PolyphonicPressure firePolyphonicPressureEvent(PolyphonicPressure event) {
		environment.firePolyphonicPressureEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.parsers.Environment#firePitchBendEvent(org.jfugue.PitchBend)
	 */
	public PitchBend firePitchBendEvent(PitchBend event) {
		environment.firePitchBendEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.parsers.Environment#fireNoteEvent(org.jfugue.Note)
	 */
	public Note fireNoteEvent(Note event) {
		environment.fireNoteEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.parsers.Environment#fireSequentialNoteEvent(org.jfugue.Note)
	 */
	public Note fireSequentialNoteEvent(Note event) {
		environment.fireSequentialNoteEvent(event);
		return event;
	}

	/**
	 * @param event
	 * @see org.jfugue.parsers.Environment#fireParallelNoteEvent(org.jfugue.Note)
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
}
