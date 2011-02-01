package org.jfugue;

import java.util.Map;

public class Environment {
	
	private Map<String,String> dictionaryMap;
	private FireParserEvent proxy;
	
	/**
	 * @param dictionary
	 * @param proxy
	 */
	public Environment(Map<String, String> dictionary, FireParserEvent proxy) {
		super();
		this.dictionaryMap = dictionary;
		this.proxy = proxy;
	}

    /**
     * Looks up a string's value in the dictionary.  The dictionary is used to
     * keep memorable names of obscure numbers - for example, the string FLUTE
     * is set to a value of 73, so when users want to play music with a flute,
     * they can say "I[Flute]" instead of "I[73]".
     *
     * <p>
     * The Dictionary feature also lets users define constants so that if the
     * value of something were to change, it only needs to be changed in one
     * place.  For example, MY_FAVORITE_INSTRUMENT could be set to 73, then you
     * can say "I[My_Favorite_Instrument]" when you want to play with that
     * instrument.  If your favorite instrument were ever to change, you only
     * have to make the change in one place, instead of every place where you
     * give the Instrument command.
     * </p>
     *
     * @param bracketedString the string to look up in the dictionary
     * @returns the definition of the string
     * @throws JFugueException if there is a problem looking up bracketedString
     */
    private String dictionaryLookup(String bracketedString) throws JFugueException
    {
        int indexOfOpeningBracket = bracketedString.indexOf("[");
        int indexOfClosingBracket = bracketedString.indexOf("]");

        String word = null;
        if ((indexOfOpeningBracket != -1) && (indexOfClosingBracket != -1)) {
            word = bracketedString.substring(indexOfOpeningBracket+1,indexOfClosingBracket);
        }
        else {
            // It appears that "bracketedString" wasn't bracketed.
            word = bracketedString;
        }
        word = word.toUpperCase();

        String definition = (String)dictionaryMap.get(word);
        while ((definition != null) && (dictionaryMap.containsKey(definition.toUpperCase()))) {
            definition = dictionaryMap.get(definition.toUpperCase());
        }

        // If there is no definition for this word, see if the word is actually a number.
        if (null == definition) {
            char ch = 0;
            boolean isNumber = true;
            for (int i=0; i < word.length(); i++) {
                ch = word.charAt(i);
                if ((!Character.isDigit(ch) && (ch != '.'))) {
                    isNumber = false;
                }
            }
            if (isNumber) {
//                trace("Dictionary lookup returning the number ",word);
                return word;
            } else {
                throw new JFugueException(JFugueException.WORD_NOT_DEFINED_EXC,word,bracketedString);
            }
        }
//        trace("Word ",word," is defined as ",definition);
        return definition;
    }

    /**
     * Look up a byte from the dictionary
     * @param bracketedString the string to look up
     * @returns the byte value of the definition
     * @throws JFugueException if there is a problem getting a byte from the dictionary look-up
     */
    public byte getByteFromDictionary(String bracketedString) throws JFugueException
    {
        String definition = dictionaryLookup(bracketedString);
        Byte newbyte = null;
        try {
            newbyte = new Byte(definition);
        } catch (NumberFormatException e) {
            throw new JFugueException(JFugueException.EXPECTED_BYTE, definition, bracketedString);
        }
        return newbyte.byteValue();
    }

    /**
     * Look up a long from the dictionary
     * @param bracketedString the string to look up
     * @returns the long value of the definition
     * @throws JFugueException if there is a problem getting a long from the dictionary look-up
     */
    public long getLongFromDictionary(String bracketedString) throws JFugueException
    {
        String definition = dictionaryLookup(bracketedString);
        Long newlong = null;
        try {
            newlong = new Long(definition);
        } catch (NumberFormatException e) {
            throw new JFugueException(JFugueException.EXPECTED_LONG,definition,bracketedString);
        }
        return newlong.longValue();
    }

    /**
     * Look up an int from the dictionary
     * @param bracketedString the string to look up
     * @returns the int value of the definition
     * @throws JFugueException if there is a problem getting a int from the dictionary look-up
     */
    public int getIntFromDictionary(String bracketedString) throws JFugueException
    {
        String definition = dictionaryLookup(bracketedString);
        Integer newint = null;
        try {
            newint = new Integer(definition);
        } catch (NumberFormatException e) {
            throw new JFugueException(JFugueException.EXPECTED_INT,definition,bracketedString);
        }
        return newint.intValue();
    }

