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
import java.util.LinkedList;
import java.util.List;

import org.jfugue.JFugueException;
import org.jfugue.factories.JFugueElementFactory;
import org.jfugue.parsers.ParserContext;
import org.jfugue.parsers.ParserError;
import org.jfugue.visitors.ElementVisitor;

/**
 * Represents a MIDI System Exclusive (SysEx) message.
 *
 *@author David Koelle
 *@version 4.1.0
 */
public final class SystemExclusive implements JFugueElement
{
    /**
	 * @author joshua
	 *
	 */
	public static class Factory extends
			JFugueElementFactory<SystemExclusive> {
		private static SystemExclusive.Factory instance;
		private Factory() {}
		public static SystemExclusive.Factory getInstance() {
			if (instance == null)
				instance = new SystemExclusive.Factory();
			return instance;
		}

		/* (non-Javadoc)
		 * @see org.jfugue.factories.JFugueElementFactory#createElement(org.jfugue.parsers.ParserContext)
		 */
		public SystemExclusive createElement(ParserContext context)
				throws IOException, IllegalArgumentException, JFugueException,
				ParserError {
//			final char[] dec = new char[] { ',' };
//			final char[] hex = new char[] { ',', 'A', 'B', 'C', 'D', 'E', 'F' };
			context.readOneOfTheChars('^');
			String decOrHex = context.readIdentifier().toUpperCase();
			int radix;
			if ("DEC".equals(decOrHex)) {
				radix = 10;
			} else if ("HEX".equals(decOrHex)) {
				radix = 16;
			} else
				throw new ParserError();
			context.readOneOfTheChars(':');
			List<Byte> byteList = new LinkedList<Byte>();
			while (true) {
				byteList.add(context.readByte(radix));
				try {
					context.readOneOfTheChars(',');
				} catch (Exception e) {
					break;
				}
			}
			byte[] bs = new byte[byteList.size()];
			int i = 0;
			for (Byte b : byteList) {
				bs[i] = (byte) b;
				i++;
			}
			return context.fireSystemExclusiveEvent(new SystemExclusive(bs));
		}

		/* (non-Javadoc)
		 * @see org.jfugue.factories.JFugueElementFactory#type()
		 */
		public Class<SystemExclusive> type() {
			return SystemExclusive.class;
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] data;

    /**
     * Creates a new SystemExclusiveEvent object, with the specified bytes.
     * @param data the bytes of data for this event
     */
    public SystemExclusive(byte[] data)
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
        buddy.append("^DEC:");
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