package org.jfugue.parsers;

import org.jfugue.ChannelPressure;
import org.jfugue.Controller;
import org.jfugue.Instrument;
import org.jfugue.KeySignature;
import org.jfugue.Layer;
import org.jfugue.Measure;
import org.jfugue.Note;
import org.jfugue.ParserListener;
import org.jfugue.PitchBend;
import org.jfugue.PolyphonicPressure;
import org.jfugue.SystemExclusiveEvent;
import org.jfugue.Tempo;
import org.jfugue.Time;
import org.jfugue.Voice;


public interface FireParserEventProxy {
	
	
    /**
     * Adds a <code>ParserListener</code>.  The listener will receive events when the parser
     * interprets music string tokens.
     *
     * @param listener the listener that is to be notified of parser events
     */
    public void addParserListener(ParserListener listener);

    /**
     * Removes a <code>ParserListener</code>.
     *
     * @param listener the listener to remove
     */
    public void removeParserListener(ParserListener listener);

    /**
     * Returns an array of all <code>ParserListener</code>s.
     * 
     * @return array of all <code>ParserListener</code>s
     */
    public ParserListener[] getParserListeners();
    
    /**
     * Removes all of the <code>ParserListener</code>s.
     */
    public void clearParserListeners();

    /** Tells all ParserListeners that a voice event has been parsed. */
    public void fireVoiceEvent(Voice event);

    /** Tells all ParserListeners that a tempo event has been parsed. */
    public void fireTempoEvent(Tempo event);

    /** Tells all ParserListeners that an instrument event has been parsed. */
    public void fireInstrumentEvent(Instrument event);

    /** Tells all ParserListeners that a layer event has been parsed. */
    public void fireLayerEvent(Layer event);
    
    /** Tells all ParserListeners that a time event has been parsed. */
    public void fireTimeEvent(Time event);
    
    /** Tells all ParserListeners that a time event has been parsed. */
    public void fireSystemExclusiveEvent(SystemExclusiveEvent event);
    
    /** Tells all ParserListeners that a key signature event has been parsed. */
    public void fireKeySignatureEvent(KeySignature event);
    
    /** Tells all ParserListeners that a measure event has been parsed. */
    public void fireMeasureEvent(Measure event);

    /** Tells all ParserListeners that a controller event has been parsed. */
    public void fireControllerEvent(Controller event);

    /** Tells all ParserListeners that a controller event has been parsed. */
    public void fireChannelPressureEvent(ChannelPressure event);

    /** Tells all ParserListeners that a controller event has been parsed. */
    public void firePolyphonicPressureEvent(PolyphonicPressure event);

    /** Tells all ParserListeners that a controller event has been parsed. */
    public void firePitchBendEvent(PitchBend event);

    /** Tells all ParserListeners that a note event has been parsed. */
    public void fireNoteEvent(Note event);

    /** Tells all ParserListeners that a sequential note event has been parsed. */
    public void fireSequentialNoteEvent(Note event);

    /** Tells all ParserListeners that a parallel note event has been parsed. */
    public void fireParallelNoteEvent(Note event);
}
