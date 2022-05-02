package no.ehealthresearch.dignitycare.fastlege;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.ehealthresearch.dignitycare.parserUtil.endable.EndableWrapper;
import no.ehealthresearch.dignitycare.parserUtil.endable.ObservableEndableLineParser;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.LineListenerState;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.ObservableLineParser;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.RepeatLineParser;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;
import no.ehealthresearch.dignitycare.parserUtil.matcher.SingleRegexLineMatcher;
import no.ehealthresearch.dignitycare.parserUtil.sequence.SequenceLineParsers;
import no.ehealthresearch.dignitycare.parserUtil.sequence.SimpleSequenceLineParser;

/**
 * 	starter nytt notat på => 20/02/2014 Pedersen, Ville (ville)/VP
 * 
 * 	LOREM IPSUm kontent bippetibappetibopp.
 *
 * @author tor003
 *
 */

public class GPNoteMaker implements ObservableLineParser{
	
	private LineListenerState state=LineListenerState.READY;
	
	private final static String initiateRegex= "^((3[01]|[12][0-9]|0[1-9]).(1[0-2]|0[1-9]).([0-9]{4})).([A-Å][a-å]*)[^A-Å]+([A-Å][a-å]*)[^/]*[/]([A-Å]*).*";
	public final static Pattern initiateRegexPattern=Pattern.compile(initiateRegex);
	
	private RepeatLineParser lineParser;
	
	private GPNoteMaker(Consumer<Object> reportConsumer) {
		
		this.lineParser=RepeatLineParser.create(
				()->SingleRegexLineMatcher.wrapPattern(initiateRegexPattern),
				()->{
					
					return ObservableEndableLineParser.wrap(
							new SequenceLineParsers.Builder()
								.addListener(SimpleSequenceLineParser.listenOnce(EndableWrapper.wrap(line->{
									Matcher m = initiateRegexPattern.matcher(line.getLineConcatString());
									
									int count=0;
									System.out.println();
									
									System.out.println("HAR FUNNET NOTAT");
									System.out.println("------------------------------------");
									
									while (m.find()) {
									   for (int j = 0; j <= m.groupCount(); j++) {
									     
									      System.out.println("Group " + j + ": " + m.group(j));
									      System.out.println("------------------------------------");
									   }
									}
									
									 System.out.println();
									
									
								})))	
								.addListener(SimpleSequenceLineParser.create(EndableWrapper.wrap(line->{
									System.out.println(line.getLineConcatString());
								}), ()->Optional.empty(), ()->Optional.empty())) 	
								.build(),
							()->{
								//on close!
								System.out.println("------------------------------------");
								System.out.println("------------------------------------");
								System.out.println();
							});
				});
		
	}
	
	public static GPNoteMaker create(Consumer<Object> reportConsumer) {
		return new GPNoteMaker(reportConsumer);
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
