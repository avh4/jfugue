package org.jfugue.test;

import org.jfugue.Pattern;
import org.jfugue.StreamingPlayer;

public class StreamingPlayerTest {
	public static void main(String[] args) {
		StreamingPlayer player = new StreamingPlayer();
		player.stream(new Pattern("I[Flute] Cq+Eq+Gq"));
		// player.streamUntilFinished(new Pattern("Aq Aq Aq"));
		// player.closeWhenFinished();

		// Player player = new Player();
		// player.play(new Pattern("Aq Aq Aq"));

	}
}
