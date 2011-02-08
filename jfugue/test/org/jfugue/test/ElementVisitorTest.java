package org.jfugue.test;


import org.jfugue.MusicStringParser;
import org.jfugue.Pattern;
import org.jfugue.extras.ListenerToVisitorAdaptor;
import org.jfugue.extras.LoggingVisitor;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ElementVisitorTest {

	
	protected LoggingVisitor loggingVisitor = new LoggingVisitor();
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore("Maven doesn't like this for some reason")
	@Test
	public void testLoggingVisitor() throws Exception {
		MusicStringParser parser = new MusicStringParser();
		parser.addParserListener(new ListenerToVisitorAdaptor(loggingVisitor));
		parser.parse(new Pattern("Cmaj C D | T160 V1 A B C"));
		System.out.println(loggingVisitor.toString());
	}

}
