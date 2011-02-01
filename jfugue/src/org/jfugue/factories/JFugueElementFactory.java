package org.jfugue.factories;

import java.io.IOException;
import java.io.PushbackReader;

import org.jfugue.Environment;
import org.jfugue.JFugueElement;

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
	 * Create a {@link JFugueElement} of type {@code T} if possible from the
	 * {@code reader}, else throw an {@link IllegalArgumentException}.
	 * 
	 * @param reader the reader to be to be parsed
	 * @return an instance of {@code T}
	 * @throws IllegalArgumentException
	 * @throws IOException 
	 */
	public abstract T parseElement(PushbackReader reader, Environment environment) throws IllegalArgumentException, IOException;

	/**
	 * This should just return {@code T.class}. Unfortunately we have to get a
	 * concrete class who knows what {@code T} is.
	 * 
	 * @return {@code T.class}
	 */
	public abstract Class<T> type();

}
