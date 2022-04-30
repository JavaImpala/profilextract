package no.ehealthresearch.dignitycare.parserUtil.pageProcessor;

public interface PageParser {
	public boolean tryToProccessPage(Page page);
	
	//public void setSettledListener(Runnable onSettled);
	//public void forceToSettle() ;
}
