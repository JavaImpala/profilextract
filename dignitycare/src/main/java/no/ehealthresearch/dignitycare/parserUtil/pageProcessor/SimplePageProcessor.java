package no.ehealthresearch.dignitycare.parserUtil.pageProcessor;

import java.util.Iterator;

import no.ehealthresearch.dignitycare.parserUtil.endable.EndableLineParser;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;

public class SimplePageProcessor implements PageParser{
	
	private final EndableLineParser parser;
	
	public SimplePageProcessor(EndableLineParser parser) {
		this.parser = parser;
	}

	@Override
	public boolean tryToProccessPage(Page page) {
		
		Iterator<TextLine> parseIterator = page.getStructPage().lines().iterator();
		
		while(parseIterator.hasNext()) {
			parser.readLine(parseIterator.next());
			
			if(parser.isEnded()) {
				return false;
			}
		}
		
		return true;
	}
}
