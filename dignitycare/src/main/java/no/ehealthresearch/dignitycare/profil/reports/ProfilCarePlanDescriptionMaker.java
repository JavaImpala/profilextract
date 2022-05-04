package no.ehealthresearch.dignitycare.profil.reports;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import no.ehealthresearch.dignitycare.parserUtil.endable.EndableLineParser;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.LineParser;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.RepeatLineParser;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;
import no.ehealthresearch.dignitycare.parserUtil.matcher.ChainMatch;
import no.ehealthresearch.dignitycare.parserUtil.matcher.Matcher;
import no.ehealthresearch.dignitycare.parserUtil.matcher.RetryRegexMatcher;

/*
Endret Av: Torbjørn Torsvik
Fra - Til: 03.06.2018 - 03.06.2018
Lorem ipsum
Endret Av: Torbjørn Torsvik
Fra - Til: 30.04.2018 - 02.06.2018
Bakgrunn for tiltaket:
Lorem Ipsum
Endret dato: 03.06.2018
Lorem Ipsum
Rapporter: 281
==>reportHeader
 */



public class ProfilCarePlanDescriptionMaker implements LineParser{

	public static Supplier<Matcher> endDescriptions=()->{
		return new ChainMatch
					.Builder()
					.addSingleLinePattern(Pattern.compile("^Rapporter:\\s+\\d+"))
					.addMatcher(RetryRegexMatcher.create(ProfilReportMaker.initiateRegexPattern,20))
					.build();
	};
	
	public static Supplier<Matcher> newEntryMatcher=()->{
		return new ChainMatch
				.Builder()
				.addSingleLinePattern(Pattern.compile("^Endret\\sAv:\\s+.*"))
				.addSingleLinePattern(Pattern.compile("Fra\\s[-]\\sTil:\\s([0-9]{2}[.][0-9]{2}[.][0-9]{4}).*([0-9]{2}[.][0-9]{2}[.][0-9]{4})"))
				.build();
	};
	
	private final LineParser lineParser;
	
	private ProfilCarePlanDescriptionMaker(Consumer<Map<String,String>> consumer) {
		
		this.lineParser=RepeatLineParser.create(
				newEntryMatcher,
				()->{
					
					return new EndableLineParser() {

						@Override
						public void readLine(TextLine line) {
							//System.out.println("DESCMAKER "+line.getLineConcatString());
						}

						@Override
						public boolean isEnded() {
							// TODO Auto-generated method stub
							return false;
						}

						@Override
						public void end() {
							// TODO Auto-generated method stub
							
						}
					};	
				});
	}
	
	public static ProfilCarePlanDescriptionMaker create(Consumer<Map<String,String>> consumer) {
		return new ProfilCarePlanDescriptionMaker(consumer);
	}

	@Override
	public void readLine(TextLine line) {
		this.lineParser.readLine(line);
	}

	
	
	
	
	
}
