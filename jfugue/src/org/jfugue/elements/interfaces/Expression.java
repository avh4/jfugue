package org.jfugue.elements.interfaces;

import org.jfugue.Environment;

public interface Expression<T> {
	public T eval(Environment environment);
	public String toString();
//	public boolean isLiteral();
//	public T getLiteral();
}
