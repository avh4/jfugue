/**
 * 
 */
package org.jfugue.parsers.combinatorial;

import java.io.IOException;

import org.jfugue.JFugueException;
import org.jfugue.parsers.ParserContext;
import org.jfugue.parsers.ParserError;

/**
 * @author joshua
 *
 */
public interface ParserNode<T> {
	public T execute(ParserContext context) throws ParserError, IOException, JFugueException;
}
