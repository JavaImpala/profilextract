package no.ehealthresearch.dignitycare.parserUtil.endable;

import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;

public class ObservableEndableLineParser implements EndableLineParser{

	private final EndableLineParser wrapped;
	private final Runnable onEnded;
	
	private ObservableEndableLineParser(EndableLineParser wrapped, Runnable onEnding) {
		this.wrapped = wrapped;
		this.onEnded = new Runnable() {
			boolean hasRan=false;

			@Override
			public void run() {
				if(hasRan) {
					return;
				}
				hasRan=true;
				
				onEnding.run();
			}
		};
	}
	
	public static ObservableEndableLineParser wrap(EndableLineParser wrapped, Runnable onEnded) {
		return new ObservableEndableLineParser(wrapped, onEnded);
	}
	
	@Override
	public void readLine(TextLine line) {
		wrapped.readLine(line);
		
		if(wrapped.isEnded()) {
			onEnded.run();
		}
	}

	@Override
	public boolean isEnded() {
		return wrapped.isEnded();
	}

	@Override
	public void end() {
		wrapped.end();
		onEnded.run();
	}

	@Override
	public String toString() {
		return "ObservableEndableLineParser [wrapped=" + wrapped.hashCode() + ", hashCode()=" + hashCode() + "]";
	}
	
	

}
