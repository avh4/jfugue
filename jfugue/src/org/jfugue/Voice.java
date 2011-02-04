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
 * Represents voice changes, also known as <i>track changes</i>.
 *
 *@author David Koelle
 *@version 1.0
 */
public final class Voice implements JFugueElement
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte voice;

    /**
     * Creates a new Voice object, with the specified voice value.
     * @param voice the voice for this object
     */
    public Voice(byte voice)
    {
        setVoice(voice);
    }

    /**
     * Sets the value of the voice for this object.
     * @param tempo the voice for this object
     */
    public void setVoice(byte voice)
    {
        this.voice = voice;
    }

    /**
     * Returns the voice used in this object
     * @return the voice used in this object
     */
    public byte getVoice()
    {
        return voice;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Voice other = (Voice) obj;
        if (this.voice != other.voice) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.voice;
        return hash;
    }

    /**
     * Returns the Music String representing this element and all of its settings.
     * For a Voice object, the Music String is <code>V</code><i>voice</i>
     * @return the Music String for this element
     */
    public String getMusicString()
    {
        StringBuffer buffy = new StringBuffer();
        buffy.append("V");
        buffy.append(getVoice());
        return buffy.toString();
    }

    /**
     * Returns verification string in this format:
     * Voice: voice={#}
     * @version 4.0
     */
    public String getVerifyString()
    {
        StringBuffer buffy = new StringBuffer();
        buffy.append("Voice: voice=");
        buffy.append(getVoice());
        return buffy.toString();
    }
    
    public void acceptVisitor(ElementVisitor visitor) {
    	visitor.visit(this);
    }

    public static class Factory extends JFugueElementFactory<Voice> {

		private static Factory instance;

		private Factory() {
		}

		public static Factory getInstance() {
			if (instance == null)
				instance = new Factory();
			return instance;
		}

		public Voice createElement(ParserContext context)
				throws IOException, IllegalArgumentException, JFugueException,
				ParserError {
			return context.fireVoiceEvent(new Voice(context.readCharThenByte('V', 'v').getThen()));
		}

		public Class<Voice> type() {
			return Voice.class;
		}
    }
}