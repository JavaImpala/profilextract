package no.ehealthresearch.dignitycare.parserUtil.matcher;
import java.util.Optional;
import java.util.regex.Pattern;

import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;
import no.ehealthresearch.dignitycare.parserUtil.sequence.SequenceLineParser;

public class ListenUntilMatchedOrUnmatched implements SequenceLineParser{
	

	private final Matcher matcher;
	
	private Optional<Matcher> endMatcher=Optional.empty();
	private boolean ended=false;
	
	private ListenUntilMatchedOrUnmatched(Matcher matcher) {
		this.matcher=matcher;
	}
	
	public static ListenUntilMatchedOrUnmatched create(Matcher matcher) {
		return new ListenUntilMatchedOrUnmatched(matcher);
	}
	
	public static Matcher create(Pattern pattern) {
		return SingleRegexLineMatcher.wrapPattern(pattern);
	}
	
	@Override
	public void readLine(TextLine line) {
		matcher.readLine(line);
		
		if(matcher.getState()==MatchingState.MATCHED || matcher.getState()==MatchingState.UNMATCHED) {
			ended=true;
			
			endMatcher=Optional.of(UnmatchUntilEnoughReads.alwaysMatch());
		}
	}

	@Override
	public boolean isEnded() {
		return ended;
	}

	@Override
	public void end() {
		ended=true;
	}

	@Override
	public Optional<Matcher> getNewShouldStart() {
		return Optional.empty();
	}

	@Override
	public Optional<Matcher> getNewShouldEnd() {
		return endMatcher;
	}

	
	
}
