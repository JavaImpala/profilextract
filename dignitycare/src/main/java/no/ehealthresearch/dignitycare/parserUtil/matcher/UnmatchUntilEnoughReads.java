package no.ehealthresearch.dignitycare.parserUtil.matcher;

import org.apache.commons.lang3.mutable.MutableInt;

import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;

public class UnmatchUntilEnoughReads implements Matcher{
	
	private MatchingState state=MatchingState.READY;
	private MutableInt counter;	
	private final int maxLines;
	
	private UnmatchUntilEnoughReads(int linesToRead,MutableInt counter) {
		this.maxLines=linesToRead;
		this.counter=counter;
	}
	

	public static UnmatchUntilEnoughReads create(int linesToRead) {
		return new UnmatchUntilEnoughReads(linesToRead,new MutableInt(0));
	}
	
	public static Matcher create(int maxLines, MutableInt counter) {
		return new UnmatchUntilEnoughReads(maxLines,counter);
	}
	
	public static UnmatchUntilEnoughReads alwaysMatch() {
		return create(0);
	}
	
	@Override
	public void readLine(TextLine line) {
		if(counter.getValue()>=maxLines) {
			state=MatchingState.MATCHED;
		}else {
			state=MatchingState.UNMATCHED;
		}
		
		counter.increment();
		
	}

	@Override
	public MatchingState getState() {
		return state;
	}


	@Override
	public String toString() {
		return "MatchAfterReads [state=" + state + ", counter=" + counter + ", maxLines=" + maxLines + "]";
	}

	


}
