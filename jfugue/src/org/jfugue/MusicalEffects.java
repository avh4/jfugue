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

import org.jfugue.extras.DurationPatternTransformer;
import org.jfugue.extras.PatternInvestigator;

/**
 * EXPERIMENTAL -
 * Contains a variety of static methods that create Patterns
 * that play various musical ornaments
 *
 * @author David Koelle
 * @version 4.0
 */
public class MusicalEffects
{
    
	public static final char EFFECT_DIRECTION_UP = 'U';
	public static final char EFFECT_DIRECTION_DOWN = 'D';
	
	private static final double THIRTY_SECOND = 0.03125d;
	
	/**
     * Returns a Pattern that plays two notes in rapid succession
     * (for a total of each note being played numHammers times)
     * over the given duration.
     *
     * The resulting Pattern will have note1 and note2 both represented numHammers times.
     *
     * Example:
     * hammerOn(new Note("C5"), new Note("E5"), 0.5, 4);
     * will produce this Pattern: [60]/0.125 [64]/0.125 [60]/0.125 [64]/0.125 [60]/0.125 [64]/0.125 [60]/0.125 [64]/0.125
     *
     * @param note1 First note to play
     * @param note2 Second note to play
     * @param duration Value representing total duration for the resulting pattern.  1.0=whole note
     * @param numHammers Number of times to repeat each note
     */
    public static Pattern hammerOn(Note note1, Note note2, double duration, int numHammers)
    {
        StringBuilder buddy = new StringBuilder();
        double durationPerHammer = duration / numHammers;
        buddy.append("[");
        buddy.append(note1.getValue());
        buddy.append("]/");
        buddy.append(durationPerHammer / 2.0);
        buddy.append(" [");
        buddy.append(note2.getValue());
        buddy.append("]/");
        buddy.append(durationPerHammer / 2.0);

        Pattern pattern = new Pattern(buddy.toString());
        pattern.repeat(numHammers);
        return pattern;
    }

    /**
     * Returns a Pattern that plays a slide between two notes
     * over the given duration.
     *
     * TODO: This is currently a naive implementation, which sounds 'numSteps' notes, each
     * with a duration of 'duration/numSteps'.  This means that if you're sliding from a F to a G,
     * for example, you could get music that looks like F F F F F F F F G G G G G G, with each note
     * having a very short duration.  The problem with this is that the sound of each note stopping and
     * starting again is noticeable.   A more intelligent implementation would sound each note for as
     * long as necessary, then sound a different note only when the microtonal math requires it.  Otherwise,
     * the pitch wheel messages should cause the note to change while it is playing.
     * This implementation may require one or more new methods in MicrotoneNotation.
     */
    public static Pattern slide(Note note1, Note note2, double duration, int numSteps)
    {
        StringBuilder buddy = new StringBuilder();
        double durationPerStep = duration / numSteps;
        double freq1 = Note.getFrequencyForNote(note1.getValue());
        double freq2 = Note.getFrequencyForNote(note2.getValue());
        double differencePerStep = (freq2-freq1) / numSteps;

        for (int i=0; i < numSteps; i++)
        {
            buddy.append(MicrotoneNotation.convertFrequencyToMusicString(freq1));
            buddy.append("/");
            buddy.append(durationPerStep);
            buddy.append(MicrotoneNotation.getResetPitchWheelString());
            buddy.append(" ");
            freq1 += differencePerStep;
        }

        Pattern pattern = new Pattern(buddy.toString());
        return pattern;
    }

    /**
     * Right now, this is a pass-through to hammerOn()
     * @see hammerOn
     */
    public static Pattern trill(Note note1, Note note2, double duration, int numSteps)
    {
        return hammerOn(note1, note2, duration, numSteps);
    }
    
