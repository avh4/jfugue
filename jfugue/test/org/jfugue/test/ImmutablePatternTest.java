package org.jfugue.test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.jfugue.ImmutablePattern;
import org.jfugue.Measure;
import org.jfugue.Note;
import org.jfugue.Pattern;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ImmutablePatternTest {

	private static final String MUSIC_STRING = "A B C D";
	public static final String BLAH = "blah";
	private static final Pattern PATTERN = new Pattern(MUSIC_STRING);
	private static final String TOKEN = "|";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private void tryMutatingPattern(Pattern p) {
		String ms = p.getMusicString();
		Map<String,String> props = p.getProperties();
		
		String blah = p.getProperty(BLAH);
		try {
			p.setProperty(BLAH, BLAH);
		} catch (Exception e) {}
		assertEquals("getProperty not same after setProperty", blah, p.getProperty(BLAH));
		
		try {
			props.put(BLAH, BLAH);
		} catch (Exception e) {}
		assertEquals("getProperty not same after props.put", blah, p.getProperty(BLAH));
		
		try {
			p.add(new Measure());
		} catch (Exception e) {}
		assertEquals("getMusicString not same after add(JFugueElement)", ms, p.getMusicString());
		
		try {
			p.add(new Pattern(MUSIC_STRING));
		} catch (Exception e) {}
		assertEquals("getMusicString not same after add(Pattern)", ms, p.getMusicString());
		
		try {
			p.add(MUSIC_STRING);
		} catch (Exception e) {}
		assertEquals("getMusicString not same after add(String)", ms, p.getMusicString());
		
		try {
			p.add(MUSIC_STRING, MUSIC_STRING);
		} catch (Exception e) {}
		assertEquals("getMusicString not same after add(String...)", ms, p.getMusicString());
		
		try {
			p.add(PATTERN, 1);
		} catch (Exception e) {}
		assertEquals("getMusicString not same after add(Pattern,int)", ms, p.getMusicString());
		
		try {
			p.add(MUSIC_STRING, 1);
		} catch (Exception e) {}
		assertEquals("getMusicString not same after add(String,int)", ms, p.getMusicString());
		
		try {
			p.addElement(new Measure());
		} catch (Exception e) {}
		assertEquals("getMusicString not same after addElement(JFugueElement)", ms, p.getMusicString());
		
		try {
			p.insert(MUSIC_STRING);
		} catch (Exception e) {}
		assertEquals("getMusicString not same after insert(String)", ms, p.getMusicString());
		
		try {
			p.repeat(2);
		} catch (Exception e) {}
		assertEquals("getMusicString not same after repeat(int)", ms, p.getMusicString());
		
		try {
			p.repeat(2, 0);
		} catch (Exception e) {}
		assertEquals("getMusicString not same after repeat(int,int)", ms, p.getMusicString());
		
		try {
			p.repeat(2, 0, 2);
		} catch (Exception e) {}
		assertEquals("getMusicString not same after repeat(int,int,int)", ms, p.getMusicString());
		
		try {
			p.replace(0, TOKEN);
		} catch (Exception e) {}
		assertEquals("getMusicString not same after replace(int,String)", ms, p.getMusicString());
		
		try {
			p.replace(0, TOKEN, TOKEN);
		} catch (Exception e) {}
		assertEquals("getMusicString not same after replace(int,String...)", ms, p.getMusicString());
		
	}

	@Test
	public void testImmutablePatternStringMapOfStringString() {
		Pattern p = new ImmutablePattern(MUSIC_STRING, new HashMap<String, String>());
		tryMutatingPattern(p);
	}

	@Test
	public void testImmutablePatternString() {
		Pattern p = new ImmutablePattern(MUSIC_STRING);
		tryMutatingPattern(p);
	}

	@Test
	public void testImmutablePattern() {
		Pattern p = new ImmutablePattern();
		tryMutatingPattern(p);
	}

	@Test
	public void testImmutablePatternStringArray() {
		Pattern p = new ImmutablePattern(MUSIC_STRING, MUSIC_STRING);
		tryMutatingPattern(p);
	}

	@Test
	public void testImmutablePatternJFugueElementArray() {
		Pattern p = new ImmutablePattern(new Note((byte) 60), new Note((byte) 70));
		tryMutatingPattern(p);
	}

	@Test
	public void testImmutablePatternPattern() {
		Pattern p = new ImmutablePattern(new Pattern(MUSIC_STRING));
		tryMutatingPattern(p);
	}

}
