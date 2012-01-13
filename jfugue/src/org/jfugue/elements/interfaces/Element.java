package org.jfugue.elements.interfaces;

import org.jfugue.Environment;
import org.jfugue.elements.JFugueElement;

public interface Element {

	public JFugueElement eval(Environment environment);
	public String toMusicString();
	public void accept(ElementVisitor vistior);

}
