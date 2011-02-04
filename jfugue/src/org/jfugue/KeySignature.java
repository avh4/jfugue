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
 * Represents key signature changes.  Key signatures are kept for the
 * whole song, independent of tracks.  You may change the key signature
 * during a song.  The Player will automatically adjust note values based
 * on the current key signature - for example, in an F Major key signature,
 * B will be converted to B-flat automatically, unless the B is noted as
 * a natural B (i.e., "Bn")
 *
 *@author David Koelle
 *@version 3.0
 */
public final class KeySignature implements JFugueElement
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte keySig;
    private byte scale;

    /**
     * Creates a new key signature object, with the specified tempo value.
     * @param keySig the key signature for this object, -7 to +7 - see MIDI specification for more details
     * @param scale whether this is a major (0) or minor (1) key signature
     */
    public KeySignature(byte keySig, byte scale)
    {
        setKeySig(keySig);
        setScale(scale);
    }

    /**
     * Sets the key signature, from -7 to +7, for this object.
     * See the MIDI specification for more details
     * @param keySig the key signature for this object
     */
    public void setKeySig(byte keySig)
    {
        this.keySig = keySig;
    }

    /**
     * Sets the scale - 0 for major, 1 for minor.
     * See the MIDI specification for more details
     * @param scale the scale for this object
     */
    public void setScale(byte scale)
    {
        this.scale = scale;
    }

    /**
     * Returns the key signature for this object.
     * @return the key signature for this object
     */
    public byte getKeySig()
    {
        return this.keySig;
    }

    /**
     * Returns the scale for this object.
     * @return the scale for this object
     */
    public byte getScale()
    {
        return this.scale;
    }

    /**
     * Returns the Music String representing this element and all of its settings.
     * For a key signature object, the Music String is <code>K</code><i>keysig</i>,
     * where 'keysig' is a root note followed by 'maj' or 'min' (i.e., Cbmaj for C-flat major)
     * @return the Music String for this element
     */
    public String getMusicString()
    {
        StringBuilder keySigSB = new StringBuilder();
        keySigSB.append("K");
        if (scale == 0) {
            keySigSB.append(majorSigs[getKeySig()+7]);
            keySigSB.append("maj");
        } else {
            keySigSB.append(minorSigs[getKeySig()+7]);
            keySigSB.append("min");
        }
        return keySigSB.toString();
    }

    /**
     * Returns verification string in this format:
     * KeySig: keySig={#}, scale={#}
     * @version 4.0
     */
    public String getVerifyString()
    {
        StringBuffer buffy = new StringBuffer();
        buffy.append("KeySig: keySig=");
        buffy.append(getKeySig());
        buffy.append(", scale=");
        buffy.append(getScale());
        return buffy.toString();
    }
    
    public void acceptVisitor(ElementVisitor visitor) {
    	visitor.visit(this);
    }

    private static final String[] majorSigs = new String[] { "Cb", "Gb", "Db", "Ab", "Eb", "Bb", "F", "C", "G", "D", "A", "E", "B", "F#", "C#" };
    private static final String[] minorSigs = new String[] { "Ab", "Eb", "Bb", "F", "C", "G", "D", "A", "E", "B", "F#", "C#", "G#", "D#", "A#" };

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KeySignature other = (KeySignature) obj;
        if (this.keySig != other.keySig) {
            return false;
        }
        if (this.scale != other.scale) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.keySig;
        hash = 11 * hash + this.scale;
        return hash;
    }

    public static class Factory extends JFugueElementFactory<KeySignature> {

		private static Factory instance;

		private Factory() {
		}

		public static Factory getInstance() {
			if (instance == null)
				instance = new Factory();
			return instance;
		}

		public KeySignature createElement(ParserContext context)
				throws IOException, IllegalArgumentException, JFugueException,
				ParserError {
			context.readOneOfTheChars('K', 'k');
			String rootNote = context.readIdentifier();
			try {
				char c = context.readOneOfTheChars('#');
				rootNote = c + context.readIdentifier();
			} catch (Exception e) {}
			
			rootNote = rootNote.toUpperCase();
			if (rootNote.length() <= 4)
				throw new ParserError("The key '%s' is too short", rootNote);
			
			String majOrMinStr = rootNote.substring(rootNote.length()-4);
			byte scale;
			if ("MAJ".equals(majOrMinStr))
				scale = 0;
			else if ("MIN".equals(majOrMinStr))
				scale = 1;
			else
				throw new ParserError("The scale must be MAJ or MIN but it is '%s'", majOrMinStr);
			
	        byte keySig = 0;

	        // TODO Somehow make this less messy -ska
	        if (rootNote.equalsIgnoreCase("CBMAJ") || rootNote.equalsIgnoreCase("ABMIN")) keySig = -7;
	        else if (rootNote.equalsIgnoreCase("GBMAJ") || rootNote.equalsIgnoreCase("EBMIN")) keySig = -6;
	        else if (rootNote.equalsIgnoreCase("DBMAJ") || rootNote.equalsIgnoreCase("BBMIN")) keySig = -5;
	        else if (rootNote.equalsIgnoreCase("ABMAJ") || rootNote.equalsIgnoreCase("FMIN")) keySig = -4;
	        else if (rootNote.equalsIgnoreCase("EBMAJ") || rootNote.equalsIgnoreCase("CMIN")) keySig = -3;
	        else if (rootNote.equalsIgnoreCase("BBMAJ") || rootNote.equalsIgnoreCase("GMIN")) keySig = -2;
	        else if (rootNote.equalsIgnoreCase("FMAJ") || rootNote.equalsIgnoreCase("DMIN")) keySig = -1;
	        else if (rootNote.equalsIgnoreCase("CMAJ") || rootNote.equalsIgnoreCase("AMIN")) keySig = 0;
	        else if (rootNote.equalsIgnoreCase("GMAJ") || rootNote.equalsIgnoreCase("EMIN")) keySig = +1;
	        else if (rootNote.equalsIgnoreCase("DMAJ") || rootNote.equalsIgnoreCase("BMIN")) keySig = +2;
	        else if (rootNote.equalsIgnoreCase("AMAJ") || rootNote.equalsIgnoreCase("F#MIN")) keySig = +3;
	        else if (rootNote.equalsIgnoreCase("EMAJ") || rootNote.equalsIgnoreCase("C#MIN")) keySig = +4;
	        else if (rootNote.equalsIgnoreCase("BMAJ") || rootNote.equalsIgnoreCase("G#MIN")) keySig = +5;
	        else if (rootNote.equalsIgnoreCase("F#MAJ") || rootNote.equalsIgnoreCase("D#MIN")) keySig = +6;
	        else if (rootNote.equalsIgnoreCase("C#MAJ") || rootNote.equalsIgnoreCase("A#MIN")) keySig = +7;
	        else {
	            throw new JFugueException(JFugueException.KEYSIG_EXC, "K" + rootNote);
	        }
	        
	        return context.fireKeySignatureEvent(new KeySignature(keySig, scale));
		}

		public Class<KeySignature> type() {
			return KeySignature.class;
		}
    }
}