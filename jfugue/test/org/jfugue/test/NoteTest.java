package org.jfugue.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jfugue.MusicStringParser;
import org.jfugue.Note;
import org.jfugue.extras.LoggingVisitor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NoteTest {

	private static final byte NOT_DEFAULT_VELOCITY = Note.DEFAULT_VELOCITY + 10;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNoteDuration() throws Exception {
		Note n1 = new Note((byte) 60, 0.5);
		assertTrue(
				"Decimal duration and long duration don't match when setting decimal duration",
				n1.getDecimalDuration() == n1.getDuration()
						/ MusicStringParser.SEQUENCE_RES);
		n1 = new Note((byte) 60, (long) (MusicStringParser.SEQUENCE_RES * 0.5));
		assertTrue(
				"Decimal duration and long duration don't match when setting long duration",
				((long) (MusicStringParser.SEQUENCE_RES * n1
						.getDecimalDuration())) == n1.getDuration());
	}
	
	@Test
	public void testNoteNote() throws Exception {
		Note n1 = new Note((byte)50, 0.5, NOT_DEFAULT_VELOCITY, NOT_DEFAULT_VELOCITY);
		Note n2 = new Note(n1);
		assertEquals("n1 does not egual n2", n1, n2);
	}

	@Test
	public void testAcceptVisitor() throws Exception {
		LoggingVisitor visitor = new LoggingVisitor();
		String[] strNotes = new String[] {"C", "Cmaj"};
		for (String string : strNotes) {
			Note note = Note.createNote(string);
			note.acceptVisitor(visitor);
			System.out.println(note.getMusicString());
			System.out.println(visitor.getLog().size());
			System.out.println(visitor.toString());
			visitor.clearLog();	
		}
	}
	
//	@Ignore
//	@Test
//	public void testChords() {
//		Map<String, byte[]> chords = Note.NoteFactory.CHORDS_MAP;
//		assertFalse("The chord map is empty", chords.isEmpty());
//		assertTrue("MAJ is not in CHORDS_MAP", chords.containsKey("MAJ"));
//		byte[] bs = chords.get("MAJ");
//		assertArrayEquals("MAJ is not as it should be", new byte[] { 4, 7 }, bs);
//	}

	@Test
	public void testEquality() throws Exception {
		String n1 = "Cb4/0.5", n2 = "D";
		assertEquals("The two " + n1 + " are not equal", Note.createNote(n1),
				Note.createNote(n1));
		assertFalse(n1 + " and " + n2 + " report being equal",
				Note.createNote(n1).equals(Note.createNote(n2)));
	}

}
