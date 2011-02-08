package org.jfugue.elements;

import java.util.HashMap;
import java.util.Map;

import org.jfugue.JFugueDefinitions;
import org.jfugue.JFugueException;
import org.jfugue.ParserListener;
import org.jfugue.SystemExclusiveEvent;
import org.jfugue.parsers.DummyParserEventProxy;
import org.jfugue.parsers.FireEventProxy;
import org.jfugue.parsers.ParserContext;

/**
 * 
 * This class contains a dictionary with which symbols such as {@code [blah]}
 * can be looked up in a variety of formats. This dictionary will eventually
 * replace the one in parsers. The other thing it contains is a
 * {@link FireEventProxy}. This class delegates to those two. It is mildly
 * important to keep this contained, as it exposes some of the inner workings of
 * the parser. Mostly this will be wrapped in a {@link ParserContext}.
 * (Or later if we need a {@code RenderContext})
 * 
 * <p>
 * This is <b>not</b> thread safe. Only one thread should be using a given
 * instance, and probably the Patterns that are used with it.
 * 
 * @author joshua
 * 
 */
public class Environment implements FireEventProxy {

	private static Environment instance;

	/**
	 * @return the instance
	 */
	public static Environment getInstance() {
		if (instance == null) {
			instance = new Environment(new HashMap<String, String>(
					JFugueDefinitions.DICT_MAP), new DummyParserEventProxy());
		}
		return instance;
	}

	private Map<String, String> dictionaryMap;
	private FireEventProxy proxy;
	protected KeySignature keySig = new KeySignature(0, 0);

	/**
	 * @param dictionary
	 * @param proxy
	 */
	public Environment(Map<String, String> dictionary, FireEventProxy proxy) {
		super();
		this.dictionaryMap = dictionary;
		this.proxy = proxy;
	}

	public Environment() {
		this(new HashMap<String, String>(JFugueDefinitions.DICT_MAP),
				new DummyParserEventProxy());
	}

	public void add(String key, Object val) {
		key = key.toUpperCase();
		dictionaryMap.put(key, val.toString());
	}

	/**
	 * Looks up a string's value in the dictionary. The dictionary is used to
	 * keep memorable names of obscure numbers - for example, the string FLUTE
	 * is set to a value of 73, so when users want to play music with a flute,
	 * they can say "I[Flute]" instead of "I[73]".
	 * 
	 * <p>
	 * The Dictionary feature also lets users define constants so that if the
	 * value of something were to change, it only needs to be changed in one
	 * place. For example, MY_FAVORITE_INSTRUMENT could be set to 73, then you
	 * can say "I[My_Favorite_Instrument]" when you want to play with that
	 * instrument. If your favorite instrument were ever to change, you only
	 * have to make the change in one place, instead of every place where you
	 * give the Instrument command.
	 * </p>
	 * 
	 * @param bracketedString
	 *            the string to look up in the dictionary
	 * @returns the definition of the string
	 * @throws JFugueException
	 *             if there is a problem looking up bracketedString
	 */
	private String dictionaryLookup(String bracketedString)
			throws JFugueException {
		int indexOfOpeningBracket = bracketedString.indexOf("[");
		int indexOfClosingBracket = bracketedString.indexOf("]");

		String word = null;
		if ((indexOfOpeningBracket != -1) && (indexOfClosingBracket != -1)) {
			word = bracketedString.substring(indexOfOpeningBracket + 1,
					indexOfClosingBracket);
		} else {
			// It appears that "bracketedString" wasn't bracketed.
			word = bracketedString;
		}
		word = word.toUpperCase();

		String definition = (String) dictionaryMap.get(word);
		while ((definition != null)
				&& (dictionaryMap.containsKey(definition.toUpperCase()))) {
			definition = dictionaryMap.get(definition.toUpperCase());
		}

		// If there is no definition for this word, see if the word is actually
		// a number.
		if (null == definition) {
			char ch = 0;
			boolean isNumber = true;
			for (int i = 0; i < word.length(); i++) {
				ch = word.charAt(i);
				if ((!Character.isDigit(ch) && (ch != '.'))) {
					isNumber = false;
				}
			}
			if (isNumber) {
				// trace("Dictionary lookup returning the number ",word);
				return word;
			} else {
				throw new JFugueException(JFugueException.WORD_NOT_DEFINED_EXC,
						word, bracketedString);
			}
		}
		// trace("Word ",word," is defined as ",definition);
		return definition;
	}

	/**
	 * Look up a byte from the dictionary
	 * 
	 * @param bracketedString
	 *            the string to look up
	 * @returns the byte value of the definition
	 * @throws JFugueException
	 *             if there is a problem getting a byte from the dictionary
	 *             look-up
	 */
	public byte getByteFromDictionary(String bracketedString)
			throws JFugueException {
		String definition = dictionaryLookup(bracketedString);
		Byte newbyte = null;
		try {
			newbyte = new Byte(definition);
		} catch (NumberFormatException e) {
			throw new JFugueException(JFugueException.EXPECTED_BYTE,
					definition, bracketedString);
		}
		return newbyte.byteValue();
	}

	/**
	 * Look up a long from the dictionary
	 * 
	 * @param bracketedString
	 *            the string to look up
	 * @returns the long value of the definition
	 * @throws JFugueException
	 *             if there is a problem getting a long from the dictionary
	 *             look-up
	 */
	public long getLongFromDictionary(String bracketedString)
			throws JFugueException {
		String definition = dictionaryLookup(bracketedString);
		Long newlong = null;
		try {
			newlong = new Long(definition);
		} catch (NumberFormatException e) {
			throw new JFugueException(JFugueException.EXPECTED_LONG,
					definition, bracketedString);
		}
		return newlong.longValue();
	}

