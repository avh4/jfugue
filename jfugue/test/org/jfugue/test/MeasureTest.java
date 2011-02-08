package org.jfugue.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.jfugue.Measure;
import org.jfugue.ElementVisitor;

public class MeasureTest {
    private Measure measure;

    @Before
    public void setUp() {
	measure = new Measure();
    }

    @Test
    public void testGetMusicString() {
	assertEquals("|", measure.getMusicString());
    }

    @Test
    public void testGetVerifyString() {
	assertEquals("Measure", measure.getVerifyString());
    }

    @Test
    public void testAcceptVisitor(){
	ElementVisitor v = mock(ElementVisitor.class);
	measure.acceptVisitor(v);
	verify(v).visit(measure);
    }

    @Test
    public void testEqualsNull(){
	assertFalse(measure.equals(null));
    }

    @Test
    public void testEqualsSomethingElse(){
	assertFalse(measure.equals(new String()));
    }

    @Test
    public void testEqualsSelf(){
	assertTrue(measure.equals(measure));
    }

    @Test
    public void testEqualsADifferentMeasure(){
	//Reflects behaviour at time of writing test.
	assertTrue(measure.equals(new Measure()));
    }

    @Test
    public void testHashCode() {
	assertEquals(201, measure.hashCode());
    }
}
