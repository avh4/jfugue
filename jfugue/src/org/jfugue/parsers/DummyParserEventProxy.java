package org.jfugue.parsers;

import javax.swing.event.EventListenerList;

import org.jfugue.ParserListener;
import org.jfugue.SystemExclusiveEvent;
import org.jfugue.elements.ChannelPressure;
import org.jfugue.elements.Controller;
import org.jfugue.elements.Instrument;
import org.jfugue.elements.KeySignature;
import org.jfugue.elements.Layer;
import org.jfugue.elements.Measure;
import org.jfugue.elements.Note;
import org.jfugue.elements.PitchBend;
import org.jfugue.elements.PolyphonicPressure;
import org.jfugue.elements.Tempo;
import org.jfugue.elements.Time;
import org.jfugue.elements.Voice;

public class DummyParserEventProxy implements org.jfugue.parsers.FireEventProxy {
    /** List of ParserListeners */
    protected EventListenerList listenerList = new EventListenerList();
    
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
    public void clearParserListeners() 
    {
        ParserListener[] listeners = listenerList.getListeners(ParserListener.class);
        for (ParserListener listener : listeners) { 
            listenerList.remove(ParserListener.class, listener);
        }
    }

    /** Tells all ParserListeners that a voice event has been parsed. */
    public void fireVoiceEvent(Voice event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.voiceEvent(event);
        }
    }

    /** Tells all ParserListeners that a tempo event has been parsed. */
    public void fireTempoEvent(Tempo event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.tempoEvent(event);
        }
    }

    /** Tells all ParserListeners that an instrument event has been parsed. */
    public void fireInstrumentEvent(Instrument event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.instrumentEvent(event);
        }
    }

    /** Tells all ParserListeners that a layer event has been parsed. */
    public void fireLayerEvent(Layer event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.layerEvent(event);
        }
    }
    
    /** Tells all ParserListeners that a time event has been parsed. */
    public void fireTimeEvent(Time event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.timeEvent(event);
        }
    }
    
    /** Tells all ParserListeners that a time event has been parsed. */
    public void fireSystemExclusiveEvent(SystemExclusiveEvent event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.systemExclusiveEvent(event);
        }
    }
    
    /** Tells all ParserListeners that a key signature event has been parsed. */
    public void fireKeySignatureEvent(KeySignature event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.keySignatureEvent(event);
        }
    }
    
    /** Tells all ParserListeners that a measure event has been parsed. */
    public void fireMeasureEvent(Measure event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.measureEvent(event);
        }
    }

    /** Tells all ParserListeners that a controller event has been parsed. */
    public void fireControllerEvent(Controller event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.controllerEvent(event);
        }
    }

    /** Tells all ParserListeners that a controller event has been parsed. */
    public void fireChannelPressureEvent(ChannelPressure event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.channelPressureEvent(event);
        }
    }

    /** Tells all ParserListeners that a controller event has been parsed. */
    public void firePolyphonicPressureEvent(PolyphonicPressure event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.polyphonicPressureEvent(event);
        }
    }

    /** Tells all ParserListeners that a controller event has been parsed. */
    public void firePitchBendEvent(PitchBend event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.pitchBendEvent(event);
        }
    }

    /** Tells all ParserListeners that a note event has been parsed. */
    public void fireNoteEvent(Note event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.noteEvent(event);
        }
    }

    /** Tells all ParserListeners that a sequential note event has been parsed. */
    public void fireSequentialNoteEvent(Note event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.sequentialNoteEvent(event);
        }
    }

    /** Tells all ParserListeners that a parallel note event has been parsed. */
    public void fireParallelNoteEvent(Note event)
    {
        ParserListener[] listeners = getParserListeners();
        for (ParserListener listener : listeners) {
            listener.parallelNoteEvent(event);
        }
    }
}
