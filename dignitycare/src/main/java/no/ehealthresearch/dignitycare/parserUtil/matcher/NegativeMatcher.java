package no.ehealthresearch.dignitycare.parserUtil.matcher;

import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;

public class NegativeMatcher implements Matcher {

	private final Matcher wrapped;
	
	private NegativeMatcher(Matcher wrapped) {
		this.wrapped = wrapped;
	}
	
	public static NegativeMatcher wrap(Matcher wrapped) {
		return new NegativeMatcher(wrapped);
	}
	
	@Override
	public void readLine(TextLine line) {
		wrapped.readLine(line);
	}

	@Override
	public MatchingState getState() {
		MatchingState origState=wrapped.getState();
		
		if(origState==MatchingState.MATCHED) {
			return MatchingState.UNMATCHED;
		}else if(origState==MatchingState.UNMATCHED) {
			return MatchingState.MATCHED;
		}else {
			return origState;
		}
		
		
	}

}
