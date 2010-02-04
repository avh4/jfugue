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

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

/**
 * Provides a player that can be given new MusicStrings at runtime.  The
 * original Player class requires that a MusicString be fully formed
 * before sending to Player.play().  This class lets you add new bits
 * of a MusicString with the add() method.  The newly-added patterns
 * are played immediately. 
 *
 *@see Pattern
 *@see Player
 *@see StreamingMidiRenderer
 *@see StreamingMidiEventManager
 *@author David Koelle
 *@version 3.2
 */
public final class StreamingPlayer 
{
    private Sequencer sequencer;
    private StreamingMidiRenderer renderer;
    private MusicStringParser parser;
    
    /**
     * Instantiates a new StreamingPlayer object, which is used for playing music in realtime.
     */
    public StreamingPlayer()
    {
        this(true);
    }
    
    /**
     * Instantiates a new StreamingPlayer object, which is used for playing music in realtime.
     * The <code>connected</code> parameter is passed directly to MidiSystem.getSequencer.
     * Pass false when you do not want to copy a live synthesizer - for example,
     * if your Player is on a server, and you don't want to create new synthesizers every time
     * the constructor is called. 
     */
    public StreamingPlayer(boolean connected)
    {
        try {
            init(MidiSystem.getSequencer());
        } 
        catch (MidiUnavailableException e)
        {
            throw new JFugueException(JFugueException.SEQUENCER_DEVICE_NOT_SUPPORTED_WITH_EXCEPTION + e.getMessage());
        }
    }
    
    /**
     * Creates a new StreamingPlayer instance using a Sequencer that you have provided.  
     * @param sequencer The Sequencer to send the MIDI events 
     */
    public StreamingPlayer(Sequencer sequencer)
    {
        init(sequencer);
        openSequencer();
    }

    /**
     * Creates a new StreamingPlayer instance using a Sequencer obtained from the Synthesizer that you have provided.  
     * @param synth The Synthesizer you want to use for this Player. 
     */
    public StreamingPlayer(Synthesizer synth) throws MidiUnavailableException
    {
        this(Player.getSequencerConnectedToSynthesizer(synth));
    }
    
    private void init(Sequencer sequencer)
    {
        setSequencer(sequencer);
        
        parser = new MusicStringParser();
        renderer = new StreamingMidiRenderer();
        parser.addParserListener(renderer);
    }

    private void openSequencer()
    {
        if (getSequencer() == null)
        {
            throw new JFugueException(JFugueException.SEQUENCER_DEVICE_NOT_SUPPORTED);
        }

        // Open the sequencer, if it is not already open
        if (!getSequencer().isOpen()) {
            try {
                getSequencer().open();
            } catch (MidiUnavailableException e)
            {
                throw new JFugueException(JFugueException.SEQUENCER_DEVICE_NOT_SUPPORTED_WITH_EXCEPTION + e.getMessage());
            }
        }
    }

    /**
     * Closes MIDI resources - be sure to call this after play() has returned.
     */
    public void close()
    {
        parser.removeParserListener(renderer);
        
        getSequencer().close();
        try {
            if (MidiSystem.getSynthesizer() != null) {
                MidiSystem.getSynthesizer().close();
            }
        } catch (MidiUnavailableException e) {
            throw new JFugueException(JFugueException.GENERAL_ERROR + e.getMessage());
        }
        renderer.close();
    }
    
    private void setSequencer(Sequencer sequencer)
    {
        this.sequencer = sequencer;
    }

    /**
     * Returns the sequencer containing the MIDI data from a pattern that has been parsed.
     * @return the Sequencer from the pattern that was recently parsed
     */
    public Sequencer getSequencer()
    {
        return this.sequencer;
    }

    /**
     * Returns the StreamingMidiRenderer that this Player will use to play MIDI events.  
     * @return the StreamingMidiRenderer that this Player will use to play MIDI events
     * @version 4.1
     */
    public ParserListener getRenderer()
    {
        return this.renderer;
    }

    /**
     * Streams a Pattern containing a single token.  Does not wait for the 
     * music to stop playing - the user will need to throttle calls to stream().
     * 
     * @param singleToken
     */
    public void stream(Pattern singleToken)
    {
        parser.parse(singleToken);
    }

    /**
     * Streams a MusicString containing a single token.  Does not wait for the 
     * music to stop playing - the user will need to throttle calls to stream().
     * 
     * @param singleToken
     */
    public void stream(String singleToken)
    {
        stream(new Pattern(singleToken));
    }
    

//    /**
//     * Streams a Pattern containing one or more tokens, plays the whole pattern,
//     * then waits for all notes in the pattern to finish playing before returning.
//     * @param pattern
//     */
//    public void streamUntilFinished(Pattern pattern)
//    {
//        stream(pattern);
//        // TODO: Ask the renderer itself when it's finished
//    }
}


