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

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

/**
 * Prepares a pattern to be turned into music by the Renderer.  This class
 * also handles saving the sequence derived from a pattern as a MIDI file.
 *
 *@see MidiRenderer
 *@see Pattern
 *@author David Koelle
 *@version 4.0.4
 */
public class AlwaysOpenPlayer extends Player
{
    /**
     * Instantiates a new Player object, which is used for playing music.
     */
    public AlwaysOpenPlayer()
    {
        super();
    }

    /**
     * Instantiates a new Player object, which is used for playing music.
     * The <code>connected</code> parameter is passed directly to MidiSystem.getSequencer.
     * Pass false when you do not want to copy a live synthesizer - for example,
     * if your Player is on a server, and you don't want to create new synthesizers every time
     * the constructor is called. 
     */
    public AlwaysOpenPlayer(boolean connected)
    {
        super(connected);
    }

    /**
     * Creates a new Player instance using a Sequencer that you have provided.
     * @param sequencer The Sequencer to send the MIDI events
     */
    public AlwaysOpenPlayer(Sequencer sequencer)
    {
        super(sequencer);
    }

    /**
     * Creates a new Player instance using a Sequencer obtained from the Synthesizer that you have provided.
     * @param synth The Synthesizer you want to use for this Player.
     */
    public AlwaysOpenPlayer(Synthesizer synth) throws MidiUnavailableException
    {
        super(synth);
    }

    @SuppressWarnings("unused")
	private void initSequencer()
    {
        // Close the sequencer and synthesizer
        getSequencer().addMetaEventListener(new MetaEventListener() {
            public void meta(MetaMessage event)
            {
                if (event.getType() == 47)
                {
                    close();
                }
            }
        });
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
     * Plays a MIDI Sequence
     * @param sequence the Sequence to play
     * @throws JFugueException if there is a problem playing the music
     * @see MidiRenderer
     */
    @Override
    public void play(Sequence sequence)
    {
        // Open the sequencer
        openSequencer();

        // Set the sequence
        try {
            getSequencer().setSequence(sequence);
        } catch (Exception e)
        {
            throw new JFugueException(JFugueException.ERROR_PLAYING_MUSIC + e.getMessage());
        }

        // Start the sequence
        getSequencer().start();

        // Wait for the sequence to finish
        while (isPlaying() || isPaused())
        {
            try {
                Thread.sleep(20);  // don't hog all of the CPU
            } catch (InterruptedException e)
            {
                throw new JFugueException(JFugueException.ERROR_SLEEP);
            }
        }

        // Close the sequencer
//        getSequencer().close();
    }


    /**
     * Closes MIDI resources - be sure to call this after play() has returned.
     */
    public void close()
    {
        getSequencer().close();
        try {
            if (MidiSystem.getSynthesizer() != null) {
                MidiSystem.getSynthesizer().close();
            }
        } catch (MidiUnavailableException e) {
            throw new JFugueException(JFugueException.GENERAL_ERROR + e.getMessage());
        }
    }
}