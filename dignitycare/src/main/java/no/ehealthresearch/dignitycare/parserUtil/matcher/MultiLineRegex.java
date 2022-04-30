package no.ehealthresearch.dignitycare.parserUtil.matcher;
import java.util.regex.Pattern;

import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;

public class MultiLineRegex implements Matcher{
	
	private final Pattern pattern;
	private StringBuilder stringBuilder=new StringBuilder();
	
	private final int maxLines;
	private int lineCounter=0;
	
	private MatchingState state=MatchingState.READY;
	
	public MultiLineRegex(Pattern pattern, int maxLines) {
		this.pattern = pattern;
		this.maxLines = maxLines;
	}

	@Override
	public void readLine(TextLine line) {
		if(state==MatchingState.MATCHED || state==MatchingState.UNMATCHED) {
			return;
		}
		
		this.stringBuilder.append(line);
		
		if(this.pattern.matcher(stringBuilder.toString()).matches()){
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
	
}