    /**
     * Look up a double from the dictionary
     * @param bracketedString the string to look up
     * @returns the double value of the definition
     * @throws JFugueException if there is a problem getting a double from the dictionary look-up
     */
    public double getDoubleFromDictionary(String bracketedString) throws JFugueException
    {
        String definition = dictionaryLookup(bracketedString);
        Double newdouble = null;
        try {
            newdouble = new Double(definition);
        } catch (NumberFormatException e) {
            throw new JFugueException(JFugueException.EXPECTED_DOUBLE,definition,bracketedString);
        }
        return newdouble.doubleValue();
    }
    
	/**
	 * @param listener
	 * @see org.jfugue.FireParserEvent#addParserListener(org.jfugue.ParserListener)
	 */
	public void addParserListener(ParserListener listener) {
		proxy.addParserListener(listener);
	}

	/**
	 * @param listener
	 * @see org.jfugue.FireParserEvent#removeParserListener(org.jfugue.ParserListener)
	 */
	public void removeParserListener(ParserListener listener) {
		proxy.removeParserListener(listener);
	}

	/**
	 * @return
	 * @see org.jfugue.FireParserEvent#getParserListeners()
	 */
	public ParserListener[] getParserListeners() {
		return proxy.getParserListeners();
	}

	/**
	 * 
	 * @see org.jfugue.FireParserEvent#clearParserListeners()
	 */
	public void clearParserListeners() {
		proxy.clearParserListeners();
	}

	/**
	 * @param event
	 * @see org.jfugue.FireParserEvent#fireVoiceEvent(org.jfugue.Voice)
	 */
	public void fireVoiceEvent(Voice event) {
		proxy.fireVoiceEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireParserEvent#fireTempoEvent(org.jfugue.Tempo)
	 */
	public void fireTempoEvent(Tempo event) {
		proxy.fireTempoEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireParserEvent#fireInstrumentEvent(org.jfugue.Instrument)
	 */
	public void fireInstrumentEvent(Instrument event) {
		proxy.fireInstrumentEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireParserEvent#fireLayerEvent(org.jfugue.Layer)
	 */
	public void fireLayerEvent(Layer event) {
		proxy.fireLayerEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireParserEvent#fireTimeEvent(org.jfugue.Time)
	 */
	public void fireTimeEvent(Time event) {
		proxy.fireTimeEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireParserEvent#fireSystemExclusiveEvent(org.jfugue.SystemExclusiveEvent)
	 */
	public void fireSystemExclusiveEvent(SystemExclusiveEvent event) {
		proxy.fireSystemExclusiveEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireParserEvent#fireKeySignatureEvent(org.jfugue.KeySignature)
	 */
	public void fireKeySignatureEvent(KeySignature event) {
		proxy.fireKeySignatureEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireParserEvent#fireMeasureEvent(org.jfugue.Measure)
	 */
	public void fireMeasureEvent(Measure event) {
		proxy.fireMeasureEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireParserEvent#fireControllerEvent(org.jfugue.Controller)
	 */
	public void fireControllerEvent(Controller event) {
		proxy.fireControllerEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireParserEvent#fireChannelPressureEvent(org.jfugue.ChannelPressure)
	 */
	public void fireChannelPressureEvent(ChannelPressure event) {
		proxy.fireChannelPressureEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireParserEvent#firePolyphonicPressureEvent(org.jfugue.PolyphonicPressure)
	 */
	public void firePolyphonicPressureEvent(PolyphonicPressure event) {
		proxy.firePolyphonicPressureEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireParserEvent#firePitchBendEvent(org.jfugue.PitchBend)
	 */
	public void firePitchBendEvent(PitchBend event) {
		proxy.firePitchBendEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireParserEvent#fireNoteEvent(org.jfugue.Note)
	 */
	public void fireNoteEvent(Note event) {
		proxy.fireNoteEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireParserEvent#fireSequentialNoteEvent(org.jfugue.Note)
	 */
	public void fireSequentialNoteEvent(Note event) {
		proxy.fireSequentialNoteEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireParserEvent#fireParallelNoteEvent(org.jfugue.Note)
	 */
	public void fireParallelNoteEvent(Note event) {
		proxy.fireParallelNoteEvent(event);
	}
	
	
	
	
	
}
