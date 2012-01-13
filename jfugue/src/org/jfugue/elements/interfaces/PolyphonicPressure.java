package org.jfugue.elements.interfaces;

public interface PolyphonicPressure extends Element {
    /**
     * Returns the key for this object.
     * @return the key for this object
     */
    public ByteExp getKey();

    /**
     * Returns the pressure for this object.
     * @return the pressure for this object
     */
    public ByteExp getPressure();

}
