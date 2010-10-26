package org.jfugue.test;

import java.io.File;
import java.io.IOException;

import org.jfugue.Pattern;
import org.jfugue.Player;

public class HardcodedFilePlayer {
	    public static void main(String[] args) {
	        // TODO code application logic here

	Player player = new Player();
	Pattern pattern = null;
	try {
	pattern = Pattern.loadPattern(
	        new File("c://download//aChessGame.jfugue"));
	player.play(pattern);
	}catch (IOException e){
	e.printStackTrace();
	}

	    }

	}