	/**
	 * Look up an int from the dictionary
	 * 
	 * @param bracketedString
	 *            the string to look up
	 * @returns the int value of the definition
	 * @throws JFugueException
	 *             if there is a problem getting a int from the dictionary
	 *             look-up
	 */
	public int getIntFromDictionary(String bracketedString)
			throws JFugueException {
		String definition = dictionaryLookup(bracketedString);
		Integer newint = null;
		try {
			newint = new Integer(definition);
		} catch (NumberFormatException e) {
			throw new JFugueException(JFugueException.EXPECTED_INT, definition,
					bracketedString);
		}
		return newint.intValue();
	}

	/**
	 * Look up a double from the dictionary
	 * 
	 * @param bracketedString
	 *            the string to look up
	 * @returns the double value of the definition
	 * @throws JFugueException
	 *             if there is a problem getting a double from the dictionary
	 *             look-up
	 */
	public double getDoubleFromDictionary(String bracketedString)
			throws JFugueException {
		String definition = dictionaryLookup(bracketedString);
		Double newdouble = null;
		try {
			newdouble = new Double(definition);
		} catch (NumberFormatException e) {
			throw new JFugueException(JFugueException.EXPECTED_DOUBLE,
					definition, bracketedString);
		}
		return newdouble.doubleValue();
	}

	/**
	 * @param listener
	 * @see org.jfugue.FireEventProxy#addParserListener(org.jfugue.ParserListener)
	 */
	public void addParserListener(ParserListener listener) {
		proxy.addParserListener(listener);
	}

	/**
	 * @param listener
	 * @see org.jfugue.FireEventProxy#removeParserListener(org.jfugue.ParserListener)
	 */
	public void removeParserListener(ParserListener listener) {
		proxy.removeParserListener(listener);
	}

	/**
	 * @return
	 * @see org.jfugue.FireEventProxy#getParserListeners()
	 */
	public ParserListener[] getParserListeners() {
		return proxy.getParserListeners();
	}

	/**
	 * 
	 * @see org.jfugue.FireEventProxy#clearParserListeners()
	 */
	public void clearParserListeners() {
		proxy.clearParserListeners();
	}

	/**
	 * @param event
	 * @see org.jfugue.FireEventProxy#fireVoiceEvent(org.jfugue.elements.Voice)
	 */
	public void fireVoiceEvent(Voice event) {
		proxy.fireVoiceEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireEventProxy#fireTempoEvent(org.jfugue.elements.Tempo)
	 */
	public void fireTempoEvent(Tempo event) {
		proxy.fireTempoEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireEventProxy#fireInstrumentEvent(org.jfugue.elements.Instrument)
	 */
	public void fireInstrumentEvent(Instrument event) {
		proxy.fireInstrumentEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireEventProxy#fireLayerEvent(org.jfugue.elements.Layer)
	 */
	public void fireLayerEvent(Layer event) {
		proxy.fireLayerEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireEventProxy#fireTimeEvent(org.jfugue.elements.Time)
	 */
	public void fireTimeEvent(Time event) {
		proxy.fireTimeEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireEventProxy#fireSystemExclusiveEvent(org.jfugue.SystemExclusiveEvent)
	 */
	public void fireSystemExclusiveEvent(SystemExclusiveEvent event) {
		proxy.fireSystemExclusiveEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireEventProxy#fireKeySignatureEvent(org.jfugue.elements.KeySignature)
	 */
	public void fireKeySignatureEvent(KeySignature event) {
		keySig = event;
		proxy.fireKeySignatureEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireEventProxy#fireMeasureEvent(org.jfugue.elements.Measure)
	 */
	public void fireMeasureEvent(Measure event) {
		proxy.fireMeasureEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireEventProxy#fireControllerEvent(org.jfugue.elements.Controller)
	 */
	public void fireControllerEvent(Controller event) {
		proxy.fireControllerEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireEventProxy#fireChannelPressureEvent(org.jfugue.elements.ChannelPressure)
	 */
	public void fireChannelPressureEvent(ChannelPressure event) {
		proxy.fireChannelPressureEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireEventProxy#firePolyphonicPressureEvent(org.jfugue.elements.PolyphonicPressure)
	 */
	public void firePolyphonicPressureEvent(PolyphonicPressure event) {
		proxy.firePolyphonicPressureEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireEventProxy#firePitchBendEvent(org.jfugue.elements.PitchBend)
	 */
	public void firePitchBendEvent(PitchBend event) {
		proxy.firePitchBendEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireEventProxy#fireNoteEvent(org.jfugue.elements.Note)
	 */
	public void fireNoteEvent(Note event) {
		proxy.fireNoteEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireEventProxy#fireSequentialNoteEvent(org.jfugue.elements.Note)
	 */
	public void fireSequentialNoteEvent(Note event) {
		proxy.fireSequentialNoteEvent(event);
	}

	/**
	 * @param event
	 * @see org.jfugue.FireEventProxy#fireParallelNoteEvent(org.jfugue.elements.Note)
	 */
	public void fireParallelNoteEvent(Note event) {
		proxy.fireParallelNoteEvent(event);
	}

	/**
	 * @return the keySig
	 */
	public KeySignature getKeySig() {
		return keySig;
	}

}
