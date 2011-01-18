package org.jfugue.test;

import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static org.junit.Assert.*;

import org.jfugue.ParserListener;
import org.jfugue.ParserProgressListener;
import org.jfugue.Parser;


public class ParserTest {

    @Test public void testParserConstructor() {
	Parser parser = new Parser();
	assertNotNull(parser);
    }
	
    public static class TracingTest {
	private Parser parser;

	@Before
	public void setUp() {
	    parser = new Parser();
	}

	@Test 
	public void testGetTracing(){
	    assertEquals("Default state of tracing should be off", Parser.TRACING_OFF, parser.getTracing());
	}

	@Test 
	public void testGetTracing_ON(){
	    parser.setTracing(Parser.TRACING_ON);
	    assertEquals("Tracing was not enabled",Parser.TRACING_ON, parser.getTracing());
	}

	@Test 
	public void testGetTracing_RESET(){
	    parser.setTracing(Parser.TRACING_ON);
	    parser.setTracing(Parser.TRACING_OFF);
	    assertEquals("Tracing was not disabled", Parser.TRACING_OFF, parser.getTracing());
	}

	@Ignore @Test 
	public void testGetTracing_OUT_OF_BOUNDS(){
	    //This behaviour is, as-yet, undefined.
	    //  We should lock this down, but it is of
	    //  little import, being specifically for logging.
	    parser.setTracing(999);
	}
    }

    public static class AddRemoveListenerTest {
	private Parser parser;

	@Before
	public void setUp() {
	    parser = new Parser();
	}

	@Test 
	public void testProgressListenerList_DefaultState(){
	    assertEquals("ProgressListenerList should be empty by default", 0, parser.getParserProgressListeners().length);
	}

	@Test 
	public void testAddParserProgressListener_Once(){
	    ParserProgressListener listener = mock(ParserProgressListener.class);
	    parser.addParserProgressListener(listener);
	    assertEquals("A listener could not be added to the ProgressListenerList", 1, parser.getParserProgressListeners().length);
	}

	@Test 
	public void testRemoveParserProgressListener_Once(){
	    ParserProgressListener listener = mock(ParserProgressListener.class);
	    parser.addParserProgressListener(listener);
	    parser.removeParserProgressListener(listener);
	    assertEquals("Adding and removing a listener did not leave the ProgressListenerList empty", 0,parser.getParserProgressListeners().length);
	}

	@Test 
	public void testRemoveParserProgressListener_Too_Many(){
	    ParserProgressListener listener = mock(ParserProgressListener.class);
	    parser.addParserProgressListener(listener);
	    parser.removeParserProgressListener(listener);
	    parser.removeParserProgressListener(listener);
	    assertEquals("Removing a ParseProgressListener twice should have the same effect as removing it once", 0, parser.getParserProgressListeners().length);
	}

	@Test 
	public void testListenerList_DefaultState(){
	    assertEquals("ParserListenerList should be empty by default", 0,parser.getParserListeners().length);
	}

	@Test 
	public void testAddParserListener_Once(){
	    ParserListener listener = mock(ParserListener.class);
	    parser.addParserListener(listener);
	    assertEquals("A listener could not be added to the ParserListenerList", 1,parser.getParserListeners().length);
	}

	@Test 
	public void testRemoveParserListener_Once(){
	    ParserListener listener = mock(ParserListener.class);
	    parser.addParserListener(listener);
	    parser.removeParserListener(listener);
	    assertEquals("Adding and removing a listener did not leave the PsrserListenerList empty", 0,parser.getParserListeners().length);
	}

	@Test 
	public void testRemoveParserListener_MoreThanAdd(){
	    ParserListener listener = mock(ParserListener.class);
	    parser.addParserListener(listener);
	    parser.removeParserListener(listener);
	    parser.removeParserListener(listener);
	    assertEquals("Removing a ParserListener twice should have the same effect as removing it once", 0, parser.getParserListeners().length);
	}
    }
}