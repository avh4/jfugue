package org.jfugue.test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.jfugue.ImmutablePattern;
import org.jfugue.Note;
import org.jfugue.Pattern;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ImmutablePatternTest {

	private static final String MUSIC_STRING = "A B C D";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testImmutablePatternStringMapOfStringString() {
		new ImmutablePattern(MUSIC_STRING, new HashMap<String, String>());
	}

	@Test
	public void testImmutablePatternString() {
		new ImmutablePattern(MUSIC_STRING);
	}

	@Test
	public void testImmutablePattern() {
		new ImmutablePattern();
	}

	@Test
	public void testImmutablePatternStringArray() {
		new ImmutablePattern(MUSIC_STRING, MUSIC_STRING);
	}

	@Test
	public void testImmutablePatternJFugueElementArray() {
		new ImmutablePattern(new Note((byte) 60), new Note((byte) 70));
	}

	@Test
	public void testImmutablePatternPattern() {
		new ImmutablePattern(new Pattern(MUSIC_STRING));
	}

}
