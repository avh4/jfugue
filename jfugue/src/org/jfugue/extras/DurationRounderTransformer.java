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

package org.jfugue.extras;

import org.jfugue.MusicStringParser;
import org.jfugue.Note;
import org.jfugue.PatternInterface;
import org.jfugue.PatternTransformer;



/**
 * Rounds decimal durations to their nearest discrete duration value (e.g., w (1.0), h (0.5), q (0.25))
 * or dotted value. Especially useful for cleaning up music that has been played on an
 * external MIDI device.
 * 
 *@author David Koelle
 *@version 5.0
 * 
 */
public class DurationRounderTransformer extends PatternTransformer
{
	private double sensitivity;
	
    public DurationRounderTransformer(double sensitivity)
    {
    	this.sensitivity = sensitivity;
    }
    
    public void noteEvent(Note note)
    {
        updateDuration(note);
    }

    public void parallelNoteEvent(Note note)
    {
        updateDuration(note);
    }

    public void sequentialNoteEvent(Note note)
    {
        updateDuration(note);
    }

    public void updateDuration(Note note)
    {
    	double decimalDuration = note.getDecimalDuration();

    	// TODO: Make this support durations greater than a single whole note
    	double[] knownDurations = new double[] 
    	      { 1.0, 0.75, 0.5, 0.375, 0.25, 0.1875,
    			0.125, 0.09375, 0.0625, 0.046875, 0.03125, 
    			0.0234375, 0.15625, 0.1171875, 0.0078125 };
    	for (double dur : knownDurations) {
    		if ((dur - sensitivity <= decimalDuration) && (decimalDuration <= dur + sensitivity)) {
    			note.setDecimalDuration(dur);
    		}
    	}
    		
    	getResult().addElement(note);
    }
    
    /**
     * Executes this tool on a Pattern
     *
     * TODO: Pull this execute() method into a higher-level class so all tools can use it 
     * 
     * @param pattern
     * @return duration
     */
    public PatternInterface execute(PatternInterface pattern) 
    {
        MusicStringParser parser = new MusicStringParser();
        parser.addParserListener(this);
        parser.parse(pattern);
        return getResult(); 
    }
}

