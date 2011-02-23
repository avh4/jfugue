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

    @Test 
    public void testGetMusicString() {
        assertEquals("C5q", new Note((byte)60).getMusicString());
    }

    @Test 
    public void testGetMusicString_Rest() {
	Note r = new Note();
	r.setRest(true);
        assertEquals("Rq",r.getMusicString());
    }

    @Test
    public void testGetMusicString_Sequential() {
	Note C = new Note((byte)60);
	C.setType(Note.SEQUENTIAL);
        assertEquals("_C5q",C.getMusicString());
    }

    @Test
    public void testGetMusicString_Parallel() {
	Note C = new Note((byte)60);
	C.setType(Note.PARALLEL);
        assertEquals("+C5q",C.getMusicString());
    }

    @Test
    public void testGetMusicString_NonDefault_Attack() {
        assertEquals("C5qa5", new Note((byte)60,32,(byte)5,Note.DEFAULT_VELOCITY).getMusicString());
    }

    @Test
    public void testGetMusicString_NonDefault_Decay() {
        assertEquals("C5qd5", new Note((byte)60,32,Note.DEFAULT_VELOCITY,(byte)5).getMusicString());
    }

    @Test
    public void testGetMusicString_NonDefault_AttackAndDecay() {
        assertEquals("C5qa6d7",new Note((byte)60,32,(byte)6,(byte)7).getMusicString());
    }

    @Test
    public void testGetMusicString_Semibreve() {
	assertEquals("C5w", new Note((byte)60,128).getMusicString());
    }

    @Test
    public void testGetMusicString_DottedMinim() {
	assertEquals("C5h.", new Note((byte)60,96).getMusicString());
    }

    @Test
    public void testGetMusicString_Minim() {
	assertEquals("C5h", new Note((byte)60,64).getMusicString());
    }

    @Test
    public void testGetMusicString_DottedCrotchet() {
	assertEquals("C5q.", new Note((byte)60,48).getMusicString());
    }

    @Test
    public void testGetMusicString_Crotchet() {
	assertEquals("C5q", new Note((byte)60,32).getMusicString());
    }

    @Test
    public void testGetMusicString_DottedQuaver() {
	assertEquals("C5i.", new Note((byte)60,24).getMusicString());
    }

    @Test
    public void testGetMusicString_Quaver() {
	assertEquals("C5i", new Note((byte)60,16).getMusicString());
    }

    @Test
    public void testGetMusicString_DottedSemiQuaver() {
	assertEquals("C5s.", new Note((byte)60,12).getMusicString());
    }

    @Test
    public void testGetMusicString_SemiQuaver() {
	assertEquals("C5s", new Note((byte)60,8).getMusicString());
    }

    @Test
    public void testGetMusicString_DottedDemiSemiQuaver() {
	assertEquals("C5t.", new Note((byte)60,6).getMusicString());
    }

    @Test
    public void testGetMusicString_DemiSemiQuaver() {
	assertEquals("C5t", new Note((byte)60,4).getMusicString());
    }

    @Test
    public void testGetMusicString_DottedHemiDemiSemiQuaver() {
	assertEquals("C5x.", new Note((byte)60,3).getMusicString());
    }

    @Test
    public void testGetMusicString_HemiDemiSemiQuaver() {
	assertEquals("C5x", new Note((byte)60,2).getMusicString());
    }

    @Test
    public void testGetMusicString_DottedSemiHemiDemiSemiQuaver() {
	assertEquals("C5o.", new Note((byte)60,0.01171875).getMusicString());
    }

    @Test
    public void testGetMusicString_SemiHemiDemiSemiQuaver() {
	assertEquals("C5o", new Note((byte)60,1).getMusicString());
    }

    @Test
    public void testGetMusicString_CustomDuration() {
	assertEquals("C5/2.0", new Note((byte)60,256).getMusicString());
    }

    @Test
    public void testGetFrequencyForNote_LowestC() {
	assertEquals(8.175, Note.getFrequencyForNote(0),0.1);
    }

    @Test
    public void testGetFrequencyForNote_HigestG() {
	assertEquals(12543.854, Note.getFrequencyForNote(127),0.1);
    }

    @Test
    public void testGetFrequencyForNote_MiddleAb() {
	assertEquals(415.305, Note.getFrequencyForNote(68),0.1);
    }

    @Test
    public void testGetFrequencyForNote_3rdOctaveB() {
	assertEquals(61.735, Note.getFrequencyForNote(35),0.1);
    }

}
