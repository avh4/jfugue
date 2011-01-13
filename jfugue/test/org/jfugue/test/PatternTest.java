package org.jfugue.test;


import org.jfugue.Pattern;
import org.jfugue.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PatternTest {
	
	protected Player player = new Player();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testFrereJacques() {		
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
		Pattern roundSong = new Pattern();
		roundSong.add(round1);
		roundSong.add(round2);
		roundSong.add(round3);

		// Play the song!
		player.play(roundSong);
	}
	
	@Test
	public void testInstruments() {
		player.play("I[Piano] C D E G Rh I[Flute] G F A B");
	}

}