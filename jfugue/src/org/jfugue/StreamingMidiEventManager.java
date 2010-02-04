/*
 * JFugue - API for Music Programming
 * Copyright (C) 2003-2007  David Koelle
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

/**
 * Assists the StreamingMidiRenderer in converting Patterns to MIDI.
 *
 *@see StreamingPlayer
 *@author David Koelle
 *@version 3.2 
 */
public class StreamingMidiEventManager
{
    private final int CHANNELS = 16;
    private final int LAYERS = 16;
    private byte currentTrack = 0;
    private byte[] currentLayer = new byte[CHANNELS];
    private long time[][] = new long[CHANNELS][LAYERS];
    private MidiChannel channels[] = new MidiChannel[CHANNELS];
    private Map<Long, List<TimerEvent>> timerMap;
    private long currentTime;
    private long lastCurrentTime;
    private long initialTime;
    private boolean isActive;

    public StreamingMidiEventManager()
    {
        timerMap = new HashMap<Long, List<TimerEvent>>();
        isActive = true;
        
        try {
            Synthesizer synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            channels = synthesizer.getChannels();
        } catch (MidiUnavailableException e)
        {
            throw new JFugueException(JFugueException.ERROR_PLAYING_MUSIC);
        }

        for (int channel = 0; channel < CHANNELS; channel++) {
            for (int layer = 0; layer < LAYERS; layer++) {
                time[channel][layer] = 0;
            }
            currentLayer[channel] = 0;
        }
        currentTrack = 0;

        initialTime = System.currentTimeMillis();
        currentTime = System.currentTimeMillis();

        Thread timerThread = new Thread(
                new Runnable() {
                    public void run() {
                        while (isActive) {
                            if (advanceTimers())
                            {
                                synchronized(timerMap)
                                {
                                    executeScheduledEvents();
                                }
                            }
                            pauseForTheCause();
                        }
                    }
                }
            );
            timerThread.start();
    
    }
    
    /**
     * Advances the play timer and insertion timer.
     * Called from the timerThread.
     * Returns false if time was not advanced
     */
    private boolean advanceTimers()
    {
        lastCurrentTime = currentTime;
        currentTime = System.currentTimeMillis();

        if (lastCurrentTime != currentTime) {
            // Make sure the insertion time for all channels and layers 
            // keeps up with the current time.
        	System.out.println("Time advancing by "+(currentTime-lastCurrentTime));
            for (int channel = 0; channel < CHANNELS; channel++) {
                for (int layer = 0; layer < LAYERS; layer++) {
                    time[channel][layer] = time[channel][layer] + (currentTime - lastCurrentTime);
                }
            }
            
            return true;
        }
        return false;
    }
    
    /**
     * See if any scheduled events need to be executed.
     * Called from the timerThread.
     */
    private void executeScheduledEvents()
    {
        // Get any TimerEvents that may have happened in the intervening time, and execute them
        for (long time = lastCurrentTime - initialTime; time < currentTime - initialTime; time++) 
        {
            List<TimerEvent> timerEvents = timerMap.get(time); 
            if (null != timerEvents) {
                for (TimerEvent event : timerEvents)
                {
                    System.out.println(time+": Executing "+event);
                    event.execute();
                }
            }
            
            // Dispose of the lists that we no longer need
            timerEvents = null;
            timerMap.put(time, null);
        }
    }
    
    /**
     * Make sure this thread doesn't hog the CPU.
     * Called from the timerThread.
     */
    private void pauseForTheCause()
    {
        try {
            Thread.sleep(20); // Don't hog the CPU
        } catch (InterruptedException e)
        {
            throw new JFugueException(JFugueException.ERROR_SLEEP);
        }
    }
    
    public void close()
    {
        isActive = false;
    }
    
    /**
     * Sets the current track, or channel, to which new events will be added.
     * @param track the track to select
     */
    public void setCurrentTrack(byte track)
    {
        currentTrack = track;
    }

    /**
     * Sets the current layer within the track to which new events will be added.
     * @param track the track to select
     */
    public void setCurrentLayer(byte layer)
    {
        currentLayer[currentTrack] = layer;
    }

