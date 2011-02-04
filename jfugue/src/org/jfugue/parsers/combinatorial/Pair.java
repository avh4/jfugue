package org.jfugue.parsers.combinatorial;

public class Pair<F,S> {
	protected F first;
	protected S second;
	/**
	 * @return the first
	 */
	public F getFirst() {
		return first;
	}
	/**
	 * @return the second
	 */
	public S getSecond() {
		return second;
	}
	/**
	 * @param first
	 * @param second
	 */
	public Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}
}