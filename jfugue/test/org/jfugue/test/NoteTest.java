package org.jfugue.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.jfugue.JFugueDefinitions;
import org.jfugue.JFugueException;

import org.jfugue.elements.Note;
import org.jfugue.elements.KeySignature;
import org.jfugue.factories.NoteFactory;
import org.jfugue.parsers.MusicStringParser;
import org.jfugue.visitors.LoggingVisitor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;;

public class NoteTest {
    private static final byte NOTE_DEFAULT_VELOCITY = Note.DEFAULT_VELOCITY + 10;

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testFourArgConstructor() {
        Note n = new Note((byte)10,20,(byte)30,(byte)40);
        assertEquals(10,n.getValue());
        assertEquals(20,n.getDuration());
        assertEquals(30,n.getAttackVelocity());
        assertEquals(40,n.getDecayVelocity());
    }


    @Test
    public void testIsChord() {
        Note note = new Note();
        assertFalse("Note should be not be a chord by default", note.isChord());
    }

    @Test
    public void testIsAdjustedForKey() {
        Note note = new Note();
        assertFalse("Note should be not be a adjusted for key by default", note.isAdjustedForKey());
    }

    @Test
    public void testSetAdjustedForKey() {
        Note note = new Note();
        note.setAdjustedForKey(true);
        assertTrue(note.isAdjustedForKey());
    }

    @Test
    public void testIsRest() {
        Note note = new Note();
        assertFalse("Note should not be a rest by default", note.isRest());
    }

    @Test
    public void testSetRest() {
        Note note = new Note();
        note.setRest(true);
        assertTrue(note.isRest());
    }

    @Test
    public void testSetDecimalDuration() {
        Note n1 = new Note((byte) 60, 0.5);
        assertEquals("Decimal duration and long duration don't match when setting decimal duration",n1.getDecimalDuration(), (n1.getDuration() / JFugueDefinitions.SEQUENCE_RESOLUTION), 0.01);
    }

