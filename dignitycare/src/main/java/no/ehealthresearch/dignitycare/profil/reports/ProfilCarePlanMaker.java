package no.ehealthresearch.dignitycare.profil.reports;

import java.util.Iterator;
import java.util.function.Consumer;

import no.ehealthresearch.dignitycare.parserUtil.RegexTools;
import no.ehealthresearch.dignitycare.parserUtil.endable.EndableLineParser;
import no.ehealthresearch.dignitycare.parserUtil.endable.EndableWrapper;
import no.ehealthresearch.dignitycare.parserUtil.endable.ObservableEndableLineParser;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;
import no.ehealthresearch.dignitycare.parserUtil.sequence.SequenceLineParsers;
import no.ehealthresearch.dignitycare.parserUtil.sequence.SimpleSequenceLineParser;

public class ProfilCarePlanMaker implements EndableLineParser{

	
	private final ObservableEndableLineParser endableParser;
	
	private ProfilCarePlanMaker(Consumer<ProfilRawCarePlanDescriptors> planConsumer) {
		ProfilRawCarePlanDescriptors plan=new ProfilRawCarePlanDescriptors();
		
		SequenceLineParsers parser=new SequenceLineParsers.Builder()
				.addListener(SimpleSequenceLineParser.listenOnce(EndableWrapper.wrap(l->{
					//Plan/rapport
				})))	
				.addListener(SimpleSequenceLineParser.listenOnce(EndableWrapper.wrap(l->{
					//Plankategori: Helsehjelp
					
					plan.setPlanCategory(RegexTools.getLastWordOfString(l.getLineConcatString()));
				})))	
				.addListener(SimpleSequenceLineParser.listenOnce(EndableWrapper.wrap(l->{
					//Planområde: Kontakt lege/nettverk/pårørend
					plan.setPlanArea(RegexTools.getLastWordOfString(l.getLineConcatString()));
				}))) 	
				.addListener(SimpleSequenceLineParser.listenOnce(EndableWrapper.wrap(l->{
					//Tiltak: Pasientsentrert team 27.04.2018 - 06.11.2019 Skrevet av: Torbjørn Torsvik
					
					
					plan.setAction(RegexTools.getValueAfterAndBefore(l.getLineConcatString(),"Tiltak:\\s","\\s(((3[01]|[12][0-9]|0[1-9]).(1[0-2]|0[1-9]).[0-9]{4}))"));
					
					
					plan.setAuthor(RegexTools.getAllValuesAfter(l.getLineConcatString(),"Skrevet av:"));
					
					Iterator<String> dates=RegexTools.getMatches(l.getLineConcatString(),"[0-9]{2}[.][0-9]{2}[.][0-9]{4}").iterator();
					
					if(dates.hasNext()) {
						plan.setStartDate(dates.next());
					}
					
					if(dates.hasNext()) {
						plan.setEndDate(dates.next());
					}
					
				}))) 	
				
				.build();
		
		endableParser=ObservableEndableLineParser.wrap(parser,()->{
			planConsumer.accept(plan);
		});
		
	}
	
	public static ProfilCarePlanMaker create(Consumer<ProfilRawCarePlanDescriptors> plan) {
		return new ProfilCarePlanMaker(plan);
	}
	
	@Override
	public void readLine(TextLine line) {
		this.endableParser.readLine(line);
	}

	@Override
	public boolean isEnded() {
		return endableParser.isEnded();
	}

	@Override
	public void end() {
		endableParser.end();
	}
}
