package no.ehealthresearch.dignitycare.parserUtil.matcher;

import java.util.function.Consumer;

import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;

public class InterceptMatcher implements Matcher{

	private final Matcher wrapped;
	private final Consumer<TextLine> interceptor;
	
	private InterceptMatcher(Matcher wrapped, Consumer<TextLine> interceptor) {
		this.wrapped = wrapped;
		this.interceptor = interceptor;
	}
	
	public static InterceptMatcher create(Matcher wrapped, Consumer<TextLine> interceptor) {
		return new InterceptMatcher(wrapped, interceptor);
	}

	@Override
	public void readLine(TextLine line) {
		wrapped.readLine(line);
		interceptor.accept(line);
	}

	@Override
	public MatchingState getState() {
		return wrapped.getState();
	}

}
