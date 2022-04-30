package no.ehealthresearch.dignitycare.parserUtil.matcher;

import java.util.regex.Pattern;

import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;

public class MatchingUntilRegex implements Matcher{
	
	
	private MatchingState state=MatchingState.READY;
	private final Pattern pattern;
	
	private MatchingUntilRegex(Pattern pattern) {
		this.pattern = pattern;
	}
	
	public static MatchingUntilRegex create(Pattern pattern) {
		return new MatchingUntilRegex(pattern);
	}

	@Override
	public void readLine(TextLine line) {
		if(pattern.matcher(line.getLineConcatString()).matches()) {
			state=MatchingState.MATCHED;
		}else if(state!=MatchingState.MATCHED) {
			state=MatchingState.MATCHING;
		}
	}

	@Override
	public MatchingState getState() {
		return state;
	}

}
