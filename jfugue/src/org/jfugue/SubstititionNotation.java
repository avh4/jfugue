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

import java.util.Map;


/**
 * In SubstitutionNotation, a MusicString contains variables for notes but otherwise each token
 * is complete with duration and other information (such as attack/decay). A set of substitutions
 * is passed in, which then render the MusicString.
 *
 * TODO: This and IntervalNotation share similar capabilities. Determine if/how they should be combined.
 *
 * @author David Koelle
 * @version 5.0
 */
public class SubstititionNotation
{
    private String musicStringWithSubstitutionMarkers;

    public SubstititionNotation(String musicStringWithSubstitutionMarkers)
    {
        setMusicStringWithSubstitutionMarkers(musicStringWithSubstitutionMarkers);
    }

    public void setMusicStringWithSubstitutionMarkers(String musicStringWithSubstitutionMarkers)
    {
        this.musicStringWithSubstitutionMarkers = musicStringWithSubstitutionMarkers;
    }

    public String getMusicStringWithSubstitutionMarkers()
    {
        return this.musicStringWithSubstitutionMarkers;
    }

    public PatternInterface getPatternForSubstitutions(Map<String, String> substitutions)
    {
        StringBuilder buddy = new StringBuilder();
        String[] tokens = getMusicStringWithSubstitutionMarkers().split(" ");

        // Go through the Pattern, and replace intervals specified within < and > with the root note plus the interval value, minus 1
        for (int i=0; i < tokens.length; i++)
        {
            int lastAngleBracketPosition = -1;
            boolean leftAngleBracketExists = (tokens[i].indexOf('<') != -1);

            if (leftAngleBracketExists) {
                while (leftAngleBracketExists)
                {
                    int start = tokens[i].indexOf('<', lastAngleBracketPosition);
                    int end = tokens[i].indexOf('>', start);
                    String markerKey = tokens[i].substring(start+1, end);
                    String markerValue = substitutions.get(markerKey);
                    buddy.append(markerValue);

                    lastAngleBracketPosition = end;
                    int nextLeftAngleBracketPosition = tokens[i].indexOf('<', lastAngleBracketPosition-1);
                    if (nextLeftAngleBracketPosition == -1) {
                        buddy.append(tokens[i].substring(end+1, tokens[i].length())); // Add the rest of the token
                        leftAngleBracketExists = false;
                    } else {
                        buddy.append(tokens[i].substring(end+1, nextLeftAngleBracketPosition)); // Add the rest of the token up to the next angle
                        leftAngleBracketExists = true;
                    }
                }
            } else {
                buddy.append(tokens[i]);
            }
            buddy.append(" ");
        }

        return new Pattern(buddy.toString());
    }
}
