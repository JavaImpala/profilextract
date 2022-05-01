package no.ehealthresearch.dignitycare.fastlege;

import no.ehealthresearch.dignitycare.parserUtil.pageProcessor.Page;
import no.ehealthresearch.dignitycare.parserUtil.pageProcessor.PageParser;
import no.ehealthresearch.dignitycare.parserUtil.pageProcessor.PageParserManager;

public class GPNoteParserManager implements PageParserManager{

	@Override
	public PageParser getPageParser() {
		return new GPNotePageParser();
	}

	public static GPNoteParserManager create() {
		return new GPNoteParserManager();
	}
	
	private static class GPNotePageParser implements PageParser{
		
		private GPNotePageParser(
				
				) {
			
		}
		
		@Override
		public boolean tryToProccessPage(Page page) {
			System.out.println("forsøker å se på "+page);
			
			for(String line:page.getRawLinesPSM1()) {
				System.out.println("=>"+line);
			}
			
			return true;
		}
		
	}
	
}
