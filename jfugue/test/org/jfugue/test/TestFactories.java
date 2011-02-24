package org.jfugue.test;


import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.regex.Matcher;

import org.jfugue.*;
import org.jfugue.elements.ChannelPressure;
import org.jfugue.elements.Controller;
import org.jfugue.elements.Instrument;
import org.jfugue.elements.JFugueElement;
import org.jfugue.elements.KeySignature;
import org.jfugue.elements.Layer;
import org.jfugue.elements.Measure;
import org.jfugue.elements.PitchBend;
import org.jfugue.elements.PolyphonicPressure;
import org.jfugue.elements.SystemExclusive;
import org.jfugue.elements.Tempo;
import org.jfugue.elements.Time;
import org.jfugue.elements.Voice;
import org.jfugue.factories.NoteFactory;
import org.jfugue.parsers.ParserContext;
import org.jfugue.visitors.ListenerToVisitorAdaptor;
import org.jfugue.visitors.LoggingVisitor;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestFactories {
	protected LoggingVisitor visitor = new LoggingVisitor();
	protected ParserListener listener = new ListenerToVisitorAdaptor(visitor);
	protected Environment environment = new Environment();
	protected ParserContext pc = new ParserContext(new StringReader(""), environment);

	
	@Before
	public void setUp() throws Exception {
		environment.addParserListener(listener);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	protected void setString(String string) {
		pc.setReader(new StringReader(string));
	}
	
//	@Ignore
	@Test
	public void testChannelPressureFactory() throws Exception {
		visitor.clearLog();
		String expected = "+50";
		setString("+50");
		ChannelPressure pressure = ChannelPressure.Factory.getInstance().createElement(pc);
		assertEquals(expected, pressure.getMusicString());
		assertEquals((byte) 50, pressure.getPressure());
		assertEquals("[visitChannelPressure(+50)]", visitor.toString());
	}
	
//	@Ignore
	@Test
	public void testControllerFactory() throws Exception {
		visitor.clearLog();
		Controller.Factory f = Controller.Factory.getInstance();
		setString("X[BANK_SELECT_FINE]=28 x[Foot_Pedal]=1345");
//		f.createElement("X[BANK_SELECT_FINE]=28", environment);
//		f.createElement("x[Foot_Pedal]=1345", environment);
		f.createElement(pc);
		f.createElement(pc);
//		System.out.println(visitor.toString());
		assertEquals("[visitController(X32=28), visitController(X36=65), visitController(X4=10)]", visitor.toString());
	}
	
//	@Ignore
	@Test
	public void testInstrumentFactory() throws Exception {
		Instrument i = Instrument.Factory.getInstance().createElement("I[PiAno]");
		assertEquals("I[Piano]", i.getMusicString());
	}
	
	@Ignore
	@Test
	public void testKeySignatureFactory() throws Exception {
		String expected = "KCbmaj";
		KeySignature.Factory f = KeySignature.Factory.getInstance();
		KeySignature actual = f.createElement(expected);
		assertEquals(expected, actual.getMusicString());
		expected = "KF#maj";
		actual = f.createElement(expected);
		assertEquals(expected, actual.getMusicString());
		expected = "KFmaj";
		actual = f.createElement(expected);
		assertEquals(expected, actual.getMusicString());
	}

//	@Ignore
	@Test
	public void testLayerFactory() throws Exception {
		Layer l = Layer.Factory.getInstance().createElement("L5");
		assertEquals("L5", l.getMusicString());
	}
	
	@Test
	public void testMeasureFactory() throws Exception {
		Measure m = Measure.Factory.getInstance().createElement("|");
		assertEquals("|", m.getMusicString());
	}
	
//	@Ignore
	@Test
	public void testPitchBendFactory() throws Exception {
		visitor.clearLog();
		PitchBend.Factory f = PitchBend.Factory.getInstance();
		PitchBend p = f.createElement("&65,10", environment);
		assertEquals("&1345", p.getMusicString());
		p = f.createElement("&1345", environment);
		assertEquals("&1345", p.getMusicString());
		assertEquals("[visitPitchBend(&1345), visitPitchBend(&1345)]", visitor.toString());
	}
	
//	@Ignore
	@Test
	public void testPolyphonicPressureFactory() throws Exception {
		PolyphonicPressure.Factory f = PolyphonicPressure.Factory.getInstance();
		String exp = "*40,50";
		assertJEquals(exp, f.createElement(exp));
	}
	
//	@Ignore
	@Test
	public void testSystemExclusiveEventFactory() throws Exception {
		SystemExclusive.Factory f = SystemExclusive.Factory.getInstance();
		String exp = "^DEC:10,40,20,60";
		assertJEquals(exp, f.createElement(exp));
		assertJEquals(exp, f.createElement("^HEX:0A,28,14,3C"));
	}
	
	@Ignore("not implemented yet")
	@Test
	public void testNoteFactory() throws Exception {
//		NoteFactory f = NoteFactory.getInstance();
//		System.out.println(NoteFactory.NOTE_PAT);
//		Matcher m = NoteFactory.NOTE_REGEX.matcher("C#");
//		assertTrue(m.matches());
//		System.out.println(m);
//		m.reset("[61]");
//		assertTrue(m.matches());
//		System.out.println(m);
//		
//		tNote("C");
//		tNote("C#");
//		tNote("C#4");
//		tNote("C#4maj");
//		tNote("C#4W");
	}
	
//	private void tNote(String s) {
//		Matcher m = NoteFactory.NOTE_PAT.matcher(s);
//		assertTrue(m.matches());
//		for (int i = 0; i <= m.groupCount(); i++) {
//			System.out.println(m.group(i));
//		}
//	}
	
	@Ignore("not implemented yet")
	@Test
	public void testPatternFactory() throws Exception {
		
	}
	
//	@Ignore
	@Test
	public void testTempoFactory() throws Exception {
		String expected = "T160";
		setString(expected);
		Tempo t = Tempo.Factory.getInstance().createElement(pc);
		assertEquals("T160", t.getMusicString());
	}
	
//	@Ignore
	@Test
	public void testTimeFactory() throws Exception {
		String exp = "@123456";
		assertJEquals(exp, Time.Factory.getInstance().createElement(exp));
	}
	
//	@Ignore
	@Test
	public void testVoiceFactory() throws Exception {
		Voice v = Voice.Factory.getInstance().createElement("V5");
		assertEquals("V5", v.getMusicString());
	}
	
	public void assertJEquals(String expected, JFugueElement actual) {
		assertEquals(expected, actual.getMusicString());
	}
}
