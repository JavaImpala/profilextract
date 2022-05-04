package no.ehealthresearch.dignitycare.profil.reports;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.ehealthresearch.dignitycare.parserUtil.RegexTools;
import no.ehealthresearch.dignitycare.parserUtil.endable.EndableWrapper;
import no.ehealthresearch.dignitycare.parserUtil.endable.ObservableEndableLineParser;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.LineListenerState;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.ObservableLineParser;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.RepeatLineParser;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;
import no.ehealthresearch.dignitycare.parserUtil.matcher.SingleRegexLineMatcher;
import no.ehealthresearch.dignitycare.parserUtil.sequence.SequenceLineParsers;
import no.ehealthresearch.dignitycare.parserUtil.sequence.SimpleSequenceLineParser;

public class ProfilReportMaker implements ObservableLineParser{
	
	private LineListenerState state=LineListenerState.READY;
	
	private final static String initiateRegex= "^([0-9]+).+((3[01]|[12][0-9]|0[1-9]).(1[0-2]|0[1-9]).[0-9]{4}).+Skrevet av:(.+)Rapport.+Rapport dato:.+((3[01]|[12][0-9]|0[1-9]).(1[0-2]|0[1-9]).[0-9]{4}).*";
	public final static Pattern initiateRegexPattern=Pattern.compile(initiateRegex);
	
	private RepeatLineParser lineParser;
	
	private ProfilReportMaker(Consumer<ProfilReport> reportConsumer) {
		
		this.lineParser=RepeatLineParser.create(
				()->SingleRegexLineMatcher.wrapPattern(initiateRegexPattern),
				()->{
					ProfilReport report=new ProfilReport();
					
					return ObservableEndableLineParser.wrap(
							new SequenceLineParsers.Builder()
								.addListener(SimpleSequenceLineParser.listenOnce(EndableWrapper.wrap(line->{
									Matcher matcher = initiateRegexPattern.matcher(line.getLineConcatString());
									
									if(matcher.find()){	
										report.setReportNumber(Integer.valueOf(matcher.group(1)));
										report.setAuthor(matcher.group(5).trim());
										report.setStringDate(matcher.group(2));
										report.setWrittenStringDate(matcher.group(6));
									}
									
								})))	
								.addListener(SimpleSequenceLineParser.listenOnce(EndableWrapper.wrap(line->{
									report.setDaytimeReportType(RegexTools.getValueAfter(line.getLineConcatString(),"Vakt:"));
									
									report.setChangedStatus((RegexTools.getValueAfter(line.getLineConcatString(),"Status:")=="Uendret")?false:true);
									report.setChangedPlan((RegexTools.getValueAfter(line.getLineConcatString(),"Endre tiltak:")=="Nei")?false:true);
									
									report.setPriority((RegexTools.getValueAfter(line.getLineConcatString(),"Prioritet gitt:")=="Nei")?false:true);
								})))	
								.addListener(SimpleSequenceLineParser.create(EndableWrapper.wrap(line->{
									report.addContent(line.getLineConcatString());
								}), ()->Optional.empty(), ()->Optional.empty())) 	
								.build(),
							()->{
								report.close();
								
								reportConsumer.accept(report);
								
								
							});
				});
		
	}
	
	
	
	public static ProfilReportMaker create(Consumer<ProfilReport> reportConsumer) {
		return new ProfilReportMaker(reportConsumer);
	}
	
	@Override
	public void readLine(TextLine line) {
		this.lineParser.readLine(line);
	}
	
	@Override
	public LineListenerState getState() {
		return state;
	}
}
