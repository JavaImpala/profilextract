package no.ehealthresearch.dignitycare.parserUtil.matcher;
import java.util.function.Predicate;

import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;

public class SingleLineMatcher implements Matcher{
	private MatchingState state=MatchingState.READY;
	private final Predicate<TextLine> predicate;

	private SingleLineMatcher(Predicate<TextLine> predicate) {
		this.predicate=predicate;
	}

	public static Matcher wrapPredicate(Predicate<TextLine> predicate) {
		return new SingleLineMatcher(predicate);
	}

	@Override
	public void readLine(TextLine line) {
		if(predicate.test(line)) {
			state=MatchingState.MATCHED;
		}else {
			state=MatchingState.UNMATCHED;
		}
	}

	@Override
	public MatchingState getState() {
		return state;
	}

}