    @Test
    public void testSetLongDuration() {
        Note n1 = new Note((byte) 60, (long) (JFugueDefinitions.SEQUENCE_RESOLUTION * 0.5));
        assertEquals("Decimal duration and long duration don't match when setting long duration", n1.getDuration(), ((long) (JFugueDefinitions.SEQUENCE_RESOLUTION * n1.getDecimalDuration())));
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

    @Test
    public void testGetVerifyString_Simple() {
        assertEquals("Note: value=60, duration=0.25, startTie=F, endTie=F, attack=64, decay=64, isFirst=T, isParallel=F, isSequential=F", new Note((byte)60).getVerifyString());
    }

    @Test
    public void testGetVerifyString_startTie() {
        Note note =  new Note((byte)60);
        note.setStartOfTie(true);
        assertEquals("Note: value=60, duration=0.25, startTie=T, endTie=F, attack=64, decay=64, isFirst=T, isParallel=F, isSequential=F",note.getVerifyString());
    }

    @Test
    public void testGetVerifyString_endTie() {
        Note note =  new Note((byte)60);
        note.setEndOfTie(true);
        assertEquals("Note: value=60, duration=0.25, startTie=F, endTie=T, attack=64, decay=64, isFirst=T, isParallel=F, isSequential=F",note.getVerifyString());
    }

    @Test
    public void testGetVerifyString_Parallel() {
        Note note =  new Note((byte)60);
        note.setType(Note.PARALLEL);
        assertEquals("Note: value=60, duration=0.25, startTie=F, endTie=F, attack=64, decay=64, isFirst=F, isParallel=T, isSequential=F",note.getVerifyString());
    }

    @Test
    public void testGetVerifyString_Sequential() {
        Note note =  new Note((byte)60);
        note.setType(Note.SEQUENTIAL);
        assertEquals("Note: value=60, duration=0.25, startTie=F, endTie=F, attack=64, decay=64, isFirst=F, isParallel=F, isSequential=T",note.getVerifyString());
    }

    @Test
    public void testGetVerifyStringFromFourArgConstructor() {
        assertEquals("Note: value=51, duration=0.5, startTie=F, endTie=F, attack=53, decay=54, isFirst=T, isParallel=F, isSequential=F", new Note((byte)51, 0.5, (byte)53, (byte)54).getVerifyString());
    }

    @Test
    public void testGetValue() {
	assertEquals(55,new Note((byte)55).getValue());
    }

    @Test
    public void testSetValue_int() {
	Note note = new Note((byte)55);
	note.setValue(65);
	assertEquals(65,note.getValue());
    }

    @Test
    public void testSetValue_byte() {
	Note note = new Note((byte)55);
	note.setValue((byte)75);
	assertEquals(75,note.getValue());
    }
    
    @Test
    public void testAdjustForKey_Rest() {
	Note note = new Note();
	note.setRest(true);
	assertEquals(note, note.adjustForKey(new KeySignature((byte)1,(byte)1)));
    }

    @Test
    public void testAdjustForKey_IsAlreadyAdjusted() { 
	Note note = new Note();
	note.setAdjustedForKey(true);
	assertEquals(note, note.adjustForKey(new KeySignature((byte)1,(byte)1)));
    }

    @Test
    public void testAdjustForKey_ZeroKeysig() { 
	Note note = new Note((byte)12);
	Note note2 = note.adjustForKey(new KeySignature((byte)0,(byte)0));
	assertEquals(note,note2);
    }

    @Test
    public void testAdjustForKey_Natural() { 
	Note note = new Note((byte)12);
	Note note2 = note.adjustForKey(new KeySignature((byte)0,(byte)0));
	assertEquals(note,note2);
    }

    @Test
    public void testAdjustForKey_LowestF_in_GMajor() { 
	Note F = new Note((byte)5);
	Note adjustedF = F.adjustForKey(new KeySignature((byte)1,(byte)0));
	Note sharpF = new Note((byte)6);
	assertEquals(sharpF,adjustedF);
    }

    @Test
    public void testAdjustForKey_MiddleF_in_GMajor() { 
	Note F = new Note((byte)65);
	Note adjustedF = F.adjustForKey(new KeySignature((byte)1,(byte)0));
	Note sharpF = new Note((byte)66);
	assertEquals(sharpF,adjustedF);
    }

    @Test 
    public void testGetSemitoneWithinOctave_ZeroOctave() {
        Note F = new Note((byte)5);
        assertEquals(5,F.getSemitoneWithinOctave());
    }

    @Test
    public void testGetSemitoneWithinOctave_HigherOctave() {
        Note Db = new Note((byte)123);
        assertEquals(3,Db.getSemitoneWithinOctave());
    }

    @Test
    public void testGetSemitoneWithinOctave_NoteZero() {
        Note C = new Note();
        assertEquals(0,C.getSemitoneWithinOctave());
    }

    @Test
    public void testGetSemitoneWithinOctave_HighestNote() {
        Note G = new Note((byte)127);
        assertEquals(7,G.getSemitoneWithinOctave());
    }

    @Test
    public void testGetOctave_Zero() {
        Note Db = new Note((byte)5);
        assertEquals(0,Db.getOctave());
    }

    @Test
    public void testGetOctave_One() {
        Note Gb = new Note((byte)18);
        assertEquals(1,Gb.getOctave());
    }

    @Test
    public void testGetOctave_Ten() {
        Note G = new Note((byte)127);
        assertEquals(10,G.getOctave());
    }

    @Test
    public void testSetOctave_getOctave() {
        Note C = new Note();
	C.setOctave((byte)6);
        assertEquals(6,C.getOctave());
    }    

    @Test
    public void testSetOctave_getNote() {
        Note C = new Note();
	C.setOctave((byte)6);
        assertEquals(72,C.getValue());
    }    

    @Test
    public void testSetOctave_LowerOctave() {
        Note C = new Note();
	C.setOctave((byte)3);
        assertEquals(3,C.getOctave());
        assertEquals(36,C.getValue());
    }    

    @Test(expected=JFugueException.class)
    public void testSetOctave_Out_Of_Upper_Bounds() {
        Note C = new Note();
	C.setOctave((byte)11);
    }    

    @Test(expected=JFugueException.class)
    public void testSetOctave_Out_Of_Upper_Bounds_In_Highest_Octave() {
        Note Ab = new Note((byte)80);
	Ab.setOctave((byte)10);
    }    

    @Test(expected=JFugueException.class)
    public void testSetOctave_Out_Of_Lower_Bounds() {
        Note C = new Note();
	C.setOctave((byte)-1);
    }    
}
