package org.jfugue.factories;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

import org.jfugue.JFugueElement;
import org.jfugue.JFugueException;
import org.jfugue.parsers.Environment;
import org.jfugue.parsers.ParserContext;
import org.jfugue.parsers.ParserError;

/**
 * Subclasses of this class will be responsible for making certain elements out
 * of MusicString tokens. Unfortunately, it will have to register an instance.
 * Maybe this is the wrong way to do this.
 * 
 * @author joshua
 * 
 * @param <T>
 *            the type of {@link JFugueElement} this factory produces
 */
public abstract class JFugueElementFactory<T extends JFugueElement> {

	/**
	 * This is only supposed to be called once per subclass to create a
	 * singleton that gets registered with {@link JFugueElementFactoryManager}.
	 */
	protected JFugueElementFactory() {
	}

	/**
	 * Create a {@link JFugueElement} of type {@code T} if possible from the
	 * {@code token}, else throw an exception.
	 * 
	 * @param token
	 *            the MusicString token to be parsed
	 * @return an instance of {@code T}
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws JFugueException
	 * @throws ParserError
	 */
	public T createElement(String token) throws IOException,
			IllegalArgumentException, JFugueException, ParserError {
		return createElement(new PushbackReader(new StringReader(token)),
				Environment.getInstance());
	}
	
	/**
	 * Create a {@link JFugueElement} of type {@code T} if possible from the
	 * {@code token}, else throw an exception.
	 * 
	 * @param token
	 * @param environment
	 * @return an instance of {@code T}
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws JFugueException
	 * @throws ParserError
	 */
	public T createElement(String token, Environment environment)
			throws IOException, IllegalArgumentException, JFugueException,
			ParserError {
		return createElement(new PushbackReader(new StringReader(token)),
				environment);
	}
	
	/**
	 * Create a {@link JFugueElement} of type {@code T} if possible, else
	 * throws.
	 * 
	 * @param reader
	 * @param environment
	 * @return an instance of {@code T}
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws JFugueException
	 * @throws ParserError
	 */
	public T createElement(PushbackReader reader,
			Environment environment) throws IOException,
			IllegalArgumentException, JFugueException, ParserError {
		return createElement(new ParserContext(reader, environment));
	}

	/**
	 * @param context
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws JFugueException
	 */
	public abstract T createElement(ParserContext context) throws IOException,
			IllegalArgumentException, JFugueException, ParserError;

	/**
	 * This should just return {@code T.class}. Unfortunately we have to get a
	 * concrete class who knows what {@code T} is.
	 * 
	 * @return {@code T.class}
	 */
	public abstract Class<T> type();

}
