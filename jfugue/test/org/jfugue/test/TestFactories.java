package org.jfugue.test;


import static org.junit.Assert.*;

import org.jfugue.*;
import org.jfugue.extras.LoggingVisitor;
import org.jfugue.extras.ListenerToVisitorAdaptor;
import org.jfugue.parsers.Environment;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestFactories {
	protected LoggingVisitor visitor = new LoggingVisitor();
	protected ParserListener listener = new ListenerToVisitorAdaptor(visitor);
	protected Environment environment = new Environment();

	
	@Before
	public void setUp() throws Exception {
		environment.addParserListener(listener);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testChannelPressureFactory() throws Exception {
		visitor.clearLog();
		ChannelPressure pressure = ChannelPressure.Factory.getInstance().createElement("+50", environment);
		assertEquals((byte) 50, pressure.getPressure());
		assertEquals("[visitChannelPressure(+50)]", visitor.toString());
	}
	
	@Test
	public void testControllerFactory() throws Exception {
		visitor.clearLog();
		Controller.Factory f = Controller.Factory.getInstance();
		f.createElement("X[BANK_SELECT_FINE]=28", environment);
		f.createElement("x[Foot_Pedal]=1345", environment);
		assertEquals("[visitController(X32=28), visitController(X36=65), visitController(X4=10)]", visitor.toString());
	}
	
	@Test
	public void testInstrumentFactory() throws Exception {
		Instrument i = Instrument.Factory.getInstance().createElement("I[PiAno]");
		assertEquals("I[Piano]", i.getMusicString());
	}
	
	@Ignore("not implemented yet")
	@Test
	public void testKeySignatureFactory() throws Exception {
	}

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
	
	@Ignore("not implemented yet")
	@Test
	public void testNoteFactory() throws Exception {
		
	}
	
	@Ignore("not implemented yet")
	@Test
	public void testPatternFactory() throws Exception {
		
	}
	
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
	
	@Ignore("not implemented yet")
	@Test
	public void testPolyphonicPressureFactory() throws Exception {
		
	}
	
	@Ignore("not implemented yet")
	@Test
	public void testSystemExclusiveEventFactory() throws Exception {
		
	}
	
	@Test
	public void testTempoFactory() throws Exception {
		Tempo t = Tempo.Factory.getInstance().createElement("T160");
		assertEquals("T160", t.getMusicString());
	}
	
	@Ignore("not implemented yet")
	@Test
	public void testTimeFactory() throws Exception {
		
	}
	
	@Test
	public void testVoiceFactory() throws Exception {
		Voice v = Voice.Factory.getInstance().createElement("V5");
		assertEquals("V5", v.getMusicString());
	}
}
