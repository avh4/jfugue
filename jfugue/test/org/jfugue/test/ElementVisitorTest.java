package org.jfugue.test;


import org.jfugue.Pattern;
import org.jfugue.parsers.MusicStringParser;
import org.jfugue.visitors.ListenerToVisitorAdaptor;
import org.jfugue.visitors.LoggingVisitor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ElementVisitorTest {

	
	protected LoggingVisitor loggingVisitor = new LoggingVisitor();
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoggingVisitor() throws Exception {
		MusicStringParser parser = new MusicStringParser();
		parser.addParserListener(new ListenerToVisitorAdaptor(loggingVisitor));
		parser.parse(new Pattern("Cmaj C D | T160 V1 A B C"));
		assertEquals("[visitTempo(T120), visitNote(C3q), visitNote(+E3q), visitNote(+G3q), visitNote(C5q), visitNote(D5q), visitMeasure(|), visitTempo(T160), visitVoice(V1), visitNote(A5q), visitNote(B5q), visitNote(C5q)]", loggingVisitor.toString());
	}
}