    /**
     * Returns a Pattern that adds a grace note to a given note.  This method assumes a
     * 32nd sound duration for the grace note.
     * 
     * @param graceNote Note value for grace note.
     * @param baseNote Note value and duration.
     * @return Pattern for the calculated effect.
     */
    public static Pattern graceNote(Note graceNote, Note baseNote){
    	return graceNote(graceNote, baseNote, THIRTY_SECOND);
    }
    
    /**
     * Returns a Pattern that adds a grace note with a given sound duration to a given note.
     * 
     * @param graceNote Note value for grace note.
     * @param baseNote Note value and duration.
     * @param graceNoteDuration Duration for the sound of the grace note.
     * @return Pattern for the calculated effect.
     */
    public static Pattern graceNote(Note graceNote, Note baseNote, double graceNoteDuration){
    	Pattern gracePattern = new Pattern(graceNote.getMusicString());
    	return addOrnament(gracePattern, baseNote, graceNoteDuration);
    }
    
    /**
     * Returns a Pattern that adds an ornamental pattern to a given note.  
     * 
     * @param ornament Ornamental pattern.
     * @param baseNote Note value and duration.
     * @return Pattern for the calculated effect.
     */
    public static Pattern addOrnament(Pattern ornament, Note baseNote){
    	return addOrnament(ornament, baseNote, THIRTY_SECOND);
    }
    
    
    /**
     * Returns a Pattern that adds an ornamental pattern to a given note.  
     * 
     * @param ornament Ornamental pattern.
     * @param baseNote Note value and duration.
     * @param longestOrnamentDuration Duration for the sound of the longest note token in the ornament pattern.
     *                                All other durations in the pattern will be adjusted relative to this 
     *                                duration.
     * @return Pattern for the calculated effect.
     */
    public static Pattern addOrnament(Pattern ornament, Note baseNote, double longestOrnamentDuration){
    	PatternInvestigator investigator = new PatternInvestigator();
    	MusicStringParser stringParser = new MusicStringParser();
    	
    	stringParser.addParserListener(investigator);
    	stringParser.parse(ornament);
    	
    	double longestDuration = investigator.getLongestDecimalDuration();
    	double durationChange =  longestOrnamentDuration / longestDuration;
    	
    	DurationPatternTransformer transformer = new DurationPatternTransformer(durationChange);
    	
    	Pattern result = transformer.transform(ornament);
    	
    	stringParser.removeParserListener(investigator);
    	investigator = new PatternInvestigator();
    	stringParser.addParserListener(investigator);
    	stringParser.parse(result);
    	
    	if(investigator.getTotalDecimalDuration() < baseNote.getDecimalDuration()){
    		double remainingDuration = baseNote.getDecimalDuration() - investigator.getTotalDecimalDuration();
    		baseNote.setDecimalDuration(remainingDuration);
    		result.add(baseNote.getMusicString());
    		return result;
    	} else {
    		return new Pattern(baseNote.getMusicString());
    	}
    }
    
