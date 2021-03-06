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

package org.jfugue.elements;

import java.io.IOException;

import org.jfugue.JFugueException;
import org.jfugue.factories.JFugueElementFactory;
import org.jfugue.parsers.ParserContext;
import org.jfugue.parsers.ParserError;
import org.jfugue.visitors.ElementVisitor;

/**
 * Represents tempo changes.  Tempo is kept for the whole
 * song, and is independent of tracks.  You may change the
 * tempo during a song.
 *
 *@author David Koelle
 *@version 3.0
 */
public final class PolyphonicPressure implements JFugueElement
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte key;
    private byte pressure;

    /**
     * Creates a new polyphonic pressure object, with the specified key and pressure values.
     * @param key the key to apply pressure to
     * @param pressure the pressure to apply
     */
    public PolyphonicPressure(byte key, byte pressure)
    {
        setKey(key);
        setPressure(pressure);
    }

    /**
     * Sets the key value of this object.
     * @param key the key for this object
     */
    public void setKey(byte key)
    {
        this.key = key;
    }

    /**
     * Sets the pressure value of this object.
     * @param pressure the pressure for this object
     */
    public void setPressure(byte pressure)
    {
        this.pressure = pressure;
    }

    /**
     * Returns the key for this object.
     * @return the key for this object
     */
    public byte getKey()
    {
        return this.key;
    }

    /**
     * Returns the pressure for this object.
     * @return the pressure for this object
     */
    public byte getPressure()
    {
        return this.pressure;
    }

    /**
     * Returns the Music String representing this element and all of its settings.
     * For a polyphonic pressure object, the Music String is <code>*</code><i>key,pressure</i>
     * @return the Music String for this element
     */
    public String getMusicString()
    {
        StringBuffer buffy = new StringBuffer();
        buffy.append("*");
        buffy.append(getKey());
        buffy.append(",");
        buffy.append(getPressure());
        return buffy.toString();
    }

    /**
     * Returns verification string in this format:
     * PolyphonicPressure: key={#}, pressure={#}
     */
    public String getVerifyString()
    {
        StringBuffer buffy = new StringBuffer();
        buffy.append("PolyphonicPressure: key=");
        buffy.append(getKey());
        buffy.append(", pressure=");
        buffy.append(getPressure());
        return buffy.toString();
    }
    
    public void acceptVisitor(ElementVisitor visitor) {
    	visitor.visit(this);
    }


	public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PolyphonicPressure other = (PolyphonicPressure) obj;
        if (this.key != other.key) {
            return false;
        }
        if (this.pressure != other.pressure) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.key;
        hash = 29 * hash + this.pressure;
        return hash;
    }

	public static class Factory extends
			JFugueElementFactory<PolyphonicPressure> {
	
		private static PolyphonicPressure.Factory instance;
		private Factory() {}
		public static PolyphonicPressure.Factory getInstance() {
			if (instance == null)
				instance = new PolyphonicPressure.Factory();
			return instance;
		}
		
		public PolyphonicPressure createElement(ParserContext context)
				throws IOException, IllegalArgumentException, JFugueException,
				ParserError {
			context.readPastWhitespace();
			context.readChar('*');
			byte key = context.readByte();
			context.readChar(',');
			byte pressure = context.readByte();
			return context.firePolyphonicPressureEvent(new PolyphonicPressure(key, pressure));
		}
	
		public Class<PolyphonicPressure> type() {
			return PolyphonicPressure.class;
		}
	
	}

}