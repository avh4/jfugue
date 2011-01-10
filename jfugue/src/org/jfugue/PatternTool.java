package org.jfugue;

public abstract class PatternTool<R> extends ParserListenerAdapter {

	private R result;
	private Pattern pattern;

	public R getResult() {
		if (result == null)
			result = initResult(null);
		return result;
	}

	protected void setResult(R result) {
		this.result = result;
	}
	
	protected abstract R initResult(Pattern pattern);

	public Pattern getPattern() {
		return pattern;
	}

	public PatternTool() {
	}
	
	public final R execute(Pattern pattern) {
		this.pattern = pattern;
		MusicStringParser parser = new MusicStringParser();
		parser.addParserListener(this);
		
		reset(pattern);
		try {
            parser.parse(pattern);
        } catch (JFugueException e)
        {
            e.printStackTrace(); // TODO Should do something else with this.
        }
        return getResult();
	}

	public void reset(Pattern pattern) {
		setResult(initResult(pattern));
	}

}