    /**
     * Returns a Pattern that plays a trill given a note, trill direction, and duration of each
     * individual sound.
     * 
     * Example:
     * trill(Note.createNote("C"), MusicalEffects.EFFECT_DIRECTION_UP, 0.03125d);
     * will produce this Pattern: 
     * [60]/0.03125 [61]/0.03125 [60]/0.03125 [61]/0.03125 [60]/0.03125 [61]/0.03125 [60]/0.03125 [60]/0.03125
     *
     * @param baseNote Note value and duration for the trill.
     * @param trillDirection Describes if the patter should trill up or down.
     * @param singleSoundDuration duration of each individual note of the trill.
     * @return Pattern for the calculated trill.
     */
    public static Pattern trill(Note baseNote, char trillDirection, double singleSoundDuration){
        
    	StringBuilder musicStringBuilder = new StringBuilder();
        
    	double totalDuration = baseNote.getDecimalDuration();
        double actualDuration = 0.0d;
        
        byte secondNote = baseNote.getValue();
        
        if(trillDirection == EFFECT_DIRECTION_UP){
        	secondNote++;
        } else if(trillDirection == EFFECT_DIRECTION_DOWN){
        	secondNote--;
        }
        
        double remainingDuration = totalDuration - (2*singleSoundDuration);
        if(remainingDuration > 0.0d){
        	
        	appendNoteValueAndDuration(musicStringBuilder, baseNote.getValue(), singleSoundDuration);
        	actualDuration+=singleSoundDuration;
        	
        	while(actualDuration < totalDuration){
        		if(actualDuration + (2*singleSoundDuration) < totalDuration){
        			appendNoteValueAndDuration(musicStringBuilder, secondNote, singleSoundDuration);
        			actualDuration += singleSoundDuration;
        			appendNoteValueAndDuration(musicStringBuilder, baseNote.getValue(), singleSoundDuration);
        			actualDuration += singleSoundDuration;
        		} else if(actualDuration + singleSoundDuration > totalDuration){
        			double gapDuration = totalDuration - actualDuration;
        			appendNoteValueAndDuration(musicStringBuilder, baseNote.getValue(), gapDuration);
        			actualDuration+=gapDuration;
        		} else {
        			appendNoteValueAndDuration(musicStringBuilder, baseNote.getValue(), singleSoundDuration);
        			actualDuration+=singleSoundDuration;
        		}
        	}
        	return new Pattern(musicStringBuilder.toString().trim());
        } else {
        	return new Pattern(baseNote.getMusicString());
        }
    }
    
    /**
     * Returns a Pattern that plays a trill given a note, and trill direction.
     * 
     * Example:
     * trill(Note.createNote("C6h"), MusicalEffects.EFFECT_DIRECTION_UP)
     * will produce this Pattern: 
     * [72]/0.03125 [73]/0.03125 [72]/0.03125 [73]/0.03125 [72]/0.03125 [73]/0.03125 [72]/0.03125 [73]/0.03125 [72]/0.03125 [73]/0.03125 [72]/0.03125 [73]/0.03125 [72]/0.03125 [73]/0.03125 [72]/0.03125 [72]/0.03125
     *
     * @param baseNote Note value and duration for the trill.
     * @param trillDirection Describes if the patter should trill up or down.
     * @return Pattern for the calculated trill.
     */
    public static Pattern trill(Note baseNote, char trillDirection){
    	return trill(baseNote, trillDirection, THIRTY_SECOND);
    }
    
    /**
     * Returns a Pattern that plays a trill given a note, and trill direction.
     * 
     * Example:
     * trill(Note.createNote("C6h"))
     * will produce this Pattern: 
     * [72]/0.03125 [73]/0.03125 [72]/0.03125 [73]/0.03125 [72]/0.03125 [73]/0.03125 [72]/0.03125 [73]/0.03125 [72]/0.03125 [73]/0.03125 [72]/0.03125 [73]/0.03125 [72]/0.03125 [73]/0.03125 [72]/0.03125 [72]/0.03125
     *
     * @param baseNote Note value and duration for the trill.
     * @return Pattern for the calculated trill.
     */
    public static Pattern trill(Note baseNote){
    	return trill(baseNote, EFFECT_DIRECTION_UP);
    }
    
    /**
     * Takes care of redundant music string building for adding notes/durations.
     * 
     * @param inputBuilder The StringBuilder to modify.
     * @param noteValue The note value in numeric form.
     * @param noteDuration The note duration in decimal form.
     * @return StringBuilder with the appropriate strings appended
     */
    private static StringBuilder appendNoteValueAndDuration(StringBuilder inputBuilder, byte noteValue, double noteDuration){
    	inputBuilder.append("[");
    	inputBuilder.append(noteValue);
    	inputBuilder.append("]/");
    	inputBuilder.append(noteDuration);
    	inputBuilder.append(" ");
    	return inputBuilder;
    }
}
