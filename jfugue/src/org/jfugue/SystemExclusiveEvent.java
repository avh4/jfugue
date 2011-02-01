/*
 * JFugue - API for Music Programming
 * Copyright (C) 2003-2008  David Koelle
 *
 * http://www.jfugue.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */

package org.jfugue;

/**
 * Represents a MIDI System Exclusive (SysEx) message.
 *
 *@author David Koelle
 *@version 4.1.0
 */
public final class SystemExclusiveEvent implements JFugueElement
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] data;

    /**
     * Creates a new SystemExclusiveEvent object, with the specified bytes.
     * @param data the bytes of data for this event
     */
    public SystemExclusiveEvent(byte[] data)
    {
        setBytes(data);
    }

    /**
     * Sets the bytes of data for this event
     * @param data the bytes of data for this event
     */
    public void setBytes(byte[] data)
    {
        this.data = data;
    }

    /**
     * Returns the data used by this event
     * @return the data used by this event
     */
    public byte[] getBytes()
    {
        return data;
    }

    /**
     * Returns the Music String representing this element and all of its settings.
     * For a SystemExclusiveEvent object, the Music String is <code>^</code><i>bytes_separated_with_commas</i>
     * @return the Music String for this element
     */
    public String getMusicString()
    {
        StringBuilder buddy = new StringBuilder();
        buddy.append("^");
        byte[] bytes = getBytes();
        for (int b=0; b < bytes.length; b++) {
        	buddy.append(bytes[b]);
        	if (b != bytes.length-1) {
        		buddy.append(",");
        	}
        }
        return buddy.toString();
    }

    /**
     * Returns verification string in this format:
     * Voice: voice={#}
     * @version 4.0
     */
    public String getVerifyString()
    {
        StringBuilder buddy = new StringBuilder();
        buddy.append("SysEx: bytes=");
        byte[] bytes = getBytes();
        for (int b=0; b < bytes.length; b++) {
        	buddy.append(bytes[b]);
        	if (b != bytes.length-1) {
        		buddy.append(",");
        	}
        }
        return buddy.toString();
    }
    
    public void acceptVisitor(ElementVisitor visitor) {
    	visitor.visit(this);
    }

}