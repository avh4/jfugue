package org.jfugue.test;

import java.io.File;

import org.jfugue.Pattern;
import org.jfugue.PatternInterface;
import org.jfugue.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PatternTest {

	public static final String FRERE_JACQUAS_FILE = "FrereJacques.jfugue";
	
	protected Player player = new Player();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	public PatternInterface getFrereJacques() {
		// "Frere Jacques"
		Pattern pattern1 = new Pattern("C5q D5q E5q C5q");

		// "Dormez-vous?"
		Pattern pattern2 = new Pattern("E5q F5q G5h");

		// "Sonnez les matines"
		Pattern pattern3 = new Pattern("G5i A5i G5i F5i E5q C5q");

		// "Ding ding dong"
		Pattern pattern4 = new Pattern("C5q G4q C5h");

		// Put all of the patters together to form the song
		Pattern song = new Pattern();
		song.add(pattern1, 2); // Adds 'pattern1' to 'song' twice
		song.add(pattern2, 2); // Adds 'pattern2' to 'song' twice
		song.add(pattern3, 2); // Adds 'pattern3' to 'song' twice
		song.add(pattern4, 2); // Adds 'pattern4' to 'song' twice

		Pattern doubleMeasureRest = new Pattern("Rw Rw");

		// Create the first voice
		Pattern round1 = new Pattern("V0");
		round1.add(song);

		// Create the second voice
		Pattern round2 = new Pattern("V1");
		round2.add(doubleMeasureRest);
		round2.add(song);

		// Create the third voice
		Pattern round3 = new Pattern("V2");
		round3.add(doubleMeasureRest, 2);
		round3.add(song);

		// Put the voices together
		PatternInterface roundSong = new Pattern();
		roundSong.add(round1);
		roundSong.add(round2);
		roundSong.add(round3);

		roundSong.setProperty(PatternInterface.TITLE, "Frere Jacques");
		return roundSong;
	}

	@Test
	public void testSave() throws Exception {
		PatternInterface song = getFrereJacques();
		song.savePattern(new File(FRERE_JACQUAS_FILE));
	}

	@Test
	public void testLoad() throws Exception {
		Pattern song = Pattern.loadPattern(getClass().getResourceAsStream(FRERE_JACQUAS_FILE));
		System.out.println(getClass().getResource(FRERE_JACQUAS_FILE));
		System.out.println(song.getPropertiesAsParagraph());
		System.out.println(song.getMusicString());
	}

	@Test
	public void testInstruments() {
		player.play("I[Piano] C D E G Rh I[Flute] G F A B");
	}

	@Test
	public void testFrereJacques() {
		PatternInterface song = getFrereJacques();
		player.play(song);
	}

    //Returns true if n0 and n1 are equal to within the tolerance specified.
    private boolean areRoughlyEqual(long n0,long n1,long tolerance) {
        return  Math.abs( n0 - n1) < tolerance;
    };

    @Test
    public void testRestsAtEndOfPattern () {
        Pattern p0 = new Pattern("T480 G5q B5q G5q C6q G10q Rq Rq Rq Rq");
        Player player = new Player();
        long t0 = System.currentTimeMillis();
        player.play(p0);
        long t1 = System.currentTimeMillis();
        long duration = t1 - t0;
        //Because other factors may be in play, one cannot rely on exact millisecond timing.
        //  I believe that 100 is an appropriate tolerance.  This may be refined if other player overheads
        //  are reduced in the future.
        assertTrue("5 notes and 4 rests should take 1125ms to play at 480 bpm", areRoughlyEqual(duration, 1125, 100));
    }

}
