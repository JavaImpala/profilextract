package no.ehealthresearch.dignitycare.parserUtil.lineParser;

import java.util.LinkedList;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import no.ehealthresearch.dignitycare.parserUtil.endable.EndableLineParser;
import no.ehealthresearch.dignitycare.parserUtil.matcher.Matcher;
import no.ehealthresearch.dignitycare.parserUtil.matcher.MatchingState;

/**
 * OBS. HVA skjer hvis current reader er ended?
 * @author tor003
 *
 */

public class RepeatLineParser implements LineParser{
	private final static Logger log=LogManager.getLogger(RepeatLineParser.class.getSimpleName());
	
	private Supplier<Matcher> shouldRestartSupplier;
	private Supplier<EndableLineParser> readers;
	
	private LinkedList<TextLine> undigested=new LinkedList<>();
	
	private EndableLineParser currentParser=null;
	private Matcher shouldRestart=null;
	
	private RepeatLineParser(Supplier<Matcher> shouldRestartSupplier, Supplier<EndableLineParser> readers) {
		this.shouldRestartSupplier = shouldRestartSupplier;
		this.readers = readers;
		
		this.shouldRestart=this.shouldRestartSupplier.get();
	}
	
	public static RepeatLineParser create(Supplier<Matcher> shouldRestartSupplier, Supplier<EndableLineParser> readers) {
		return new RepeatLineParser(shouldRestartSupplier, readers);
	}
	
	@Override
	public void readLine(TextLine s) {
		log.info(hashCode()+" read line "+s);
		
		undigested.add(s);
		consumeFromQueue(undigested.size()-1);
	}
	
	private void consumeFromQueue(int head) {
		if(currentParser==null) {
			currentParser=this.readers.get();
		}
		
		log.info(hashCode()+" enter consumeFromQueue at "+head+" ("+undigested.size()+"). Current matcher har hashCode():"+shouldRestart.hashCode()+" og state "+shouldRestart.getState());
		
		if(head>=undigested.size()) {
			log.info(hashCode()+" returned fordi vi ikke har nok");
			return;
		}
		
		TextLine s=undigested.get(head);
		
		shouldRestart.readLine(s);
		log.info(hashCode()+" skjekker should restart etter readLine. Matcher med hashCode():"+shouldRestart.hashCode()+" er i state "+shouldRestart.getState());

		if(shouldRestart.getState()==MatchingState.MATCHING) {
			consumeFromQueue(head+1);
		}else if(shouldRestart.getState()==MatchingState.UNMATCHED) {
			currentParser.readLine(undigested.removeFirst());
			log.info(hashCode()+" sender linje til wrapped parser ("+s+")");
			
			shouldRestart=this.shouldRestartSupplier.get();
			log.info(hashCode()+" lager ny matcher med hashCode():"+shouldRestart.hashCode()+". Går tilbake til nytt forsøk på consume from queue på head 0");
			
			consumeFromQueue(0);
		}else if(shouldRestart.getState()==MatchingState.MATCHED ) {
			
			shouldRestart=this.shouldRestartSupplier.get();
			
			log.info(hashCode()+" ending current parser "+currentParser);
			currentParser.end();
			
			log.info(hashCode()+" getting new parser");
			currentParser=this.readers.get();
			
			currentParser.readLine(undigested.removeFirst());
			
			consumeFromQueue(0);
		}
	}
}
