package no.ehealthresearch.dignitycare.parserUtil.sequence;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import no.ehealthresearch.dignitycare.parserUtil.endable.EndableLineParser;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;
import no.ehealthresearch.dignitycare.parserUtil.matcher.Matcher;
import no.ehealthresearch.dignitycare.parserUtil.matcher.MatchingState;

public class ListenFrom implements SequenceLineParser{
	
	
	private final EndableLineParser listener;
	
	private final Matcher shouldStart;
	
	private final int steps;
	private final List<TextLine> unparsed=new ArrayList<>();
	
	private ListenFrom(EndableLineParser listener,Matcher shouldStart,int steps) {
		if(steps>0) {
			throw new IllegalArgumentException("kan bare se bakover, steps må være mindre enn 0");
		}
		
		this.listener=listener;
		this.shouldStart=shouldStart;
		this.steps=steps;
	}
	
	public static SequenceLineParser listenFrom(EndableLineParser listener,Matcher shouldStart,int steps) {
		return new ListenFrom(listener,shouldStart,steps);
	}


	@Override
	public void readLine(TextLine line) {
		if(shouldStart.getState()!=MatchingState.MATCHED) {
			
			shouldStart.readLine(line);
			unparsed.add(line);
			
			if(shouldStart.getState()==MatchingState.MATCHED) {
				if(steps<0) {
					for(int i=Integer.max(unparsed.size()-1+steps,0);i<unparsed.size();i++) {
						listener.readLine(unparsed.get(i));
					}
				}
			}
			
		}else {
			listener.readLine(line);
		}
		
		
	}

	

	@Override
	public boolean isEnded() {
		return listener.isEnded();
	}

	@Override
	public void end() {
		listener.end();
	}

	@Override
	public Optional<Matcher> getNewShouldStart() {
		return Optional.empty();
	}

	@Override
	public Optional<Matcher> getNewShouldEnd() {
		return Optional.empty();
	}
}