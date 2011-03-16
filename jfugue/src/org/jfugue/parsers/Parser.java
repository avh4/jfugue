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

package org.jfugue.parsers;

import java.util.HashMap;

import javax.swing.event.EventListenerList;

import org.jfugue.Environment;
import org.jfugue.Environment.Error;
import org.jfugue.JFugueDefinitions;
import org.jfugue.JFugueException;
import org.jfugue.ParserListener;
import org.jfugue.ParserProgressListener;
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
        environment = new org.jfugue.Environment(new HashMap<String, String>(JFugueDefinitions.DICT_MAP),
        		eventProxy);
        // The Parser could add itself as a ParserProgressListener.
    }
 
    protected Environment environment;
    
    
    
    // Logging methods
    ///////////////////////////////////////////

    /******* Delegate to Environment *******/
    
    /**
	 * @param key
	 * @param val
	 * @see org.jfugue.Environment#add(java.lang.String, java.lang.String)
	 */
	public void addDict(String key, String val) {
		environment.addToDict(key, val);
	}

	/**
	 * @param bracketedString
	 * @return byte
	 * @throws Error
	 * @see org.jfugue.Environment#getByteFromDictionary(java.lang.String)
	 */
	public byte getByteFromDictionary(String bracketedString) throws Error {
		return environment.getByteFromDictionary(bracketedString);
	}

	/**
	 * @param bracketedString
	 * @return double
	 * @throws Error
	 * @see org.jfugue.Environment#getDoubleFromDictionary(java.lang.String)
	 */
	public double getDoubleFromDictionary(String bracketedString) throws Error {
		return environment.getDoubleFromDictionary(bracketedString);
	}

	/**
	 * @param bracketedString
	 * @return int
	 * @throws Error
	 * @see org.jfugue.Environment#getIntFromDictionary(java.lang.String)
	 */
	public int getIntFromDictionary(String bracketedString) throws Error {
		return environment.getIntFromDictionary(bracketedString);
	}

	/**
	 * @param bracketedString
	 * @return long
	 * @throws JFugueException
	 * @see org.jfugue.Environment#getLongFromDictionary(java.lang.String)
	 */
	public long getLongFromDictionary(String bracketedString)
			throws JFugueException {
		return environment.getLongFromDictionary(bracketedString);
	}

	/**
	 * @return the current KeySig
	 * @see org.jfugue.Environment#getKeySig()
	 */
	public KeySignature getKeySig() {
		return environment.getKeySig();
	}

	/******* End delegate to Environment *******/
	

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
    public void addParserProgressListener(ParserProgressListener listener) 
    {
        progressListenerList.add(ParserProgressListener.class, listener);
    }

    /**
     * Removes a <code>ParserListener</code>.
     *
     * @param listener the listener to remove
     */
    public void removeParserProgressListener(ParserProgressListener listener) 
    {
        progressListenerList.remove(ParserProgressListener.class, listener);
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
    protected void fireSystemExclusiveEvent(SystemExclusive event)
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

    protected final FireEventProxy eventProxy = this.new ParserEventProxy();
    protected class ParserEventProxy implements FireEventProxy {

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

		public void fireSystemExclusiveEvent(SystemExclusive event) {
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
