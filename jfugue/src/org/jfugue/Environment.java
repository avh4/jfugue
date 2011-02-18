package org.jfugue;

import java.util.HashMap;
import java.util.Map;

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
import org.jfugue.parsers.DummyParserEventProxy;
import org.jfugue.parsers.FireEventProxy;
import org.jfugue.parsers.ParserContext;

import org.apache.log4j.Logger;

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

	private Map<String, String> dictionaryMap;

	private FireEventProxy proxy;

	protected KeySignature keySig = new KeySignature(0, 0);

	public Environment() {
		this(new HashMap<String, String>(JFugueDefinitions.DICT_MAP),
				new DummyParserEventProxy());
	}
	/**
	 * @param dictionary
	 * @param proxy
	 */
	public Environment(Map<String, String> dictionary, FireEventProxy proxy) {
		super();
		this.dictionaryMap = dictionary;
		this.proxy = proxy;
	}
	public void add(String key, Object val) {
		key = key.toUpperCase();
		// TODO Should we allow arbitrary Objects?
		dictionaryMap.put(key, val.toString());
	}

	/**
	 * Look up a byte from the dictionary
	 * 
	 * @param bracketedString
	 *            the string to look up
	 * @return the byte value of the definition
	 * @throws JFugueException
	 *             if there is a problem getting a byte from the dictionary
	 *             look-up
	 */
	public byte getByteFromDictionary(String bracketedString)
			throws Error {
		String definition = dictionaryLookup(bracketedString);
		Byte newbyte = null;
		try {
			newbyte = new Byte(definition);
		} catch (NumberFormatException e) {
			throw new Error(Error.EXPECTED_BYTE,
					definition, bracketedString);
		}
		return newbyte.byteValue();
	}

	/**
	 * Look up a double from the dictionary
	 * 
	 * @param bracketedString
	 *            the string to look up
	 * @return the double value of the definition
	 * @throws JFugueException
	 *             if there is a problem getting a double from the dictionary
	 *             look-up
	 */
	public double getDoubleFromDictionary(String bracketedString)
			throws Error {
		String definition = dictionaryLookup(bracketedString);
		Double newdouble = null;
		try {
			newdouble = new Double(definition);
		} catch (NumberFormatException e) {
			throw new Error(Error.EXPECTED_DOUBLE,
					definition, bracketedString);
		}
		return newdouble.doubleValue();
	}

	/**
	 * Look up an int from the dictionary
	 * 
	 * @param bracketedString
	 *            the string to look up
	 * @return the int value of the definition
	 * @throws JFugueException
	 *             if there is a problem getting a int from the dictionary
	 *             look-up
	 */
	public int getIntFromDictionary(String bracketedString)
			throws Error {
		String definition = dictionaryLookup(bracketedString);
		Integer newint = null;
		try {
			newint = new Integer(definition);
		} catch (NumberFormatException e) {
			throw new Error(Error.EXPECTED_INT, definition,
					bracketedString);
		}
		return newint.intValue();
	}

	/**
	 * @return the keySig
	 */
	public KeySignature getKeySig() {
		return keySig;
	}

	/**
	 * Look up a long from the dictionary
	 * 
	 * @param bracketedString
	 *            the string to look up
	 * @return the long value of the definition
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
			throw new Error(Error.EXPECTED_LONG,
					definition, bracketedString);
		}
		return newlong.longValue();
	}

	/**
	 * @return null because you aren't allowed
	 * @see org.jfugue.parsers.FireEventProxy#getParserListeners()
	 */
	public ParserListener[] getParserListeners() {
		return null; //proxy.getParserListeners();
	}

	/**
	 * @param listener
	 * @see org.jfugue.parsers.FireEventProxy#removeParserListener(org.jfugue.ParserListener)
	 */
	public void removeParserListener(ParserListener listener) {
		proxy.removeParserListener(listener);
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
	 * @return the definition of the string
	 * @throws JFugueException
	 *             if there is a problem looking up bracketedString
	 */
	private String dictionaryLookup(String bracketedString)
			throws Error {
		int indexOfOpeningBracket = bracketedString.indexOf("["); //$NON-NLS-1$
		int indexOfClosingBracket = bracketedString.indexOf("]"); //$NON-NLS-1$

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
				// Logger.getRootLogger().trace("Dictionary lookup returning the number ",word);
				return word;
			} else {
				throw new Error(Error.WORD_NOT_DEFINED_EXC,
						word, bracketedString);
			}
		}
		// Logger.getRootLogger().trace("Word ",word," is defined as ",definition);
		return definition;
	}

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

	private static Environment instance;

	/**
	 * @author joshua
	 *
	 */
	@SuppressWarnings("serial")
	public static final class Error extends JFugueException {

		/**
		 * @param exc
		 */
		public Error(Exception exc) {
			super(exc);
		}

		/**
		 * @param format
		 * @param objects
		 */
		public Error(String format, Object... objects) {
			super(format, objects);
		}

		public static final String EXPECTED_BYTE = Messages.getString("Environment.EXP_BYTE"); //$NON-NLS-1$
		public static final String EXPECTED_DOUBLE = Messages.getString("Environment.EXP_DOUBLE"); //$NON-NLS-1$
		public static final String EXPECTED_INT = Messages.getString("Environment.EXP_INT"); //$NON-NLS-1$
		public static final String EXPECTED_LONG = Messages.getString("Environment.EXP_LONG"); //$NON-NLS-1$
		public static final String WORD_NOT_DEFINED_EXC = Messages.getString("Environment.NO_DEF"); //$NON-NLS-1$
	}

	/**
	 * @param listener
	 * @see org.jfugue.parsers.FireEventProxy#addParserListener(org.jfugue.ParserListener)
	 */
	public void addParserListener(ParserListener listener) {
		proxy.addParserListener(listener);
	}
	/**
	 * 
	 * @see org.jfugue.parsers.FireEventProxy#clearParserListeners()
	 */
	public void clearParserListeners() {
		proxy.clearParserListeners();
	}
	/**
	 * @param event
	 * @see org.jfugue.parsers.FireEventProxy#fireVoiceEvent(org.jfugue.elements.Voice)
	 */
	public void fireVoiceEvent(Voice event) {
		proxy.fireVoiceEvent(event);
	}
	/**
	 * @param event
	 * @see org.jfugue.parsers.FireEventProxy#fireTempoEvent(org.jfugue.elements.Tempo)
	 */
	public void fireTempoEvent(Tempo event) {
		proxy.fireTempoEvent(event);
	}
	/**
	 * @param event
	 * @see org.jfugue.parsers.FireEventProxy#fireInstrumentEvent(org.jfugue.elements.Instrument)
	 */
	public void fireInstrumentEvent(Instrument event) {
		proxy.fireInstrumentEvent(event);
	}
	/**
	 * @param event
	 * @see org.jfugue.parsers.FireEventProxy#fireLayerEvent(org.jfugue.elements.Layer)
	 */
	public void fireLayerEvent(Layer event) {
		proxy.fireLayerEvent(event);
	}
	/**
	 * @param event
	 * @see org.jfugue.parsers.FireEventProxy#fireTimeEvent(org.jfugue.elements.Time)
	 */
	public void fireTimeEvent(Time event) {
		proxy.fireTimeEvent(event);
	}
	/**
	 * @param event
	 * @see org.jfugue.parsers.FireEventProxy#fireSystemExclusiveEvent(org.jfugue.elements.SystemExclusive)
	 */
	public void fireSystemExclusiveEvent(SystemExclusive event) {
		proxy.fireSystemExclusiveEvent(event);
	}
	/**
	 * @param event
	 * @see org.jfugue.parsers.FireEventProxy#fireKeySignatureEvent(org.jfugue.elements.KeySignature)
	 */
	public void fireKeySignatureEvent(KeySignature event) {
		proxy.fireKeySignatureEvent(event);
	}
	/**
	 * @param event
	 * @see org.jfugue.parsers.FireEventProxy#fireMeasureEvent(org.jfugue.elements.Measure)
	 */
	public void fireMeasureEvent(Measure event) {
		proxy.fireMeasureEvent(event);
	}
	/**
	 * @param event
	 * @see org.jfugue.parsers.FireEventProxy#fireControllerEvent(org.jfugue.elements.Controller)
	 */
	public void fireControllerEvent(Controller event) {
		proxy.fireControllerEvent(event);
	}
	/**
	 * @param event
	 * @see org.jfugue.parsers.FireEventProxy#fireChannelPressureEvent(org.jfugue.elements.ChannelPressure)
	 */
	public void fireChannelPressureEvent(ChannelPressure event) {
		proxy.fireChannelPressureEvent(event);
	}
	/**
	 * @param event
	 * @see org.jfugue.parsers.FireEventProxy#firePolyphonicPressureEvent(org.jfugue.elements.PolyphonicPressure)
	 */
	public void firePolyphonicPressureEvent(PolyphonicPressure event) {
		proxy.firePolyphonicPressureEvent(event);
	}
	/**
	 * @param event
	 * @see org.jfugue.parsers.FireEventProxy#firePitchBendEvent(org.jfugue.elements.PitchBend)
	 */
	public void firePitchBendEvent(PitchBend event) {
		proxy.firePitchBendEvent(event);
	}
	/**
	 * @param event
	 * @see org.jfugue.parsers.FireEventProxy#fireNoteEvent(org.jfugue.elements.Note)
	 */
	public void fireNoteEvent(Note event) {
		proxy.fireNoteEvent(event);
	}
	/**
	 * @param event
	 * @see org.jfugue.parsers.FireEventProxy#fireSequentialNoteEvent(org.jfugue.elements.Note)
	 */
	public void fireSequentialNoteEvent(Note event) {
		proxy.fireSequentialNoteEvent(event);
	}
	/**
	 * @param event
	 * @see org.jfugue.parsers.FireEventProxy#fireParallelNoteEvent(org.jfugue.elements.Note)
	 */
	public void fireParallelNoteEvent(Note event) {
		proxy.fireParallelNoteEvent(event);
	}
	public void setKeySig(KeySignature keySig) {
		this.keySig = keySig;
	}

}
