package org.jfugue.test;


import static org.junit.Assert.*;

import java.util.Map;

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
	public void testNote() {
		System.out.println(Note.NoteFactory.CHORD_RE);
		Map<String, byte[]> chords = Note.NoteFactory.CHORDS_MAP;
		
		assertFalse(!chords.isEmpty());
	}

}