    /**
     * Advances the timer for the current track by the specified duration,
     * which is specified in Pulses Per Quarter (PPQ)
     * @param duration the duration to increase the track timer
     */
    public void advanceTrackTimer(long duration)
    {
        time[currentTrack][currentLayer[currentTrack]] += duration;
    }

    /**
     * Sets the timer for the current track by the given time,
     * which is specified in Pulses Per Quarter (PPQ)
     * @param newTime the time at which to set the track timer
     */
    public void setTrackTimer(long newTime)
    {
        time[currentTrack][currentLayer[currentTrack]] = newTime;
    }

    /**
     * Returns the timer for the current track.
     * @return the timer value for the current track, specified in Pulses Per Quarter (PPQ)
     */
    public long getTrackTimer()
    {
        return time[currentTrack][currentLayer[currentTrack]];
    }

    /**
     * Adds a MetaMessage to the current track.  
     *
     * @param command the MIDI command represented by this message
     * @param data1 the first data byte
     * @param data2 the second data byte
     */
    public void addMetaMessage(int type, byte[] bytes)
    {
        // NOP
    }

    /**
     * Adds a MIDI event to the current track.  
     *
     * @param command the MIDI command represented by this message
     * @param data1 the first data byte
     */
    public void addEvent(int command, int data1)
    {
        scheduleEvent(time[currentTrack][currentLayer[currentTrack]], command, data1, 0);
    }

    /**
     * Adds a MIDI event to the current track.  
     *
     * @param command the MIDI command represented by this message
     * @param data1 the first data byte
     * @param data2 the second data byte
     */
    public void addEvent(int command, int data1, int data2)
    {
        scheduleEvent(time[currentTrack][currentLayer[currentTrack]], command, data1, data2);
    }
    
    /**
     * Adds a MIDI event to the current track.  
     *
     * @param command the MIDI command represented by this message
     * @param data1 the first data byte
     * @param data2 the second data byte
     */
    public void scheduleEvent(long when, int command, int data1, int data2)
    {
        switch (command) {
          case ShortMessage.PROGRAM_CHANGE:
            scheduleTimerEvent(when, new ProgramChangeTimerEvent(currentTrack, data1));
            break;
          case ShortMessage.CONTROL_CHANGE:
            scheduleTimerEvent(when, new ControlChangeTimerEvent(currentTrack, data1, data2));
            break;
          case ShortMessage.CHANNEL_PRESSURE:
            scheduleTimerEvent(when, new ChannelPressureTimerEvent(currentTrack, data1));
            break;
          case ShortMessage.POLY_PRESSURE:
            scheduleTimerEvent(when, new PolyPressureTimerEvent(currentTrack, data1, data2));
            break;
          case ShortMessage.PITCH_BEND:
            scheduleTimerEvent(when, new PitchBendTimerEvent(currentTrack, data1));
            break;
          default: break;
        }
    }
    
    /**
     * Adds a ShortMessage.NOTE_ON event to the current track, using attack and
     * decay velocity values.  Also adds a ShortMessage.NOTE_OFF command for
     * the note, using the duration parameter to space the NOTE_OFF command properly.
     * 
     * Both the NOTE_ON and NOTE_OFF events can be suppressed.  This is useful
     * when notes are tied to other notes.
     *
     * @param data1 the first data byte, which contains the note value
     * @param data2 the second data byte for the NOTE_ON event, which contains the attack velocity
     * @param data3 the second data byte for the NOTE_OFF event, which contains the decay velocity
     * @param duration the duration of the note
     * @param addNoteOn whether a ShortMessage.NOTE_ON event should be created for for this event.  For the end of a tied note, this should be false; otherwise it should be true.
     * @param addNoteOff whether a ShortMessage.NOTE_OFF event should be created for for this event.  For the start of a tied note, this should be false; otherwise it should be true.
     */
    public void addNoteEvents(final byte noteValue, final byte attackVelocity, final byte decayVelocity, final long duration, boolean addNoteOn, boolean addNoteOff)
    {
        if (addNoteOn) {
            scheduleTimerEvent(time[currentTrack][currentLayer[currentTrack]], new NoteOnTimerEvent(currentTrack, noteValue, attackVelocity));
        }
        if (addNoteOff) {
            scheduleTimerEvent(time[currentTrack][currentLayer[currentTrack]] + duration /*  *TimeFactor.QUARTER_DURATIONS_IN_WHOLE */, new NoteOffTimerEvent(currentTrack, noteValue, decayVelocity));
        }
    }
    
