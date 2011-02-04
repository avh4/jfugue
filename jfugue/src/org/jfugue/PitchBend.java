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

import java.io.IOException;

import org.jfugue.factories.JFugueElementFactory;
import org.jfugue.parsers.ParserContext;
import org.jfugue.parsers.ParserError;

/**
 * Represents pitch bend changes.
 *
 *@author David Koelle
 *@version 3.0
 */
public final class PitchBend implements JFugueElement
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte lsb;
    private byte msb;

    /**
     * Creates a new Pitch Bend object, with the specified tempo value.
     * Integer value = msb * 0x80 + lsb   (0x80 hex == 128 dec)
     * @param lsb the least significant byte for the pitch bend for this object
     * @param msb the most significant byte for the pitch bend for this object
     */
    public PitchBend(byte lsb, byte msb)
    {
        setPitchBend(lsb, msb);
    }

    /**
     * Sets the value of the pitch bend for this object.
     * @param tempo the pitch bend for this object
     */
    public void setPitchBend(byte lsb, byte msb)
    {
        this.lsb = lsb;
        this.msb = msb;
    }

    /**
     * Returns the value of the pitch bend for this object.
     * @return the value of the pitch bend for this object
     */
    public byte[] getBend()
    {
        return new byte[] { lsb, msb };
    }

    /**
     * Returns the Music String representing this element and all of its settings.
     * For a PitchBend object, the Music String is <code>&</code><i>int</i> or <code>&</code><i>lsb,msb</i>
     * @return the Music String for this element
     */
    public String getMusicString()
    {
        StringBuffer buffy = new StringBuffer();
        buffy.append("&");
        buffy.append(getBend()[1] * 0x80 + getBend()[0]);
        return buffy.toString();
    }

    /**
     * Returns verification string in this format:
     * PitchBend: bend={#}
     * @version 4.0
     */
    public String getVerifyString()
    {
        StringBuffer buffy = new StringBuffer();
        buffy.append("PitchBend: bend=");
        buffy.append(getBend());
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
        final PitchBend other = (PitchBend) obj;
        if (this.lsb != other.lsb) {
            return false;
        }
        if (this.msb != other.msb) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + this.lsb;
        hash = 43 * hash + this.msb;
        return hash;
    }

	public static class Factory extends JFugueElementFactory<PitchBend> {
		private static PitchBend.Factory instance;
		private Factory() {}
		public static PitchBend.Factory getInstance() {
			if (instance == null)
				instance = new PitchBend.Factory();
			return instance;
		}
	
		public PitchBend createElement(ParserContext context)
				throws IOException, IllegalArgumentException, JFugueException,
				ParserError {
//	        // A PitchBend token looks like one of the following:
//	        //      &lsb,msb
//	        //      &int
//	        //
//	        // where "byte1" and "byte2" or "int" can be bytes/ints or dictionary items
//
//	        byte lsb = 0;
//	        byte msb = 0;
//
//	        if (s.indexOf(',') > -1) {
//	            // We're dealing with two bytes
//	            String b1String = s.substring(1,s.indexOf(','));
//	            lsb = getByteFromDictionary(b1String);
//
//	            String b2String = s.substring(s.indexOf(',')+1, s.length());
//	            msb = getByteFromDictionary(b2String);
//	        } else {
//	            // We're dealing with a single integer, which we will break into bytes
//	            String valueString = s.substring(1,s.length());
//	            int value = getIntFromDictionary(valueString);
//	            lsb = (byte)(value % 128);
//	            msb = (byte)(value / 128);
//	        }
			context.readOneOfTheChars('&');
			int i = context.readInt();
			byte lsb, msb;
			if (i < 127) {
				lsb = (byte) i;
				context.readOneOfTheChars(',');
				msb = context.readByte();
			} else {
				lsb = (byte)(i % 128);
				msb = (byte)(i / 128);
			}
			return context.firePitchBendEvent(new PitchBend(lsb, msb));
		}
	
		public Class<PitchBend> type() {
			return PitchBend.class;
		}
	
	}

}