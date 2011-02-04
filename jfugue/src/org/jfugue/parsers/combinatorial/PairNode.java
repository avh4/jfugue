package org.jfugue.parsers.combinatorial;

import java.io.IOException;

import org.jfugue.JFugueException;
import org.jfugue.parsers.ParserContext;
import org.jfugue.parsers.ParserError;

public class PairNode<F extends ParserNode<VF>, S extends ParserNode<VS>, VF, VS> implements ParserNode<Pair<VF,VS>> {

	protected Pair<F,S> pair;
	public Pair<F,S> getPair() {
		return pair;
	}
	
	/**
	 * @param first
	 * @param second
	 */
	public PairNode(F first, S second) {
		pair = new Pair<F,S>(first, second);
	}

	public Pair<VF, VS> execute(ParserContext context) throws ParserError, IOException,
			JFugueException {
		// TODO
		return null;
	}

}
