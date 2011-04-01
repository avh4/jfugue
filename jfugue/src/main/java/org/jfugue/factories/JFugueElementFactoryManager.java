package org.jfugue.factories;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jfugue.elements.JFugueElement;

public final class JFugueElementFactoryManager {

	private static final Set<JFugueElementFactory<? extends JFugueElement>> factories = new HashSet<JFugueElementFactory<? extends JFugueElement>>();
	private static final Set<JFugueElementFactory<? extends JFugueElement>> imFactories = Collections.unmodifiableSet(factories);
	
	private static final Map<Character, JFugueElementFactory<? extends JFugueElement>> firstChars = new HashMap<Character, JFugueElementFactory<? extends JFugueElement>>();
	private static final Map<Character, JFugueElementFactory<? extends JFugueElement>> imFirstChars = Collections.unmodifiableMap(firstChars);
	private static final Map<JFugueElementFactory<? extends JFugueElement>, Set<Character>> factFirstChars = new HashMap<JFugueElementFactory<? extends JFugueElement>, Set<Character>>();
	
	public static Set<JFugueElementFactory<? extends JFugueElement>> getFactories() {
		return imFactories;
	}
	
	public static Map<Character, JFugueElementFactory<? extends JFugueElement>> getFirstCharsToFactoriesMap() {
		return imFirstChars;
	}
	
	public static void addFactory(JFugueElementFactory<? extends JFugueElement> factory) {
		factories.add(factory);
	}

	public static void addFactory(JFugueElementFactory<? extends JFugueElement> factory, char...cs) {
		factories.add(factory);
		
		// So the factory wants to register some chars.
		// First we add them all to the char->factory map.
		for (char c : cs) {
			firstChars.put(c, factory);
		}
		
		// Then we make sure it is keeping track of the chars.
		Set<Character> sc = factFirstChars.get(factory);
		if (sc == null) {
			sc = new HashSet<Character>(cs.length);
			factFirstChars.put(factory, sc);
		}
		
		// Then we add them to the factory's set of chars.
		for (char c : cs) {
			sc.add(c);
		}
	}

	public static void removeFactory(JFugueElementFactory<? extends JFugueElement> factory) {
		factories.remove(factory);
		
		// If the factory has registered for any chars, remove them, and then remove the set of chars.
		Set<Character> sc = factFirstChars.get(factory);
		if (sc != null) {
			for (Character c : sc) {
				firstChars.remove(c);
			}
			factFirstChars.remove(factory);
		}
	}
	
	public static void removeFactory(JFugueElementFactory<? extends JFugueElement> factory, char...cs) {
		Set<Character> sc = factFirstChars.get(factory);
		if (sc == null)
			return;
		for (char c : cs) {
			firstChars.remove(c);
			sc.remove(c);
		}
		
		// If there are no chars left associated with the factory, remove the set of chars.
		if (sc.isEmpty()) {
			factFirstChars.remove(factory);
			
			// Should we remove the factory entirely?
			factories.remove(factory);
		}
	}
	
}
