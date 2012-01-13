package org.jfugue.elements.interfaces;

public interface KeySignature extends Element {
	
    /**
     * Returns the key signature for this object.
     * @return the key signature for this object
     */
	public ByteExp getKeySig();

    /**
     * Returns the scale for this object.
     * @return the scale for this object
     */
    public ByteExp getScale();
}
