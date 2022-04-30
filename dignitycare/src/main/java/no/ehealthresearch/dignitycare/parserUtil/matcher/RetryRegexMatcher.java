package no.ehealthresearch.dignitycare.parserUtil.matcher;

import java.util.regex.Pattern;

import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;

public class RetryRegexMatcher implements Matcher{
	
	

	private final Pattern pattern;
	
	private final int maxLines;
	private int lineCounter=0;
	
	private MatchingState state=MatchingState.READY;
	
	private RetryRegexMatcher(Pattern pattern, int maxLines) {
		this.pattern = pattern;
		this.maxLines = maxLines;
	}
	
	public static RetryRegexMatcher create(Pattern pattern, int maxLines) {
		return new RetryRegexMatcher(pattern, maxLines);
	}

	@Override
	public void readLine(TextLine line) {
		if(state==MatchingState.MATCHED || state==MatchingState.UNMATCHED) {
			return;
		}
		
		if(this.pattern.matcher(line.getLineConcatString()).matches()){
			state=MatchingState.MATCHED;
		}else if(lineCounter<maxLines){
			state=MatchingState.MATCHING;
		}else {
			state=MatchingState.UNMATCHED;
		}
		
		lineCounter++;
	}

	@Override
	public MatchingState getState() {
		return state;
	}

	@Override
	public String toString() {
		return "RetryMatcher [pattern=" + pattern.pattern() + ", maxLines=" + maxLines + ", lineCounter=" + lineCounter
				+ ", state=" + state + "]";
	}
	
	
	
}
