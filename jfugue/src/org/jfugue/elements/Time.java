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
 * Represents a timing value, which can be used to indicate when certain events are played.
 *
 *@author David Koelle
 *@version 3.0
 */
public final class Time implements JFugueElement
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long time;

    /**
     * Creates a new Time object, with the specified time number.
     * @param time the number of the time to use
     */
    public Time(long time)
    {
        setTime(time);
    }

    /**
     * Sets the value of the time for this object.
     * @param time the number of the time to use
     */
    public void setTime(long time)
    {
        this.time = time;
    }

    /**
     * Returns the time used in this object
     * @return the time used in this object
     */
    public long getTime()
    {
        return time;
    }

    /**
     * Returns the Music String representing this element and all of its settings.
     * For a Time object, the Music String is <code>@</code><i>time</i>
     * @return the Music String for this element
     */
    public String getMusicString()
    {
        StringBuffer buffy = new StringBuffer();
        buffy.append("@");
        buffy.append(getTime());
        return buffy.toString();
    }

    /**
     * Returns verification string in this format:
     * Time: time={#}
     * @version 4.0
     */
    public String getVerifyString()
    {
        StringBuffer buffy = new StringBuffer();
        buffy.append("Time: time=");
        buffy.append(getTime());
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
        final Time other = (Time) obj;
        if (this.time != other.time) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (this.time ^ (this.time >>> 32));
        return hash;
    }

	public static class Factory extends JFugueElementFactory<Time> {
		private static Time.Factory instance;
		private Factory() {}
		public static Time.Factory getInstance() {
			if (instance == null)
				instance = new Time.Factory();
			return instance;
		}
		
		public Time createElement(ParserContext context) throws IOException,
				IllegalArgumentException, JFugueException, ParserError {
			context.readOneOfTheChars('@');
			return context.fireTimeEvent(new Time(context.readLong()));
		}
	
		public Class<Time> type() {
			return Time.class;
		}
	
	}

}