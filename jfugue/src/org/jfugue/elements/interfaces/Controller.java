package org.jfugue.elements.interfaces;

public interface Controller extends Element {
	public ByteExp getIndex();
	public ByteExp getValue();
	public Controller getCoarseController();
}
