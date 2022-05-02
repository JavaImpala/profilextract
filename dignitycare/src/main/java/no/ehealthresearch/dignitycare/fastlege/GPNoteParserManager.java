package no.ehealthresearch.dignitycare.fastlege;

import java.util.Optional;

import no.ehealthresearch.dignitycare.parserUtil.endable.EndableWrapper;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;
import no.ehealthresearch.dignitycare.parserUtil.pageProcessor.Page;
import no.ehealthresearch.dignitycare.parserUtil.pageProcessor.PageParser;
import no.ehealthresearch.dignitycare.parserUtil.pageProcessor.PageParserManager;
import no.ehealthresearch.dignitycare.parserUtil.sequence.SequenceLineParsers;
import no.ehealthresearch.dignitycare.parserUtil.sequence.SimpleSequenceLineParser;

public class GPNoteParserManager implements PageParserManager{

	@Override
	public PageParser getPageParser() {
		return new GPNotePageParser();
	}

	public static GPNoteParserManager create() {
		return new GPNoteParserManager();
	}
	
	
	
	private static class GPNotePageParser implements PageParser{
		
		private final SequenceLineParsers reader;
		
		private GPNotePageParser(
				
				) {
			
			GPNoteMaker reportMaker=GPNoteMaker.create(o->{
				
			});
			
			reader=new SequenceLineParsers.Builder()
					.addListener(
						SimpleSequenceLineParser.create(
							EndableWrapper.wrapWithOnEndedHook(
								l->{
									reportMaker.readLine(l);
								},
								()->{
									//on ending reading reports
									
								}),
							()->Optional.empty(),
							()->Optional.empty()
						))
					.build();
		}
		
		@Override
		public boolean tryToProccessPage(Page page) {
			System.out.println("=======================");
			System.out.println("forsøker å se på "+page);
			
			for(TextLine line:page.getStructPage().lines()) {
				reader.readLine(line);
			}
			
			return true;
		}
		
	}
	
}
