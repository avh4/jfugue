package org.jfugue.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.jfugue.Pattern;
import org.jfugue.Tempo;
import org.jfugue.extras.CanonicalPatternTransformer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CanonicalPatternTransformerTest extends TestCaseHelpers {

	protected static final CanonicalPatternTransformer cpt = new CanonicalPatternTransformer();
	protected static final Pattern abcd = new Pattern("A B C D");

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSimpleReflexitivity() {
		assertCanonEq(abcd, abcd);
	}

	@Test
	public void testTempo() {
		Tempo tempo1 = new Tempo(120), tempo2 = new Tempo(240);
		Pattern test1 = new Pattern(tempo1, abcd), test2 = new Pattern(test1,
				tempo2), test3 = new Pattern(test2, tempo1);
		assertCanonEq(test1, new Pattern(test1, tempo1));
		assertCononNotEq(test1, test2);
		assertCononNotEq(test2, test3);
		assertCononNotEq(test1, test3);
	}

	private void assertCanonEq(Pattern one, Pattern two) {
		assertEquals(cpt.execute(one), cpt.execute(two));
	}

	private void assertCononNotEq(Pattern one, Pattern two) {
		assertFalse(String.format("%s == %s", one, two), cpt.execute(one)
				.equals(cpt.execute(two)));
	}

}
