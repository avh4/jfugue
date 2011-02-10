package org.jfugue.parsers.combinatorial;

import java.io.IOException;

import org.jfugue.JFugueException;
import org.jfugue.parsers.ParserContext;
import org.jfugue.parsers.ParserError;

public class PairNode<F, S> implements ParserNode<Pair<F,S>> {

	protected Pair<ParserNode<F>, ParserNode<S>> pair;
	public Pair<ParserNode<F>, ParserNode<S>> getPair() {
		return pair;
	}
	
	/**
	 * @param first
	 * @param second
	 */
	public PairNode(ParserNode<F> first, ParserNode<S> second) {
		pair = new Pair<ParserNode<F>, ParserNode<S>>(first, second);
	}

	public Pair<F, S> execute(ParserContext context) throws ParserError, IOException,
			JFugueException {
		return new Pair<F,S>(pair.getFirst().execute(context), pair.getSecond().execute(context));
	}

}
