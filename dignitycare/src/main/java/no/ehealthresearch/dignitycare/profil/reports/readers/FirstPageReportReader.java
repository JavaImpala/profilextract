package no.ehealthresearch.dignitycare.profil.reports.readers;

import java.util.Iterator;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import no.ehealthresearch.dignitycare.parserUtil.endable.EndableLineParser;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;
import no.ehealthresearch.dignitycare.parserUtil.matcher.Matcher;
import no.ehealthresearch.dignitycare.parserUtil.matcher.MatchingState;
import no.ehealthresearch.dignitycare.parserUtil.pageProcessor.Page;
import no.ehealthresearch.dignitycare.parserUtil.pageProcessor.PageParser;
import no.ehealthresearch.dignitycare.profil.reports.ProfilReportMaker;

public class FirstPageReportReader implements PageParser{
	private final static Logger log=LogManager.getLogger(FirstPageReportReader.class.getSimpleName());
	
	private Optional<ProfilReportMaker> currentReport;

	private final EndableLineParser parser;

	private FirstPageReportReader(EndableLineParser contentParser) {
		this.parser=contentParser;
	}
	
	public static FirstPageReportReader create(EndableLineParser contentParser) {
		return new FirstPageReportReader(contentParser);
	}

	@Override
	public boolean tryToProccessPage(Page page) {
		
		//validate that its a reportPage
		
		Iterator<TextLine> matchIterator=page.getStructPage().lines().iterator();
		Matcher validator =  ReportStartMatcher.startMatcher.get();
		
		while(matchIterator.hasNext()) {
			
			TextLine next=matchIterator.next();
			
			//System.out.println("validating "+next);
			
			validator.readLine(next);
			
			if(validator.getState()==MatchingState.MATCHED) {
				break;
			}
			
			if(validator.getState()==MatchingState.UNMATCHED) {
				//System.out.println("first page har IKKE match");
				return false;
			}
		}
		
		if(validator.getState()==MatchingState.READY || validator.getState()==MatchingState.MATCHING) {
			return false;
		}
		
		
		log.info("first page har match "+validator.getState());
		
		
		
		//parse
		if(validator.getState()==MatchingState.MATCHED) {
			Iterator<TextLine> readIterator=page.getStructPage().lines().iterator();
			
			while(readIterator.hasNext()) {
				
				TextLine line =readIterator.next();
				
				if(!parser.isEnded()) {
					log.info("reads line "+line);
					parser.readLine(line);
				}else {
					log.info("reader is ended!");
					return false;
				}
			}
		}
		
		
		
		return true;
	}

	public Optional<ProfilReportMaker> rollOverReport(){
		return currentReport;
	}
}
