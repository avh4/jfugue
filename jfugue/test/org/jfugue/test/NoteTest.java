package org.jfugue.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.jfugue.elements.Note;
import org.jfugue.factories.NoteFactory;
import org.jfugue.parsers.MusicStringParser;
import org.jfugue.visitors.LoggingVisitor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NoteTest {

	private static final byte NOTE_DEFAULT_VELOCITY = Note.DEFAULT_VELOCITY + 10;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetDecimalDuration() {
	    Note n1 = new Note((byte) 60, 0.5);
	    assertEquals("Decimal duration and long duration don't match when setting decimal duration",n1.getDecimalDuration(), (n1.getDuration() / MusicStringParser.SEQUENCE_RES), 0.01);
	}

        @Test
        public void testSetLongDuration() {
	    Note n1 = new Note((byte) 60, (long) (MusicStringParser.SEQUENCE_RES * 0.5));
	    assertEquals("Decimal duration and long duration don't match when setting long duration", n1.getDuration(), ((long) (MusicStringParser.SEQUENCE_RES * n1.getDecimalDuration())));
	}
	
	@Test
	public void testNoteCTorWithNoteArg() {
            Note n1 = new Note((byte)50, 0.5, NOTE_DEFAULT_VELOCITY, NOTE_DEFAULT_VELOCITY);
            Note n2 = new Note(n1);
            assertEquals("Note constructed with Note argument should match argument", n1, n2);
	}

	@Test
	public void testAcceptVisitor() throws Exception {
		LoggingVisitor visitor = new LoggingVisitor();
		Note.createNote("C").acceptVisitor(visitor);
		Note.createNote("Cmaj").acceptVisitor(visitor);
		assertEquals("[visitNote(C5q), visitNote(C3q)]", visitor.toString());
	}
	
	@Test
	public void testChords() {
		Map<String, byte[]> chords = Note.Factory.CHORDS_MAP;
		assertFalse("The chord map is empty", chords.isEmpty());
		assertTrue("MAJ is not in CHORDS_MAP", chords.containsKey("MAJ"));
		assertArrayEquals("MAJ intervals should be 4 & 7 semitones", new byte[] { 4, 7 }, chords.get("MAJ"));
	}

	@Test
        public void testEqualsSameNote() {
	    String n1 = "Cb4/0.5";
	    assertEquals("Notes with the same data should be equal", Note.createNote(n1),Note.createNote(n1));
	}

        @Test
        public void testEqualsDifferentValue() {
	    assertFalse("Notes with different values should not be equal", Note.createNote("Cb4/0.5").equals(Note.createNote("D")));
	}
}
