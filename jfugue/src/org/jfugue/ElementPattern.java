/**
 * 
 */
package org.jfugue;

import java.util.List;

import org.jfugue.elements.JFugueElement;

/**
 * @author joshua
 *
 */
@SuppressWarnings("serial")
public class ElementPattern extends Pattern {

	protected List<JFugueElement> elements;
	
	/**
	 * 
	 */
	public ElementPattern() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param musicString
	 */
	public ElementPattern(String musicString) {
		super(musicString);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param strings
	 */
	public ElementPattern(String... strings) {
		super(strings);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pattern
	 */
	public ElementPattern(Pattern pattern) {
		super(pattern);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param elements
	 */
	public ElementPattern(JFugueElement... elements) {
		super(elements);
		// TODO Auto-generated constructor stub
	}

}
