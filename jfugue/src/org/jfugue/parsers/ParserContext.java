package org.jfugue.parsers;

import static org.jfugue.JFugueException.PARSE_CHAR_ERROR;

import java.io.IOException;
import java.io.PushbackReader;

import org.jfugue.JFugueException;

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

	public String readSymbol() throws JFugueException, IOException {
		StringBuilder sb = new StringBuilder(CAPACITY);
		final String type = "symbol";
		if (!reader.ready())
			throw new IOException();
		int cp = reader.read();
		if (cp == '[') {
			sb.append((char) cp);
			while (reader.ready()) {
				cp = reader.read();
				if (Character.isJavaIdentifierPart(cp))
					sb.append((char) cp);
				else if (cp == ']') {
					sb.append((char) cp);
					return sb.toString();
				} else {
					reader.unread(cp);
					throw new JFugueException(PARSE_CHAR_ERROR, (char) cp, type);
				}
			}
		}
		reader.unread(cp);
		throw new JFugueException(PARSE_CHAR_ERROR, (char) cp, type);
	}

	public byte readByte() throws JFugueException, IOException {
		StringBuilder sb = new StringBuilder(CAPACITY);
		if (!reader.ready())
			throw new IOException();
		int cp = reader.read();
		char ch = (char) cp;
		if (Character.isDigit(ch)) {
			sb.append(ch);
			while (reader.ready()) {
				cp = reader.read();
				ch = (char) cp;
				if (Character.isDigit(ch))
					sb.append(ch);
				else {
					reader.unread(cp);
					break;
				}
			}
			return Byte.parseByte(sb.toString());
		} else {
			reader.unread(cp);
			return environment.getByteFromDictionary(readSymbol());
		}
		// else if (ch == '[') {
		// sb.append(ch);
		// while (reader.ready()) {
		// cp = reader.read();
		// ch = (char) cp;
		// if (Character.isJavaIdentifierPart(ch))
		// sb.append(ch);
		// else if (ch == ']') {
		// sb.append(ch);
		// break;
		// } else {
		// reader.unread(cp);
		// throw new JFugueException(PARSE_CHAR_ERROR, ch, "byte");
		// }
		// }
		// return environment.getByteFromDictionary(sb.toString());
		// } else {
		// reader.unread(cp);
		// throw new JFugueException(PARSE_CHAR_ERROR, ch, "byte");
		// }
	}

	public int readInt() throws JFugueException, IOException {
		StringBuilder sb = new StringBuilder(CAPACITY);
		if (!reader.ready())
			throw new IOException();
		int cp = reader.read();
		char ch = (char) cp;
		if (Character.isDigit(ch)) {
			sb.append(ch);
			while (reader.ready()) {
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

	public long readLong() throws JFugueException, IOException {
		StringBuilder sb = new StringBuilder(CAPACITY);
		if (!reader.ready())
			throw new IOException();
		int cp = reader.read();
		char ch = (char) cp;
		if (Character.isDigit(ch)) {
			sb.append(ch);
			while (reader.ready()) {
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

	public double readDouble() throws JFugueException, IOException {
		StringBuilder sb = new StringBuilder(CAPACITY);
		final String type = "double";
		if (!reader.ready())
			throw new IOException();
		int cp = reader.read();
		char ch = (char) cp;
		if (Character.isDigit(ch)) {
			sb.append(ch);
			while (reader.ready()) {
				cp = reader.read();
				ch = (char) cp;
				if (Character.isDigit(ch))
					sb.append(ch);
				else if (ch == '.') {
					sb.append(ch);
					break;
				} else {
					reader.unread(cp);
					throw new JFugueException(PARSE_CHAR_ERROR, ch, type);
				}
			}
			while (reader.ready()) {
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

	public CharThen<Byte> readCharThenByte(char... cs) throws IOException {
		if (!reader.ready())
			throw new IOException();
		int cp = reader.read();
		for (char c : cs) {
			if (c == cp) {
				return new CharThen<Byte>(c, readByte());
			}
		}
		throw new JFugueException(JFugueException.PARSE_CHAR_ERROR, (char) cp,
				cs);
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
	
	@SuppressWarnings("serial")
	public static class ParserError extends Exception {
		public ParserError(String format, Object...objects) {
			super(String.format(format, objects));
		}
		
		public ParserError() {
			super();
		}
		
		public ParserError(String exc) {
			super(exc);
		}
	}

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

	public char readChar(char...cs) throws IOException, JFugueException {
		if (reader.ready()) {
			int cp = reader.read();
			for (char d : cs) {
				if (cp == d)
					return d;
			}
			reader.unread(cp);
		}
		throw new JFugueException("Could not read the char " + cs.toString());
	}
}
