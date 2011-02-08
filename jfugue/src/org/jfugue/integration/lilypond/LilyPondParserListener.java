package org.jfugue.integration.lilypond;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.jfugue.ParserListener;
import org.jfugue.SystemExclusiveEvent;
import org.jfugue.elements.ChannelPressure;
import org.jfugue.elements.Controller;
import org.jfugue.elements.Instrument;
import org.jfugue.elements.KeySignature;
import org.jfugue.elements.Layer;
import org.jfugue.elements.Measure;
import org.jfugue.elements.Note;
import org.jfugue.elements.PitchBend;
import org.jfugue.elements.PolyphonicPressure;
import org.jfugue.elements.Tempo;
import org.jfugue.elements.Time;
import org.jfugue.elements.Voice;

/**
 * This class listens to events from the MusicString parser. In response to this
 * events, a Lilypond string is produced. The Lilypond string is produced with
 * relative octave notation.
 * 
 * @author Hans Beemsterboer
 */
public class LilyPondParserListener implements ParserListener {

	private boolean addFirstNote = false;
	private String chordDuration = "4";
	private boolean closeStaff = false;
	private String currentNote = "c";
	private int currentOctave = 4;
	private String firstChordNote = null;
	private int firstChordOctave = 0;
	private String firstNote = null;
	private String instrument;
	private boolean isAfterChordProcessed = false;
	private boolean isDotted = false;
	private boolean isFirstNote = true;
	private boolean isRest = false;
	private String lastNote = "c";
	private boolean lastNoteIsFirstNote = false;
	private int lastOctave = 4;
	private StringBuffer lyBuffer = new StringBuffer();
	private StringBuffer lyString = new StringBuffer();
	private List<String> parallelList = new ArrayList<String>();
	private List<String> polyphoneList = new ArrayList<String>();
	private boolean processParallel = false;
	private boolean processPolyphony = false;

	/**
	 * This method determines octave changes that are relative to the previous
	 * note.
	 * <ul>
	 * <li>JFugue: [ c4 d4 e4 f4 g4 a4 b4 ] c5 d5 e5 f5 g5 a5 b5 | c6</li>
	 * <li>Lilypond: c d e f [g' a b c d e f ] g' a b c</li>
	 * </ul>
	 * 
	 * @param lyString
	 */
	private void addOctaveChange(StringBuffer lyString) {
		int octaveChange = currentOctave - lastOctave;
		int lilypondChange = lilypondRelativeDirection(lastNote.charAt(0),
				currentNote.charAt(0));
		int jfugueChange = jfugueOctaveChange(lastNote.charAt(0), currentNote
				.charAt(0), lilypondChange);
		octaveChange += jfugueChange;
		if (octaveChange > 0) {
			for (int i = 0; i < octaveChange; i++) {
				lyString.append("'");
			}
		}
		if (octaveChange < 0) {
			for (int i = 0; i > octaveChange; i--) {
				lyString.append(",");
			}
		}
	}

	public void channelPressureEvent(ChannelPressure arg0) {
	}

	public void controllerEvent(Controller arg0) {
	}

