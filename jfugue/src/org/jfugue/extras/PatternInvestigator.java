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

import org.jfugue.ParserListenerAdapter;
import org.jfugue.elements.Note;


/**
 * A parser listener that collects data about a given pattern.
 *
 * @author Jason Schindler
 * @version ?
 */
public class PatternInvestigator extends ParserListenerAdapter{
	
	private double shortestDuration = 10000.0d;
	private double longestDuration = 0.0d;
	private double totalDuration = 0.0d;
	
	/**
	 * Returns the shortest decimal duration of the pattern
	 * 
	 * @return double for shortest decimal duration.
     */
	public double getShortestDecimalDuration(){
		return shortestDuration;
	}
	
	/**
	 * Returns the longest decimal duration of the pattern
	 * 
	 * @return double for longest decimal duration.
     */
	public double getLongestDecimalDuration(){
		return longestDuration;
	}
	
	/**
	 * Returns the total decimal duration of the pattern
	 * 
	 * @return double for total decimal duration.
     */
	public double getTotalDecimalDuration(){
		return totalDuration;
	}
	
	/**
	 * Called on note event.
	 * 
	 * @param note from note event.
     */
	@Override
	public void noteEvent(Note note){
		evaluateNoteDuration(note);
	}
	
	/**
	 * Called on sequential note event.
	 * 
	 * @param note from sequential note event.
     */
	@Override
	public void sequentialNoteEvent(Note note){ 
    	evaluateNoteDuration(note);
    }
    
	/**
	 * Called on parallel note event.
	 * 
	 * @param note from parallel note event.
     */
    @Override
    public void parallelNoteEvent(Note note){ 
    	evaluateNoteDuration(note);
    }
	
    /**
	 * Performs evaluations for notes.
	 * 
	 * @param note to be evaluated.
     */
    private void evaluateNoteDuration(Note note){
		double thisNoteDuration = note.getDecimalDuration();
		totalDuration+=thisNoteDuration;
		if(thisNoteDuration < shortestDuration){
			shortestDuration = thisNoteDuration;
		}
		if(thisNoteDuration > longestDuration){
			longestDuration = thisNoteDuration;
		}
	}
}