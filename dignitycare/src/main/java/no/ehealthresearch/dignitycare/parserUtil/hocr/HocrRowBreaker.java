package no.ehealthresearch.dignitycare.parserUtil.hocr;
import java.util.List;
import java.util.Optional;

import no.ehealthresearch.dignitycare.parserUtil.hocr.HocrColumnSegments.TextInColumn;

public interface HocrRowBreaker {
	
	public boolean shoudBreak(List<Optional<TextInColumn>> line);
	
}
