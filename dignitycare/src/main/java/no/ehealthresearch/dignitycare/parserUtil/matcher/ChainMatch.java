package no.ehealthresearch.dignitycare.parserUtil.matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;


public class ChainMatch implements Matcher{
	private final static Logger log=LogManager.getLogger(ChainMatch.class.getSimpleName());
	
	private Consumer<TextLine> lineListener=s->{};
	
	private MatchingState state=MatchingState.READY;
	
	private ChainMatch(List<Matcher> patterns) {
		
		MutableInt i=new MutableInt(0);
		
		
		lineListener=s->{
			
			
			
			Matcher matcher=patterns.get(i.getValue());
			
			matcher.readLine(s);
			
			log.info("on index:"+i.getValue()+" of:"+(patterns.size()-1)+" "+s+" "+matcher);
			
			//System.out.println(s+" "+pattern.hashCode()+" "+i.getValue()+" "+pattern.getState());
			
			if(matcher.getState()==MatchingState.MATCHED) {
				i.increment();
				
				if(i.getValue()>=patterns.size()) {
					
					state=MatchingState.MATCHED;
					lineListener=(o)->{};
				}else {
					state=MatchingState.MATCHING;
				}
				
			}else if(matcher.getState()==MatchingState.UNMATCHED){
				state=MatchingState.UNMATCHED;
				lineListener=(o)->{};
			}else {
				state=MatchingState.MATCHING;
			}
		};
	}
	
	
	public MatchingState getState() {
		return this.state;
	}

	@Override
	public void readLine(TextLine line) {
		lineListener.accept(line);
	}
	
	public static class Builder{
		private final List<Matcher> patterns=new ArrayList<>();
		
		public Builder() {
			
		}
		
		public Builder addSingleLinePattern(Pattern pattern) {
			this.patterns.add(SingleRegexLineMatcher.wrapPattern(pattern));return this;
		}
		public Builder addMatcher(Matcher matcher) {
			this.patterns.add(matcher);return this;
		}
		
		public ChainMatch build() {
			return new ChainMatch(this.patterns);
		}

		
	}

	@Override
	public String toString() {
		return "ChainMatch [state=" + state + ", hashCode()=" + hashCode() + "]";
	}

	
	
	
	
}
