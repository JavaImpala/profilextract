package no.ehealthresearch.dignitycare.parserUtil.hocr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import no.ehealthresearch.dignitycare.parserUtil.hocr.Jenks.Breaks;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLineWord;

public class HocrColumnSegments {
	
	private final List<TextLine> srcLines=new ArrayList<>();
	private static final double defaultWordDistBreak=40d; //21 målt på 300 dpi

	public HocrColumnSegments() {
		
	}

	public void addLine(TextLine line) {
		srcLines.add(line);
	}
	
	public HocrColumnTextLines getSegmentedLines(int columnCount){
		return getSegmentedLines(columnCount,defaultWordDistBreak);
	}
	
	public HocrColumnTextLines getSegmentedLines(int columnCount,double wordDistanceBreak){
		double startX=-(defaultWordDistBreak+1);
		
		TextLineWord dummyStartWord = TextLineWord.createWithoutParent(startX, 0, 0,0,"dummyStartWord", 10);
		
		List<Double> breaks=new ArrayList<>();
		breaks.add(startX);
		
		Jenks jenks=new Jenks();	
		
		//finner kolonneinndeling
		for(TextLine line:srcLines) {
			
			List<TextLineWord> words=new ArrayList<>();
			
			words.add(dummyStartWord);
			words.addAll(line.getWords());
			
			for(int i=1;i<words.size();i++) {
				
				TextLineWord prevWord = words.get(i-1);
				TextLineWord currentWord = words.get(i);
				
				if(currentWord.getX()>(prevWord.getX()+prevWord.getWidth()+wordDistanceBreak)) {
					jenks.addValue(currentWord.getX());
				}
			}
		}
		
		Breaks clusteredBreaks=jenks.computeBreaks(columnCount);
		
		List<HocrColumn> columns=new ArrayList<>();
		
		System.out.println(clusteredBreaks.numClassses()+" "+columnCount);
		
		if(clusteredBreaks.numClassses()>1) {
			for(int b=1;b<clusteredBreaks.numClassses();b++) {
				columns.add(
						new HocrColumn(
								clusteredBreaks.getClassMin(b-1),
								clusteredBreaks.getClassMin(b)));
			}
			
			columns.add(
					new HocrColumn(
							clusteredBreaks.getClassMin(clusteredBreaks.numClassses()-1),
							Double.POSITIVE_INFINITY));
		}else {
			columns.add(
				new HocrColumn(0,Double.POSITIVE_INFINITY));
		}
		
		
		
		
		
		List<HocrColumnTextLine> outputLines=new ArrayList<>();
		
		//sorterer ord til kolonner
		for(TextLine line:srcLines) {
			
			HocrColumnTextLine columnTextLine=new HocrColumnTextLine(line,columns);
			
			List<TextLineWord> words=line.getWords();
			
			for(int i=0;i<words.size();i++) {
				
				TextLineWord currentWord = words.get(i);
				
				HocrColumn current=columns.get(0);
				double match=Double.POSITIVE_INFINITY;
				
				for(HocrColumn column:columns) {
					
					double candOverlap=column.overlap(currentWord.getX(),currentWord.getX()+currentWord.getWidth());
					
					if(candOverlap>0) {
						
						double dist = column.dist(currentWord.getX());
						
						if(dist<match) {
							current=column;
							match=dist;
						}
						
						
					}
				}
				
				columnTextLine.addWord(current, currentWord);
			}
			
			outputLines.add(columnTextLine);
		}
		
		
		
		return new HocrColumnTextLines(columns, outputLines);
	}
	
	
	public static class HocrColumnTextLines{
		private final List<HocrColumn> columns;
		private final List<HocrColumnTextLine> lines;
		
		public HocrColumnTextLines(List<HocrColumn> columns, List<HocrColumnTextLine> lines) {
			this.columns = columns;
			this.lines = lines;
			
			Collections.sort(columns, (a,b)->a.getStart().compareTo(b.getStart()));
		}

		public List<HocrColumn> getColumns() {
			return columns;
		}

		public List<HocrColumnTextLine> getLines() {
			return lines;
		}
		

		@Override
		public String toString() {
			return "HocrColumnTextLines [columns=" + columns + ", lines=" + lines + "]";
		}
		
		
	}
	
	public static class HocrColumnTextLine{
		private final Map<HocrColumn,List<TextLineWord>> words=new LinkedHashMap<>();
		private final TextLine parentLine;
		private final List<HocrColumn> columns;
		
		private List<Optional<TextInColumn>> output;
		
		public HocrColumnTextLine(TextLine parentLine,List<HocrColumn> columns) {
			this.parentLine=parentLine;
			this.columns=columns;
		}
		
		public void addWord(HocrColumn column,TextLineWord word) {
			words.computeIfAbsent(column,c->new ArrayList<>()).add(word);
			output=null;
		}
		
		public List<Optional<TextInColumn>> getColumns() {
			
			if(output==null) {
				
				output=new ArrayList<>();
				
				for(HocrColumn c:columns) {
					
					if(words.containsKey(c)) {
						TextLine line=TextLine.create(parentLine.getHocrPartition(), parentLine.getOpenCVPart(),parentLine.getY(),parentLine.getHeight());
						words.get(c).forEach(e->line.addWord(e));
						
						output.add(Optional.of(new TextInColumn(line,c)));
						
					}else {
						output.add(Optional.empty());
					}
				}
			}
			
			
			return output;
		}

		@Override
		public String toString() {
			return "HocrColumnTextLine [words=" + words + "]";
		}
	}
	
	public static class TextInColumn{
		
		private final TextLine text;
		private final HocrColumn column;
		
		public TextInColumn(TextLine text, HocrColumn column) {
			this.text = text;
			this.column = column;
		}
		
		public TextLine getText() {
			return text;
		}
		public HocrColumn getColumn() {
			return column;
		}

		@Override
		public String toString() {
			return "TextInColumn [text=" + text + ", column=" + column + "]";
		}
		
		
		
	}
}
