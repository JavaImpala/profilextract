package no.ehealthresearch.dignitycare.parserUtil.sequence;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import no.ehealthresearch.dignitycare.parserUtil.endable.EndableLineParser;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;
import no.ehealthresearch.dignitycare.parserUtil.matcher.Matcher;
import no.ehealthresearch.dignitycare.parserUtil.matcher.MatchingState;

public class SequenceLineParsers implements EndableLineParser{
	private final static Logger log=LogManager.getLogger(SequenceLineParsers.class.getSimpleName());
	
	private final EndableLineParser lineListener;
	
	private SequenceLineParsers(List<SequenceLineParser> makers) {
		
		log.info(hashCode()+" ********************************** lager ny SequenceLineParsers "+hashCode());
		
		//MutableObject<String> prevLine=new MutableObject<>("");
		MutableInt currentIndex=new MutableInt(0);
		
		lineListener=new EndableLineParser() {
			
			private boolean ended=false;
			
			private LinkedList<TextLine> undigested=new LinkedList<>();
			private LineEater currentEater;
			
			private LineEater getEater(TextLine food) {
				log.info(hashCode()+" lager ny eater basert på index "+currentIndex.getValue()+" "+makers.size());
				
				if(currentIndex.getValue()<makers.size()-1) {
					
					return new LineEater(
							makers.get(currentIndex.getValue()+1).getNewShouldStart(),//should next start
							makers.get(currentIndex.getValue()).getNewShouldEnd(),
							makers.get(currentIndex.getValue()));
				}else if(currentIndex.getValue()<makers.size()) {
					
					return new LineEater(
							Optional.empty(),//no next
							makers.get(currentIndex.getValue()).getNewShouldEnd(),
							makers.get(currentIndex.getValue()));
				}else {
					throw new IllegalArgumentException("vi er ended"); 
				}
			}
			
			
			
			@Override
			public void readLine(TextLine s) {
				undigested.add(s);
				
				log.info(hashCode()+" consuming after eatLine "+undigested.size());
				
				if(currentEater==null) {
					consumeFood(0);
				}else {
					consumeFood(undigested.size()-1);
				}
				
			}
			
			private void consumeFood(int head) {
				log.info(hashCode()+" consuming food med head:"+head);
				
				if(head>=undigested.size()) {
					log.info(hashCode()+" returned fordi vi ikke har nok");
					return;
				}
				
				TextLine s=undigested.get(head);
				
				log.info(hashCode()+" vi har nok "+s.getLineConcatString());
				
				if(currentEater==null) {
					if(head!=0) {
						throw new IllegalArgumentException("det skjer noe jeg ikke forstår"); 
					}
					
					currentEater=getEater(s);
				}
				
				currentEater.digest(s);
				
				log.info(hashCode()+" forsøker å spise "+s+" "+head+" "+currentEater.end+" "+currentEater.start+" "+currentEater.state);
				
				if(currentEater.state==LineEaterState.EATEN) {
					log.info(hashCode()+" =>SPISER "+s);
					
					currentEater.eater.readLine(undigested.removeFirst());
					
					
					if(currentEater.eater.isEnded()) {
						currentIndex.increment();
						currentEater.eater.end();
						currentEater=null;
						
						log.info(hashCode()+" FORDI EATER ER ENDED GÅR VI TIL NESTE!=> "+currentIndex.intValue()+"/"+makers.size()+" ");
						
						if(currentIndex.getValue()>=makers.size()) {
							log.info(hashCode()+" ended");
							ended=true;
							
						}else {
							consumeFood(0);
						}
					}else {
						currentEater=null;
						
						consumeFood(0);
					}
					
					
				}else if(currentEater.state==LineEaterState.EATING) {
					log.info(hashCode()+" =>Forsøker å spise "+s);
					consumeFood(head+1);
				}else if(currentEater.state==LineEaterState.GOTONEXT) {
					
					currentIndex.increment();
					currentEater.eater.end();
					currentEater=null;
					
					log.info(hashCode()+" VI GÅR TIL NESTE!=> "+currentIndex.intValue()+"/"+makers.size()+" "+undigested.get(0));
					
					if(currentIndex.getValue()>=makers.size()) {
						log.info(hashCode()+" ended");
						ended=true;
					}else {
						consumeFood(0);
					}
					
					
				}
			}

			@Override
			public boolean isEnded() {
				return ended;
			}

			@Override
			public void end() {
				if(ended!=true) {
					ended=true;
					makers.get(currentIndex.getValue()).end();
				}
			}
		};
	}
	
	@Override
	public void readLine(TextLine line) {
		lineListener.readLine(line);
	}
	
	@Override
	public boolean isEnded() {
		return lineListener.isEnded();
	}

	@Override
	public void end() {
		lineListener.end();
	}
	
	public static class Builder{
		private final List<SequenceLineParser> makers=new ArrayList<>();
		
		public Builder() {
			
		}
		
		public Builder addListener(SequenceLineParser maker) {
			this.makers.add(maker);return this;
		}
		
		public SequenceLineParsers build() {
			return new SequenceLineParsers(this.makers);
		}
	}
	
	private static class LineEater{
		
		private Optional<Matcher> start;
		private Optional<Matcher> end;
		
		private LineEaterState state=LineEaterState.EATING;	
		
		private final EndableLineParser eater;
		
		private LineEater(
				Optional<Matcher> start,
				Optional<Matcher> end,
				EndableLineParser eater) {
			
			this.start=start;
			this.end=end;
			
			this.eater=eater;
		}
		
		private void updateState() {
			if(start.isPresent() ) {
				if(start.get().getState()==MatchingState.MATCHED) {
					state=LineEaterState.GOTONEXT;
				}else if(start.get().getState()==MatchingState.UNMATCHED) {
					start=Optional.empty();
				}
			}
			
			if(end.isPresent() ) {
				if(end.get().getState()==MatchingState.MATCHED) {
					state=LineEaterState.GOTONEXT;
				}else if(end.get().getState()==MatchingState.UNMATCHED) {
					end=Optional.empty();
				}
			}
			
			if(state==LineEaterState.GOTONEXT) {
				return;
			}
			
			if(end.isEmpty() && start.isEmpty()) {
				state=LineEaterState.EATEN;
			}else {
				state=LineEaterState.EATING;
			}
			
		}
		
		private LineEaterState digest(TextLine line) {
			end.ifPresent(m->{m.readLine(line);});
			start.ifPresent(m->m.readLine(line));
			
			updateState();
			return state;
		}
	}
	
	private enum LineEaterState{
		EATING,EATEN,GOTONEXT;
	}
	
}
