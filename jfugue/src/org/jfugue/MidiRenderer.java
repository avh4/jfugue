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

import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;

import org.jfugue.elements.ChannelPressure;
import org.jfugue.elements.Controller;
import org.jfugue.elements.Instrument;
import org.jfugue.elements.KeySignature;
import org.jfugue.elements.Layer;
import org.jfugue.elements.Measure;
import org.jfugue.elements.Note;
import org.jfugue.elements.PitchBend;
import org.jfugue.elements.PolyphonicPressure;
import org.jfugue.elements.SystemExclusive;
import org.jfugue.elements.Tempo;
import org.jfugue.elements.Time;
import org.jfugue.elements.Voice;


/**
 * This class takes a Pattern, and turns it into wonderful music.
 *
 * <p>
 * Playing music is only one thing that can be done by rendering a pattern.
 * You could also create your own renderer that draws sheet music based on 
 * a pattern. Or, you could create a graphical light show based on the musical
 * notes in the pattern.
 * </p>
 * 
 * <p>
 * This was named Renderer in previous versions of JFugue.  The name has been
 * changed to differentiate it from other types of renderers.
 * </p>
 *
 *@author David Koelle
 *@version 2.0
 *@version 3.0 - Renderer renamed to MidiRenderer
 */
public final class MidiRenderer extends ParserListenerAdapter
{
    private MidiEventManager eventManager;
    long initialNoteTime = 0;
    private float sequenceTiming;
    private int resolution;

    /**
     * Instantiates a Renderer
     */
    public MidiRenderer(float sequenceTiming, int resolution)
    {
        reset(sequenceTiming, resolution);
    }

    /**
     * Creates a new MidiEventManager.  If this isn't called,
     * events from multiple calls to render() will be added
     * to the same eventManager, which means that the second
     * time render() is called, it will contain music left over
     * from the first time it was called.  (This wasn't a problem
     * with Java 1.4)
     * @since 3.0
     */
    public void reset(float sequenceTiming, int resolution)
    {
        this.sequenceTiming = sequenceTiming;
        this.resolution = resolution;
        this.eventManager = new MidiEventManager(sequenceTiming, resolution);
    }
    
    /**
     * Creates a new MidiEventManager using the sequenceTiming and
     * resolution already used to create this MidiRenderer.  If this 
     * isn't called, events from multiple calls to render() will be 
     * added to the same eventManager, which means that the second
     * time render() is called, it will contain music left over
     * from the first time it was called.  (This wasn't a problem
     * with Java 1.4)
     * @since 3.2
     */
    public void reset()
    {
        this.eventManager = new MidiEventManager(this.sequenceTiming, this.resolution);
    }
    
    /**
     * Returns the last sequence generated by this renderer
     */
    public Sequence getSequence()
    {
        return this.eventManager.getSequence();
    }

    // ParserListener methods
    ////////////////////////////

    public void voiceEvent(Voice voice)
    {
        this.eventManager.setCurrentTrack(voice.getVoice());
    }

    public void tempoEvent(Tempo tempo)
    {
        byte[] threeTempoBytes = TimeFactor.convertBPMToBytes(tempo.getTempo());
        this.eventManager.addMetaMessage(0x51, threeTempoBytes);
    }

    public void instrumentEvent(Instrument instrument)
    {
        this.eventManager.addEvent(ShortMessage.PROGRAM_CHANGE, instrument.getInstrument(), 0);
    }

    public void layerEvent(Layer layer)
    {
        this.eventManager.setCurrentLayer(layer.getLayer());
    }
    
    public void timeEvent(Time time)
    {
        this.eventManager.setTrackTimer(time.getTime());
    }
    
    public void measureEvent(Measure measure)
    {
        // No MIDI is generated when a measure indicator is identified.
    }
    
    public void keySignatureEvent(KeySignature keySig)
    {
        this.eventManager.addMetaMessage(0x59, new byte[] { keySig.getKeySig(), keySig.getScale() });
    }

    public void systemExclusiveEvent(SystemExclusive systemExclusiveEvent)
    {
    	this.eventManager.addSystemExclusiveEvent(systemExclusiveEvent.getBytes());
    }
    
    public void controllerEvent(Controller controller)
    {
        this.eventManager.addEvent(ShortMessage.CONTROL_CHANGE, controller.getIndex(), controller.getValue());
    }

    public void channelPressureEvent(ChannelPressure channelPressure)
    {
        this.eventManager.addEvent(ShortMessage.CHANNEL_PRESSURE, channelPressure.getPressure());
    }

    public void polyphonicPressureEvent(PolyphonicPressure polyphonicPressure)
    {
        this.eventManager.addEvent(ShortMessage.POLY_PRESSURE, polyphonicPressure.getKey(), polyphonicPressure.getPressure());
    }

    public void pitchBendEvent(PitchBend pitchBend)
    {
        this.eventManager.addEvent(ShortMessage.PITCH_BEND, pitchBend.getBend()[0], pitchBend.getBend()[1]);
    }

    public void noteEvent(Note note)
    {
        // Remember the current track time, so we can flip back to it
        // if there are other notes to play in parallel
        this.initialNoteTime = this.eventManager.getTrackTimer();
        long duration = note.getMillisDuration();
        
        // If there is no duration, don't add this note to the event manager
        // TODO: This is a special case as of v4.0.3 that should be re-thought if a new noteEvent callback is created in v5.0
        if (duration == 0) {
            return;
        }

        // Add messages to the track
        if (note.isRest()) {
            this.eventManager.addRest(duration);
        } else {
            initialNoteTime = eventManager.getTrackTimer();
            byte attackVelocity = note.getAttackVelocity();
            byte decayVelocity = note.getDecayVelocity();
            this.eventManager.addNoteEvent(note.getValue(), attackVelocity, decayVelocity, duration, !note.isEndOfTie(), !note.isStartOfTie());
        }
    }

    public void sequentialNoteEvent(Note note)
    {
        long duration = note.getMillisDuration();
        if (note.isRest()) {
            this.eventManager.addRest(duration);
        } else {
            byte attackVelocity = note.getAttackVelocity();
            byte decayVelocity = note.getDecayVelocity();
            this.eventManager.addNoteEvent(note.getValue(), attackVelocity, decayVelocity, duration, !note.isEndOfTie(), !note.isStartOfTie());
        }
    }

    public void parallelNoteEvent(Note note)
    {
        long duration = note.getMillisDuration();
        this.eventManager.setTrackTimer(this.initialNoteTime);
        if (note.isRest()) {
            this.eventManager.addRest(duration);
        } else {
            byte attackVelocity = note.getAttackVelocity();
            byte decayVelocity = note.getDecayVelocity();
            this.eventManager.addNoteEvent(note.getValue(), attackVelocity, decayVelocity, duration, !note.isEndOfTie(), !note.isStartOfTie());
        }
    }
}
