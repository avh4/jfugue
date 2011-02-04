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
 * Represents layer changes.  A Layer allows multiple sounds to be played at the same
 * time on a single track (also known as a voice), without those notes being specified
 * as a chord.  This is particularly helpful when sing Track 9, the percussion track,
 * so multiple percussion sounds can occur at the same time.
 *
 *@author David Koelle
 *@version 3.0
 */
public final class Layer implements JFugueElement
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte layer;

    /**
     * Creates a new Layer object, with the specified layer number.
     * @param layer the number of the layer to use
     */
    public Layer(byte layer)
    {
        setLayer(layer);
    }

    /**
     * Sets the value of the layer for this object.
     * @param layer the number of the layer to use
     */
    public void setLayer(byte layer)
    {
        this.layer = layer;
    }

    /**
     * Returns the layer used in this object
     * @return the layer used in this object
     */
    public byte getLayer()
    {
        return layer;
    }

    /**
     * Returns the Music String representing this element and all of its settings.
     * For a Layer object, the Music String is <code>L</code><i>layer-number</i>
     * @return the Music String for this element
     */
    public String getMusicString()
    {
        StringBuffer buffy = new StringBuffer();
        buffy.append("L");
        buffy.append(getLayer());
        return buffy.toString();
    }

    /**
     * Returns verification string in this format:
     * Layer: layer={#}
     * @version 4.0
     */
    public String getVerifyString()
    {
        StringBuffer buffy = new StringBuffer();
        buffy.append("Layer: layer=");
        buffy.append(getLayer());
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
        final Layer other = (Layer) obj;
        if (this.layer != other.layer) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + this.layer;
        return hash;
    }

	public static class Factory extends JFugueElementFactory<Layer> {
		private static Layer.Factory instance;
		private Factory() {}
		public static Layer.Factory getInstance() {
			if (instance == null)
				instance = new Layer.Factory();
			return instance;
		}
	
		public Layer createElement(ParserContext context) throws IOException,
				IllegalArgumentException, JFugueException, ParserError {
			return context.fireLayerEvent(new Layer(context.readCharThenByte('L', 'l').getThen()));
		}
	
		public Class<Layer> type() {
			return Layer.class;
		}
	
	}


}