    public void scheduleTimerEvent(long when, TimerEvent event)
    {
        long updatedWhen = when; // + (currentTime-initialTime);
        if (updatedWhen < (currentTime-initialTime)) {
        	System.out.println("when was "+updatedWhen+", updating to "+(currentTime-initialTime));
        	updatedWhen = currentTime-initialTime;
        }
        System.out.println("At time "+currentTime+" ("+(currentTime-initialTime)+" relative), adding "+event+" to time "+updatedWhen);
        
        synchronized(timerMap)
        {
            List<TimerEvent> timerEventsAtTimeX = timerMap.get(updatedWhen);  
            if (null == timerEventsAtTimeX)
            {
                timerEventsAtTimeX = new ArrayList<TimerEvent>();
            }
            timerEventsAtTimeX.add(event);
            timerMap.put(updatedWhen, timerEventsAtTimeX);
        }
    }

    interface TimerEvent
    {
        public void execute();
    }
    
    class NoteOnTimerEvent implements TimerEvent
    {
        public byte track;
        public byte noteValue;
        public byte attackVelocity;
        
        public NoteOnTimerEvent(byte track, byte noteValue, byte attackVelocity)
        {
            this.track = track;
            this.noteValue = noteValue;
            this.attackVelocity = attackVelocity;
        }
        
        public void execute()
        {
            channels[track].noteOn(noteValue, attackVelocity);
        }
    }

    class NoteOffTimerEvent implements TimerEvent
    {
        public byte track;
        public byte noteValue;
        public byte decayVelocity;
        
        public NoteOffTimerEvent(byte track, byte noteValue, byte decayVelocity)
        {
            this.track = track;
            this.noteValue = noteValue;
            this.decayVelocity = decayVelocity;
        }
        
        public void execute()
        {
            channels[track].noteOff(noteValue, decayVelocity);
        }
    }

    class ProgramChangeTimerEvent implements TimerEvent 
    {
        public byte track;
        public int data1;
        
        public ProgramChangeTimerEvent(byte track, int data1) 
        {
            this.track = track;
            this.data1 = data1;
        }
        
        public void execute()
        {
            channels[track].programChange(data1);
        }
    }

    class ControlChangeTimerEvent implements TimerEvent 
    {
        public byte track;
        public int data1;
        public int data2;
        
        public ControlChangeTimerEvent(byte track, int data1, int data2) 
        {
            this.track = track;
            this.data1 = data1;
            this.data2 = data2;
        }
        
        public void execute()
        {
            channels[track].controlChange(data1, data2);
        }
    }

    class ChannelPressureTimerEvent implements TimerEvent 
    {
        public byte track;
        public int data1;
        
        public ChannelPressureTimerEvent(byte track, int data1) 
        {
            this.track = track;
            this.data1 = data1;
        }
        
        public void execute()
        {
            channels[track].setChannelPressure(data1); 
        }
    }

    class PolyPressureTimerEvent implements TimerEvent 
    {
        public byte track;
        public int data1;
        public int data2;
        
        public PolyPressureTimerEvent(byte track, int data1, int data2) 
        {
            this.track = track;
            this.data1 = data1;
            this.data2 = data2;
        }
        
        public void execute()
        {
            channels[track].setPolyPressure(data1, data2); 
        }
    }

    class PitchBendTimerEvent implements TimerEvent 
    {
        public byte track;
        public int data1;
        
        public PitchBendTimerEvent(byte track, int data1) 
        {
            this.track = track;
            this.data1 = data1;
        }
        
        public void execute()
        {
            channels[track].setPitchBend(data1); 
        }
    }

}

