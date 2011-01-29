package org.jfugue.test;

import java.io.File;
import java.io.IOException;

import org.jfugue.Pattern;
import org.jfugue.Player;
import org.junit.Test;

/**
 * @deprecated {@link PatternTest} does this and more
 */
public class HardcodedFilePlayer {
	public static void main(String[] args) {

		Player player = new Player();
		Pattern pattern = null;
		try {
			pattern = Pattern.loadPattern(new File(
					"c://download//aChessGame.jfugue"));
			player.play(pattern);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testHardcodedFilePlayer() throws Exception {
		Player player = new Player();
		Pattern pattern = null;
		try {
			pattern = Pattern.loadPattern(getClass().getResourceAsStream(PatternTest.FRERE_JACQUAS_FILE));
			player.play(pattern);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
