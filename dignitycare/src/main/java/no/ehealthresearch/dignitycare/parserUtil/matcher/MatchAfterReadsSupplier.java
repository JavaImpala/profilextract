package no.ehealthresearch.dignitycare.parserUtil.matcher;

import java.util.function.Supplier;

import org.apache.commons.lang3.mutable.MutableInt;

public class MatchAfterReadsSupplier implements Supplier<Matcher>{
	
	private MutableInt counter=new MutableInt(0);
	private int maxLines;
	
	private MatchAfterReadsSupplier(int maxLines) {
		this.maxLines = maxLines;
	}

	public static MatchAfterReadsSupplier create(int maxLines) {
		return new MatchAfterReadsSupplier(maxLines);
	}
	
	@Override
	public Matcher get() {
		return UnmatchUntilEnoughReads.create(maxLines,counter);
	}

}
