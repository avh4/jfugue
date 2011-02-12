package org.jfugue.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.jfugue.parsers.MusicStringParser;
import org.jfugue.PatternInterface;
import org.jfugue.ParserProgressListener;
import org.jfugue.ParserListener;
import org.jfugue.elements.Tempo;
import org.jfugue.elements.Voice;


import org.junit.Before;
import org.junit.Test;

public class MusicStringParserTest {
    private MusicStringParser parser;

    @Before
    public void setUp() {
        parser = new MusicStringParser();
    }

    @Test
    public void testDefaultTempoEnabled(){
        assertTrue(parser.isDefaultTempoEnabled());
    }

    @Test
    public void testSetDefaultTempoEnabled(){
        parser.setDefaultTempoEnabled(false);
        assertFalse(parser.isDefaultTempoEnabled());
    }

    @Test
    public void testReSetDefaultTempoEnabled(){
        parser.setDefaultTempoEnabled(false);
        parser.setDefaultTempoEnabled(true);
        assertTrue(parser.isDefaultTempoEnabled());
    }

    @Test
    public void testParseNoTokensDoesNothing(){
        ParserProgressListener listener0 = mock(ParserProgressListener.class);
        ParserListener listener1 = mock(ParserListener.class);
        parser.addParserProgressListener(listener0);
        parser.addParserListener(listener1);
        String [] tokl = {};
        parser.parse(getMockPattern(tokl));
        verifyZeroInteractions(listener0,listener1);
    }

    @Test
    public void testParseSingleTokenFiresProgressEvent(){
        ParserProgressListener listener = mock(ParserProgressListener.class);
        parser.addParserProgressListener(listener);
        parser.parse(getSingleTokenMockPattern());
        verify(listener).progressReported("Parsing music string...", 1, 1);
    }

    private PatternInterface getSingleTokenMockPattern(){
        String [] tokl = {"V0"};
        return getMockPattern(tokl);
    }

    private PatternInterface getMockPattern(String [] tokl) {
        PatternInterface pattern = mock(PatternInterface.class);
        when(pattern.getTokens()).thenReturn(tokl);
        return pattern;
    }

    @Test
    public void testParseSingleTokenFiresDefaultTempoEvent(){
        ParserListener listener = mock(ParserListener.class);
        parser.addParserListener(listener);
        parser.parse(getSingleTokenMockPattern());
        verify(listener).tempoEvent(new Tempo(120));
        verify(listener).voiceEvent(new Voice((byte)0));
    }

    @Test
    public void testParseSingleTokenDoesNotFireDefaultTempoEventWhenDefaultTempoDisabled(){
        ParserListener listener = mock(ParserListener.class);
        parser.addParserListener(listener);
        parser.setDefaultTempoEnabled(false);
        parser.parse(getSingleTokenMockPattern());
        verify(listener,never()).tempoEvent(new Tempo(120));
    }

    @Test
    public void testParseSingleTokenDoesNotFireDefaultTempoEventWhenTempoPresent(){
        ParserListener listener = mock(ParserListener.class);
        parser.addParserListener(listener);
        String [] tokl = {"T240"};
        parser.parse(getMockPattern(tokl));
        verify(listener,never()).tempoEvent(new Tempo(120));
        verify(listener).tempoEvent(new Tempo(240));
    }

    @Test
    public void testParseMultipleTokensFiresMultipleEvents(){
        ParserListener listener = mock(ParserListener.class);
        parser.addParserListener(listener);
        String [] tokl = {"T240", "V0", "V1"};
        parser.parse(getMockPattern(tokl));
        verify(listener).tempoEvent(new Tempo(240));
        verify(listener).voiceEvent(new Voice((byte)0));
        verify(listener).voiceEvent(new Voice((byte)1));
    }

    @Test
    public void testParseMultipleTokensFiresMultipleProgressEvents(){
        ParserProgressListener listener = mock(ParserProgressListener.class);
        parser.addParserProgressListener(listener);
        String [] tokl = {"T240", "V0", "V1"};
        parser.parse(getMockPattern(tokl));
        verify(listener).progressReported("Parsing music string...", 1, 3);
        verify(listener).progressReported("Parsing music string...", 2, 3);
        verify(listener).progressReported("Parsing music string...", 3, 3);
    }
}