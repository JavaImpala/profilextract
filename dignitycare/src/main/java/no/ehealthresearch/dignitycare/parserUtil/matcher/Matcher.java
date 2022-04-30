package no.ehealthresearch.dignitycare.parserUtil.matcher;

import no.ehealthresearch.dignitycare.parserUtil.lineParser.LineParser;

public interface Matcher extends LineParser{
	public MatchingState getState();
}
