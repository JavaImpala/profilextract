package no.ehealthresearch.dignitycare.parserUtil.endable;

import no.ehealthresearch.dignitycare.parserUtil.lineParser.LineParser;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;

public class EndableWrapper implements EndableLineParser{
	
	private final LineParser listener;
	private final Runnable onEnded;
	private boolean isEnded=false;
	
	private EndableWrapper(LineParser listener,Runnable onEnded) {
		this.listener = listener;
		this.onEnded=onEnded;
	}
	
	public static EndableWrapper wrap(LineParser listener) {
		return new EndableWrapper(listener,()->{});
	}
	
	public static EndableWrapper wrapWithOnEndedHook(LineParser listener,Runnable onEnded) {
		return new EndableWrapper(listener,onEnded);
	}


	@Override
	public void readLine(TextLine line) {
		listener.readLine(line);
	}

	@Override
	public boolean isEnded() {
		return isEnded;
	}

	@Override
	public void end() {
		isEnded=true;
		onEnded.run();
	}

}
