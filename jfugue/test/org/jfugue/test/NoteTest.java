package org.jfugue.test;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jfugue.MusicStringParser;
import org.jfugue.Note;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NoteTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNoteDuration() throws Exception {
		Note n1 = new Note((byte) 60, 0.5);
		assertTrue("Decimal duration and long duration don't match when setting decimal duration", n1.getDecimalDuration() == n1.getDuration() / MusicStringParser.SEQUENCE_RES);
		n1 = new Note((byte) 60, (long) (MusicStringParser.SEQUENCE_RES * 0.5));
		assertTrue("Decimal duration and long duration don't match when setting long duration", ((long) (MusicStringParser.SEQUENCE_RES * n1.getDecimalDuration())) == n1.getDuration());
	}
	
	@Test
	public void testChords() {
		assertFalse("The chord map is empty", Note.NoteFactory.CHORDS_MAP.isEmpty());
	}

}
