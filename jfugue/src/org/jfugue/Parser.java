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

import javax.swing.event.EventListenerList;

import org.jfugue.parsers.FireParserEvent;


/**
 * You may notice that there is no parse() method in the Parser class!
 * That's because the parse() method may take any type of parameter, as
 * well as any number of parameters, so it isn't something that can 
 * declared ahead of time. 
 * 
 * @author David Koelle
 *
 */
public abstract class Parser 
{
    public Parser() 
    { 
        progressListenerList = new EventListenerList();
        listenerList = new EventListenerList ();
        
        // The Parser could add itself as a ParserProgressListener.
    }
    
    // Logging methods
    ///////////////////////////////////////////

    /** Pass this value to setTracing( ) to turn tracing off.  Tracing is off by default. */
    public static final int TRACING_OFF = 0;

    /** Pass this value to setTracing( ) to turn tracing on.  Tracing is off by default. */
    public static final int TRACING_ON = 1;

    private int tracing = TRACING_OFF;

    /**
     * Turns tracing on or off.  If you're having trouble with your music string,
     * or if you've added new tokens to the parser, turn tracing on to make sure
     * that your new tokens are parsed correctly.
     * @param tracing the state of tracing - on or off
     */
    public void setTracing(int tracing)
    {
        this.tracing = tracing;
    }

    /**
     * Returns the current state of tracing.
     * @return the state of tracing
     */
    public int getTracing()
    {
        return this.tracing;
    }

    /**
     * Displays the passed String.
     * @param s the String to display
     */
    protected void trace(Object... sentenceFragments)
    {
        if (TRACING_ON == getTracing())
        {
            StringBuilder buddy = new StringBuilder();
            for (int i=0; i < sentenceFragments.length; i++)
            {
                buddy.append(sentenceFragments[i]);
            }
            
            System.out.println(buddy.toString());
        }
    }

    //
    // ParserProgressListener methods
    /////////////////////////////////////////////////////////////////////////

    /** List of ParserProgressListeners */
    protected EventListenerList progressListenerList;

    /**
     * Adds a <code>ParserListener</code>.  The listener will receive events when the parser
     * interprets music string tokens.
     *
     * @param listener the listener that is to be notified of parser events
     */
    public void addParserProgressListener(ParserProgressListener l) 
    {
        progressListenerList.add(ParserProgressListener.class, l);
    }

    /**
     * Removes a <code>ParserListener</code>.
     *
     * @param listener the listener to remove
     */
    public void removeParserProgressListener(ParserProgressListener l) 
    {
        progressListenerList.remove(ParserProgressListener.class, l);
    }

    /**
     * Returns an array of all <code>ParserProgressListener</code>s.
     * 
     * @return array of all <code>ParserProgressListener</code>s
     */
    public ParserProgressListener[] getParserProgressListeners()
    {
        ParserProgressListener[] listeners =  progressListenerList.getListeners(ParserProgressListener.class);
        return listeners;
    }
    
    /**
     * Removes all of the <code>ParserProgressListener</code>s.
     */
    protected void clearParserProgressListeners() 
    {
        ParserProgressListener[] listeners = listenerList.getListeners(ParserProgressListener.class);
        for (ParserProgressListener listener : listeners) { 
             progressListenerList.remove(ParserProgressListener.class, listener);
        }
    }

    /** Tells all ParserProgressListener interfaces that progress has occurred. */
    protected void fireProgressReported(String description, long partCompleted, long whole)
    {
        ParserProgressListener[] listeners = getParserProgressListeners();
        for (ParserProgressListener listener : listeners) {
            listener.progressReported(description, partCompleted, whole);
        }
    }

    //
    // ParserListener methods
    /////////////////////////////////////////////////////////////////////////

    /** List of ParserListeners */
    protected EventListenerList listenerList;

    /**
     * Adds a <code>ParserListener</code>.  The listener will receive events when the parser
     * interprets music string tokens.
     *
     * @param listener the listener that is to be notified of parser events
     */
    public void addParserListener(ParserListener listener) 
    {
        listenerList.add(ParserListener.class, listener);
    }

    /**
     * Removes a <code>ParserListener</code>.
     *
     * @param listener the listener to remove
     */
    public void removeParserListener(ParserListener listener) 
    {
        listenerList.remove(ParserListener.class, listener);
    }

    /**
     * Returns an array of all <code>ParserListener</code>s.
     * 
     * @return array of all <code>ParserListener</code>s
     */
    public ParserListener[] getParserListeners()
    {
        ParserListener[] listeners = listenerList.getListeners(ParserListener.class);
        return listeners;
    }
    
    /**
     * Removes all of the <code>ParserListener</code>s.
     */
    protected void clearParserListeners() 
    {
        ParserListener[] listeners = listenerList.getListeners(ParserListener.class);
        for (ParserListener listener : listeners) { 
            listenerList.remove(ParserListener.class, listener);
        }
    }

