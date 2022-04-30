package no.ehealthresearch.dignitycare.parserUtil.pageProcessor;

import java.util.List;

import no.ehealthresearch.dignitycare.parserUtil.struct.StructPage;

public class Page {
	private final List<String> rawLinesPSM1;
	private final List<String> rawLinesPSM6;
	private final StructPage structPage;
	
	public Page(List<String> rawLinesPSM1, List<String> rawLinesPSM6, StructPage structPage) {
		this.rawLinesPSM1 = rawLinesPSM1;
		this.rawLinesPSM6 = rawLinesPSM6;
		this.structPage = structPage;
	}
	public List<String> getRawLinesPSM1() {
		return rawLinesPSM1;
	}
	public List<String> getRawLinesPSM6() {
		return rawLinesPSM6;
	}
	public StructPage getStructPage() {
		return structPage;
	}
	
	
}
