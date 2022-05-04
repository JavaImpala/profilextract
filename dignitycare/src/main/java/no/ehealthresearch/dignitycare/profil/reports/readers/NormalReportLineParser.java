package no.ehealthresearch.dignitycare.profil.reports.readers;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import no.ehealthresearch.dignitycare.parserUtil.endable.EndableLineParser;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;
import no.ehealthresearch.dignitycare.parserUtil.matcher.Matcher;
import no.ehealthresearch.dignitycare.parserUtil.matcher.MatchingState;
import no.ehealthresearch.dignitycare.parserUtil.pageProcessor.Page;
import no.ehealthresearch.dignitycare.parserUtil.pageProcessor.PageParser;

		/*
		 * 
			for(Entry<Predicate<String>,Supplier<ObservableLineParser>> entry:pageParsers.entrySet()) {
				if(entry.getKey().test(line)) {
					if(activeMaker.getValue()!=null) {
						
						activeMaker.getValue().settle();
						
						System.out.println(activeMaker.getValue());
						
						noteCounter++;
					}
					
					ObservableLineParser newMaker = entry.getValue().get();
					
					newMaker.setHappyListener(()->{
						activeMaker.setValue(null);
					});
					
					activeMaker.setValue(newMaker);
					
					break;
				}
			}
		 */

public class NormalReportLineParser implements PageParser{
	private final static Logger log=LogManager.getLogger(NormalReportLineParser.class.getSimpleName());
	
	private final EndableLineParser contentParser;
	
	public static  NormalReportLineParser create(EndableLineParser contentParser) {
		return new  NormalReportLineParser(contentParser);
	}

	private NormalReportLineParser(EndableLineParser contentParser) {
		log.info("is created!");
		this.contentParser=contentParser;
	}

	@Override
	public boolean tryToProccessPage(Page page) {
		
		//validate that its a reportPage
		
		Iterator<TextLine> matchIterator=page.getStructPage().lines().iterator();
		Matcher validator = ReportStartMatcher.startMatcher.get();
		
		while(matchIterator.hasNext()) {
			
			
			validator.readLine(matchIterator.next());
			
			if(validator.getState()==MatchingState.MATCHED) {
				break;
			}
			
			if(validator.getState()==MatchingState.UNMATCHED) {
				log.info("is UNvalidated");
				return false;
			}
		}
		log.info("is validated");
		//System.out.println("subsequent page har match");
		//parse
		
		if(validator.getState()==MatchingState.MATCHED) {
			Iterator<TextLine> readIterator=matchIterator;
			
			while(readIterator.hasNext()) {
				
				TextLine line =readIterator.next();
				
				if(!contentParser.isEnded()) {
					log.info("reads line "+line);
					contentParser.readLine(line);
				}else {
					log.info("reader is ended!");
					return false;
				}
				
			}
		}
		
		log.info("has read page successfully");
		
		return true;
	}
}