    /** Tells all ParserListeners that a voice event has been parsed. */
    protected void fireVoiceEvent(Voice event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.voiceEvent(event);
        }
    }

    /** Tells all ParserListeners that a tempo event has been parsed. */
    protected void fireTempoEvent(Tempo event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.tempoEvent(event);
        }
    }

    /** Tells all ParserListeners that an instrument event has been parsed. */
    protected void fireInstrumentEvent(Instrument event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.instrumentEvent(event);
        }
    }

    /** Tells all ParserListeners that a layer event has been parsed. */
    protected void fireLayerEvent(Layer event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.layerEvent(event);
        }
    }
    
    /** Tells all ParserListeners that a time event has been parsed. */
    protected void fireTimeEvent(Time event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.timeEvent(event);
        }
    }
    
    /** Tells all ParserListeners that a time event has been parsed. */
    protected void fireSystemExclusiveEvent(SystemExclusiveEvent event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.systemExclusiveEvent(event);
        }
    }
    
    /** Tells all ParserListeners that a key signature event has been parsed. */
    protected void fireKeySignatureEvent(KeySignature event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.keySignatureEvent(event);
        }
    }
    
    /** Tells all ParserListeners that a measure event has been parsed. */
    protected void fireMeasureEvent(Measure event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.measureEvent(event);
        }
    }

    /** Tells all ParserListeners that a controller event has been parsed. */
    protected void fireControllerEvent(Controller event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.controllerEvent(event);
        }
    }

    /** Tells all ParserListeners that a controller event has been parsed. */
    protected void fireChannelPressureEvent(ChannelPressure event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.channelPressureEvent(event);
        }
    }

    /** Tells all ParserListeners that a controller event has been parsed. */
    protected void firePolyphonicPressureEvent(PolyphonicPressure event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.polyphonicPressureEvent(event);
        }
    }

    /** Tells all ParserListeners that a controller event has been parsed. */
    protected void firePitchBendEvent(PitchBend event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.pitchBendEvent(event);
        }
    }

    /** Tells all ParserListeners that a note event has been parsed. */
    protected void fireNoteEvent(Note event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.noteEvent(event);
        }
    }

    /** Tells all ParserListeners that a sequential note event has been parsed. */
    protected void fireSequentialNoteEvent(Note event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.sequentialNoteEvent(event);
        }
    }

    /** Tells all ParserListeners that a parallel note event has been parsed. */
    protected void fireParallelNoteEvent(Note event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.parallelNoteEvent(event);
        }
    }

    //
    // End ParserListener methods
    /////////////////////////////////////////////////////////////////////////

    protected final FireParserEvent eventProxy = this.new ParserEventProxy();
    protected class ParserEventProxy implements FireParserEvent {

		public void addParserListener(ParserListener listener) {
			Parser.this.addParserListener(listener);
		}

		public void removeParserListener(ParserListener listener) {
			Parser.this.removeParserListener(listener);
		}

		public ParserListener[] getParserListeners() {
			// TODO Auto-generated method stub
			return null;
		}

		public void clearParserListeners() {
			// TODO Auto-generated method stub
			
		}

		public void fireVoiceEvent(Voice event) {
			Parser.this.fireVoiceEvent(event);
		}

		public void fireTempoEvent(Tempo event) {
			Parser.this.fireTempoEvent(event);
		}

		public void fireInstrumentEvent(Instrument event) {
			Parser.this.fireInstrumentEvent(event);
		}

		public void fireLayerEvent(Layer event) {
			Parser.this.fireLayerEvent(event);
		}

		public void fireTimeEvent(Time event) {
			Parser.this.fireTimeEvent(event);
		}

		public void fireSystemExclusiveEvent(SystemExclusiveEvent event) {
			Parser.this.fireSystemExclusiveEvent(event);
		}

		public void fireKeySignatureEvent(KeySignature event) {
			Parser.this.fireKeySignatureEvent(event);
		}

		public void fireMeasureEvent(Measure event) {
			Parser.this.fireMeasureEvent(event);
		}

		public void fireControllerEvent(Controller event) {
			Parser.this.fireControllerEvent(event);
		}

		public void fireChannelPressureEvent(ChannelPressure event) {
			Parser.this.fireChannelPressureEvent(event);
		}

		public void firePolyphonicPressureEvent(PolyphonicPressure event) {
			Parser.this.firePolyphonicPressureEvent(event);
		}

		public void firePitchBendEvent(PitchBend event) {
			Parser.this.firePitchBendEvent(event);
		}

		public void fireNoteEvent(Note event) {
			Parser.this.fireNoteEvent(event);
		}

		public void fireSequentialNoteEvent(Note event) {
			Parser.this.fireSequentialNoteEvent(event);
		}

		public void fireParallelNoteEvent(Note event) {
			Parser.this.fireParallelNoteEvent(event);
		}
    	
    }
}
