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
import org.jfugue.CollatedParserListener;

import org.jfugue.elements.Tempo;
import org.jfugue.elements.Note;
import org.jfugue.elements.Voice;
import org.jfugue.elements.JFugueElement;


import org.junit.Before;
import org.junit.Test;


import org.jfugue.test.TestCaseHelpers;

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

    @Test
    //method exists for testing only, may be redundant
    public void testIsValidToken_Success() {
	assertTrue(parser.isValidToken("C3qh"));
    }

    @Test
    //method exists for testing only, may be redundant
    public void testIsValidToken_Fail() {
	assertFalse(parser.isValidToken("This isn't a token"));
    }

    // Don't forget -- individual tokens ONLY!  No strings with spaces!
    private void verifyToken(String musicStringToken, final String expected) {
        ParserListener listener = new CollatedParserListener() {
            private StringBuilder results = new StringBuilder();
            private boolean first = true;

            public void jfugueEvent(JFugueElement e) {
		if (!first) {
		    results.append("; ");
		}
                results.append(e.getVerifyString());
                first = false;
            }
            public String toString() {
		return results.toString();
            }
	};

        parser.addParserListener(listener);

	parseToken(musicStringToken);
        parser.removeParserListener(listener);
	assertEquals(expected,listener.toString());
    }

    private void parseToken(String token) {
	Class [] argTypes = {String.class};
	Object [] args = {token};
        TestCaseHelpers.invokeRestrictedMethod(parser,MusicStringParser.class,"parseToken",argTypes,args);    
    }

    @Test 
    public void testParseC() {
        verifyToken("C", Note.createVerifyString(60, 0.25));
    }

    @Test 
    public void testParseC3() {
        verifyToken("C3", Note.createVerifyString(36, 0.25));
    }

    @Test 
    public void testParseC3Flat() {
        verifyToken("Cb3", Note.createVerifyString(35, 0.25));
    }

    @Test 
    public void testParseB3Sharp() {
        verifyToken("B#3", Note.createVerifyString(48, 0.25));
    }

    @Test 
    public void testParseC3Eighth() {
        verifyToken("C3i", Note.createVerifyString(36, 0.125));
    }

    @Test 
    public void testParseC3QuarterHalf() {
        verifyToken("C3qh", Note.createVerifyString(36, 0.75));
    }

    @Test 
    public void testParseC5majorWhole() {
        verifyToken("C5minw", Note.createCompoundVerifyString(Note.createVerifyString(60, 1.0), Note.createVerifyString(63, 1.0, false, true, false), Note.createVerifyString(67, 1.0, false, true, false)));
    }

    @Test 
    public void testParseCmajor() {
        verifyToken("Cmaj", Note.createCompoundVerifyString(Note.createVerifyString(36, 0.25), Note.createVerifyString(40, 0.25, false, true, false), Note.createVerifyString(43, 0.25, false, true, false)));
    }

    @Test
    public void testParseInstrument_Unwrapped_Number() {
        verifyToken("I0","Instrument: instrument=0");
    }

    @Test
    public void testParseInstrument_Bracketed_Number() {
        verifyToken("I[13]","Instrument: instrument=13");
    }

    @Test
    public void testParseInstrument_Unwrapped_Name() {
        verifyToken("IFlute","Instrument: instrument=73");
    }

    @Test
    public void testParseInstrument_Bracketed_Name() {
        verifyToken("I[Acoustic_Grand]","Instrument: instrument=0");
    }

    // 3.0  Layers
    @Test
    public void testParseLayer() {
        verifyToken("L[8]","Layer: layer=8");
    }

    @Test
    public void testParseLayerWithDictionary() {
        verifyToken("$number1=1","");
        verifyToken("L[Number1]","Layer: layer=1");
    }
 
    // 4.1 System Exclusive
    @Test
    public void testParseSysex_Decimal() {
        verifyToken("^dec:240,67,127,0,0,3,0,65,247"
,"SysEx: bytes=-16,67,127,0,0,3,0,65,-9");
    }

    @Test
    public void testParseSysex_Hex() {
        verifyToken("^hex:F0,43,7F,00,00,03,00,41,F7","SysEx: bytes=-16,67,127,0,0,3,0,65,-9");
    }

    @Test
    public void testParseSysex_Decimal_UpperCase() {
        verifyToken("^DEC:240,67,127,0,0,3,0,65,247"
,"SysEx: bytes=-16,67,127,0,0,3,0,65,-9");
    }

    @Test
    public void testParseSysex_Hex_UpperCase() {
        verifyToken("^HEX:F0,43,7F,00,00,03,00,41,F7","SysEx: bytes=-16,67,127,0,0,3,0,65,-9");
    }
    
    //Expected exception should be a JFugueException, but this is concealed by invokeRestrictedMethod
    @Test(expected=AssertionError.class)
	public void testParseSysex_ErroneousRadix() {
	parseToken("^boo:F0,43,7F,00,00,03,00,41,F7");
    }
}