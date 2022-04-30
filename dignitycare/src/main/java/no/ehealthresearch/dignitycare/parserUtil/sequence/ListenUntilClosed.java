package no.ehealthresearch.dignitycare.parserUtil.sequence;

import java.util.Optional;

import no.ehealthresearch.dignitycare.parserUtil.endable.EndableLineParser;
import no.ehealthresearch.dignitycare.parserUtil.endable.EndableWrapper;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.LineParser;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;
import no.ehealthresearch.dignitycare.parserUtil.matcher.Matcher;
import no.ehealthresearch.dignitycare.parserUtil.matcher.UnmatchUntilEnoughReads;

public class ListenUntilClosed implements SequenceLineParser{
	private Optional<Matcher> shouldEnd=Optional.empty();
	private EndableLineParser wrapped;
	
	private ListenUntilClosed(EndableLineParser wrapped) {
		this.wrapped=wrapped;
	}
	
	public static ListenUntilClosed wrap(EndableLineParser wrapped) {
		return new ListenUntilClosed(wrapped);
	}
	
	public static SequenceLineParser wrap(LineParser wrapped) {
		return new ListenUntilClosed(EndableWrapper.wrap(wrapped));
	}
	
	public static SequenceLineParser create() {
		return new ListenUntilClosed(EndableWrapper.wrap(s->{}));
	}
	
	@Override
	public void readLine(TextLine line) {
		wrapped.readLine(line);
		shouldEnd=Optional.of(UnmatchUntilEnoughReads.alwaysMatch());
	}

	@Override
	public boolean isEnded() {
		return wrapped.isEnded();
	}

	@Override
	public void end() {
		wrapped.end();
	}

	@Override
	public Optional<Matcher> getNewShouldStart() {
		return Optional.empty();
	}

	@Override
	public Optional<Matcher> getNewShouldEnd() {
		return this.shouldEnd;
	}
	
	
}
