package no.ehealthresearch.dignitycare.parserUtil.sequence;

import java.util.Optional;

import no.ehealthresearch.dignitycare.parserUtil.endable.EndableLineParser;
import no.ehealthresearch.dignitycare.parserUtil.matcher.Matcher;

public interface SequenceLineParser extends EndableLineParser{
	public Optional<Matcher> getNewShouldStart();
	public Optional<Matcher> getNewShouldEnd();
	
}