	public void createLyFile() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					"jfugue2lilypond.ly"));
			out.write("\\header {\n");
			out.write("title = \"Untitled\"\n");
			out.write("composer = \"Composer\"\n");
			out.write("copyright = \"Public Domain\"\n");
			out.write("}\n");
			out.write("\\relative c' {\n");
			out.write("<<\n");
			out.write(getLyString() + "\n");
			out.write(">>\n");
			out.write("}\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getDuration(String duration) {
		int durationLy = 4;
		switch (duration.charAt(0)) {
		case 's':
			durationLy = 4;
			break;
		case 'i':
			durationLy = 8;
			break;
		case 'q':
			durationLy = 4;
			break;
		case 'h':
			durationLy = 2;
			break;
		case 'w':
			durationLy = 1;
			break;
		default:
			durationLy = 4;
			break;
		}
		return durationLy;
	}

	private String getLilypondNoteLength(String note) {
		int length = 0;
		length = note.length();
		if (note.contains(".")) {
			return note.substring(length - 2, length - 1);
		} else {
			return note.substring(length - 1, length);
		}
	}

	public String getLyString() {
		if (!lyString.toString().contains("v")) {
			lyString = new StringBuffer("v " + lyString.toString());
		}
		translateChords();
		if (closeStaff) {
			lyString.append(" } ");
		}
		return lyString.toString();
	}

	public void instrumentEvent(Instrument arg0) {
		lyString.append(" i" + arg0.getInstrument() + " ");
	}

	private int jfugueOctaveChange(char firstNote, char secondNote,
			int lilypondDirection) {
		char curChar = firstNote;
		int steps = Math.abs(lilypondDirection) + 1;
		for (int i = 1; Math.abs(i) < steps; i += 1) {
			if (lilypondDirection > 0) {
				curChar++;
				if (curChar > 'g') {
					curChar = 'a';
				}
				if ((firstNote < 'c' || firstNote >= 'g') && curChar > 'b') {
					return -1;
				}
			} else {
				curChar--;
				if (curChar < 'a') {
					curChar = 'g';
				}
				if ((firstNote >= 'c' && firstNote < 'f') && curChar < 'c') {
					return 1;
				}
			}
		}
		return 0;
	}

	public void keySignatureEvent(KeySignature arg0) {
	}

	public void layerEvent(Layer arg0) {
	}

	private int lilypondRelativeDirection(char firstNote, char secondNote) {
		char curChar = firstNote;
		if (firstNote == secondNote) {
			return 0;
		}
		for (int i = 1; i < 4; i++) {
			curChar++;
			if (curChar > 'g') {
				curChar = 'a';
			}

			if (curChar == secondNote) {
				return i;
			}
		}
		curChar = firstNote;
		for (int i = 1; i < 4; i++) {
			curChar--;
			if (curChar < 'a') {
				curChar = 'g';
			}

			if (curChar == secondNote) {
				return -i;
			}
		}
		return 0;
	}

	public void measureEvent(Measure arg0) {
	}

	public void noteEvent(Note arg0) {
		String note = arg0.getMusicString();
		System.out.println("Test: "+note);		
		if (note.startsWith("C0") || note.startsWith("R")) {
			lyString.append("r");
			isRest = true;
		} else if (note.contains("maj")) {
			// Does nothing
		} else {
			currentNote = note.substring(0, 1).toLowerCase();
			lyString.append(currentNote);
		}

		// Use the octave settings of the first chord note.
		if (firstChordNote != null) {
			lastNote = firstChordNote;
			lastOctave = firstChordOctave;
		}
		if (firstChordNote != null && !isRest) {
			firstChordNote = null;
		}

		processNote(lyString, note, false);
		if (lastNoteIsFirstNote) {
			firstChordNote = currentNote;
			firstChordOctave = currentOctave;
		}
		lastNoteIsFirstNote = true;
		lyString.append(" ");
	}

	public void parallelNoteEvent(Note arg0) {
		if (lastNoteIsFirstNote) {
			firstChordNote = currentNote;
			firstChordOctave = currentOctave;
			lastNoteIsFirstNote = false;
		}
		System.out.println("Test2: "+arg0.getMusicString());
		if (arg0.getMusicString().startsWith("+C0") || arg0.getMusicString().startsWith("R")) {
			lyString.append("+r");
			isRest = true;
			processNote(lyString, arg0.getMusicString().substring(1), true);
			lyString.append(" ");
		} else if (arg0.getMusicString().contains("/")) {
			String note = arg0.getMusicString().substring(0, 1);
			lyString.append("+");
			lyString.append(note.toLowerCase());
			currentNote = note.toLowerCase();
			processNote(lyString, arg0.getMusicString().substring(1), true);
			lyString.append(" ");
		} else {
			String note = arg0.getMusicString().substring(1, 2);
			lyString.append("+");
			lyString.append(note.toLowerCase());
			currentNote = note.toLowerCase();
			System.out.println("Subnote: "+arg0.getMusicString());
			if (arg0.getMusicString().startsWith("R")) {
				isRest = true;
			}
			processNote(lyString, arg0.getMusicString().substring(1), true);
			lyString.append(" ");
		}
	}

	public void pitchBendEvent(PitchBend arg0) {
	}

	public void polyphonicPressureEvent(PolyphonicPressure arg0) {
	}

	private void postProcess(StringBuffer lyString) {
		if (processPolyphony) {
			if (addFirstNote) {
				processPolyphonyLastNote(lyString);
			}
		} else if (processParallel) {
			lyString.append(">");
			lyString.append(chordDuration);
			if (isDotted) {
				lyString.append(".");
			}
			lyString.append(" ");
		} else if (addFirstNote) {
			lyString.append(firstNote);
		}
	}

	private void processChord(StringBuffer lyString) {
		boolean isDotted = false;
		Iterator<String> iterator = parallelList.iterator();
		lyString.append("<");
		while (iterator.hasNext()) {
			String note = (String) iterator.next();
			chordDuration = getLilypondNoteLength(note);
			if (note.contains(".")) {
				isDotted = true;
				note = note.substring(0, note.length() - 2);
			} else {
				note = note.substring(0, note.length() - 1);
			}
			lyString.append(note);
			if (iterator.hasNext()) {
				lyString.append(" ");
			}
		}
		lyString.append(">");
		lyString.append(chordDuration);
		if (isDotted) {
			lyString.append(".");
		}
		lyString.append(" ");
	}

	private void processNote(StringBuffer lyString, String note,
			boolean isParallel) {
		if (!isRest) {
			String secondLetter = note.substring(1, 2);
			if (secondLetter.equals("b")) {
				lyString.append("es");
				currentOctave = Integer.parseInt(note.substring(2, 3));
				addOctaveChange(lyString);
				lyString.append(getDuration(note.substring(3, 4)));

			} else if (secondLetter.equals("#")) {
				lyString.append("is");
				currentOctave = Integer.parseInt(note.substring(2, 3));
				addOctaveChange(lyString);
				lyString.append(getDuration(note.substring(3, 4)));

			} else {
				System.out.println("Test: "+isRest+" "+note+", "+lyString);
				currentOctave = Integer.parseInt(note.substring(1, 2));
				addOctaveChange(lyString);
				lyString.append(getDuration(note.substring(2, 3)));
			}
		} else {
			System.out.println("Test: "+note);
			if (note.length() > 2) {
				lyString.append(getDuration(note.substring(2, 3)));
			} else {
				lyString.append(getDuration(note.substring(1, 2)));
			}
			isRest = false;
		}
		if (note.contains(".")) {
			lyString.append(".");
		}
		lastNote = currentNote;
		lastOctave = currentOctave;
	}

	private void processParallel(StringBuffer lyString) {
		Iterator<String> iterator = parallelList.iterator();
		lyString.append("<");
		while (iterator.hasNext()) {
			String note = (String) iterator.next();
			chordDuration = getLilypondNoteLength(note);
			if (note.contains(".")) {
				isDotted = true;
				note = note.substring(0, note.length() - 2);
			} else {
				note = note.substring(0, note.length() - 1);
			}
			lyString.append(note);
			if (iterator.hasNext()) {
				lyString.append(" ");

			}
		}
	}

	private void processPolyphony(StringBuffer lyString) {
		Iterator<String> parallelIterator = parallelList.iterator();
		Iterator<String> polyphoneIterator = polyphoneList.iterator();
		lyString.append("<< { ");
		while (parallelIterator.hasNext()) {
			String note = (String) parallelIterator.next();
			lyString.append(note);

		}
		lyString.append(" } \\\\ { ");
		while (polyphoneIterator.hasNext()) {
			String note = (String) polyphoneIterator.next();
			lyString.append(note);

		}
		lyString.append(" } ");
	}

	private void processPolyphonyLastNote(StringBuffer lyString) {
		Iterator<String> parallelIterator = parallelList.iterator();
		Iterator<String> polyphoneIterator = polyphoneList.iterator();
		String lastNote = null;

		lyString.append("<< { ");
		while (parallelIterator.hasNext()) {
			String note = (String) parallelIterator.next();
			if (parallelIterator.hasNext()) {
				lyString.append(note);
			} else {
				lastNote = note;
			}
		}
		lyString.append(" } \\\\ { " + lastNote + " ");
		while (polyphoneIterator.hasNext()) {
			String note = (String) polyphoneIterator.next();
			lyString.append(note);

		}
		lyString.append(" } >>");
	}

	public void sequentialNoteEvent(Note arg0) {
		System.out.println("Test seq: "+arg0.getMusicString());
		String note = arg0.getMusicString().substring(1, 2);
		lyString.append("_");
		lyString.append(note.toLowerCase());
		currentNote = note.toLowerCase();
		processNote(lyString, arg0.getMusicString().substring(1), false);
		lyString.append(" ");
		firstChordNote = null;
	}

	public void tempoEvent(Tempo arg0) {
	}

	public void timeEvent(Time arg0) {
	}

	private void translateChords() {
		StringTokenizer tokenizer = new StringTokenizer(lyString.toString());
		String prevNote = null;
		String curNote = null;
		while (tokenizer.hasMoreTokens()) {
			curNote = tokenizer.nextToken();
			if (curNote.startsWith("v")) {
				if (addFirstNote && processParallel) {
					processParallel(lyBuffer);
				}
				postProcess(lyBuffer);
				processParallel = false;
				addFirstNote = false;
				parallelList = new ArrayList<String>();
				polyphoneList = new ArrayList<String>();
				// Start new staff. If needed, close the previous staff.
				if (closeStaff) {
					lyBuffer.append(" }\n");
				}
				lyBuffer.append("\\new Staff { ");
				curNote = tokenizer.nextToken();
				closeStaff = true;
			}
			if (curNote.startsWith("i")) {
				String id = curNote.substring(1);
				instrument = "\\set Staff.instrumentName = \""
						+ Instrument.INSTRUMENT_NAME[Integer.parseInt(id)]
						+ "\" ";
				lyBuffer.append(instrument);
				curNote = tokenizer.nextToken();
			}

			translateNote(prevNote, curNote);

			if (isAfterChordProcessed) {

				isAfterChordProcessed = false;
				isFirstNote = true;
			}

			prevNote = curNote;
		}
		if (addFirstNote && processParallel) {
			processParallel(lyBuffer);
		}
		postProcess(lyBuffer);
		lyString = lyBuffer;
		return;
	}

	private void translateNote(String prevNote, String curNote) {

		if (!curNote.contains("_") && !curNote.contains("+")) {
			if (processPolyphony) {
				if (addFirstNote && processParallel) {
					processParallel(lyBuffer);
				}
				postProcess(lyBuffer);
				addFirstNote = false;
				isAfterChordProcessed = true;
				parallelList = new ArrayList<String>();
				polyphoneList = new ArrayList<String>();
				processPolyphony = false;
			}
		}

		if (addFirstNote && !curNote.contains("+") && !curNote.contains("_")
				&& !processParallel) {
			lyBuffer.append(prevNote + " ");
			addFirstNote = false;
		}
		if (curNote.contains("+")) {
			if (!processParallel) {
				parallelList.add(prevNote);
			}
			processParallel = true;
			parallelList.add(curNote.substring(1));
		}

		if (processParallel && curNote.contains("_")) {
			processParallel = false;
			processPolyphony = true;
			polyphoneList = new ArrayList<String>();
			polyphoneList.add(curNote.substring(1));
		} else if (processPolyphony && curNote.contains("_")) {
			polyphoneList.add(" " + curNote.substring(1));
		}
		if (processParallel && !curNote.contains("_") && !curNote.contains("+")) {
			// process chord
			processParallel = false;
			processChord(lyBuffer);
			isAfterChordProcessed = true;
			parallelList = new ArrayList<String>();
		}
		if (processPolyphony && !curNote.contains("_")
				&& !curNote.contains("+")) {
			// process polyphony
			processPolyphony = false;
			processPolyphony(lyBuffer);
		}
		if (!processParallel && !processPolyphony && !isFirstNote
				&& !isAfterChordProcessed) {
			// process note
			lyBuffer.append(curNote + " ");

		}
		if (isFirstNote && !curNote.contains("+")) {
			addFirstNote = true;
			firstNote = curNote;
		}
	}

	public void voiceEvent(Voice arg0) {
		lyString.append(" v ");
	}

	public void systemExclusiveEvent(SystemExclusiveEvent sysex) {
	}

}
