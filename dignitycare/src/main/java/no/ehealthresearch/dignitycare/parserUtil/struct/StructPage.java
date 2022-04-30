package no.ehealthresearch.dignitycare.parserUtil.struct;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import no.ehealthresearch.dignitycare.opencv.OpenCVXMLPartition;
import no.ehealthresearch.dignitycare.parserUtil.RegexTools;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.HocrPartition;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.OpenCVPartition;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLineWord;


public class StructPage {

	private final List<TextLine> lines=new ArrayList<>();
	
	public StructPage(File hocrFile,File segmentFile) {
		try {
			
			/*
			 * parser struct
			 */
			
			ObjectMapper objectMapper = new XmlMapper();
			
			OpenCVXMLPartition partitions=objectMapper.readValue(segmentFile,OpenCVXMLPartition.class);
			
			
			
			List<OpenCVPartition> partitionRectangles=partitions.getSegment()
					.stream()
					.map(s->OpenCVPartition.create(s.getX(),s.getY(),s.getWidth(),s.getHeight()))
					.sorted((a,b)->Double.valueOf(a.getRectangle().getHeight()*a.getRectangle().getWidth()).compareTo((b.getRectangle().getHeight()*b.getRectangle().getWidth())))
					.collect(Collectors.toList());
			
			OpenCVPartition defaultPartition = OpenCVPartition.create(0, 0, 1, 1);
			
			/*
			 * parser hocr
			 */
			Document doc=Jsoup.parse(hocrFile,"UTF-8");
			
			for(Element segment:doc.getElementsByClass("ocr_par")) {
				
				List<String> segmentTitleAttr=Arrays.asList(segment.attr("title").split(";"));
				List<String> segmentBounds = RegexTools.getMatches(segmentTitleAttr.get(0).trim(),"(\\d+)");
				
				HocrPartition hocrPartition = HocrPartition.create(
						Double.valueOf(segmentBounds.get(0)), 
						Double.valueOf(segmentBounds.get(1)), 
						Double.valueOf(segmentBounds.get(2)), 
						Double.valueOf(segmentBounds.get(3)));
				
				for(Element line:segment.children()) {
					
					List<String> lineTitleAttr=Arrays.asList(line.attr("title").split(";"));
					List<String> lineBounds = RegexTools.getMatches(lineTitleAttr.get(0).trim(),"(\\d+)");
					
					double lineY=Double.valueOf(lineBounds.get(1));
					double lineHeight=Double.valueOf(lineBounds.get(3))-lineY;
					
					OpenCVPartition openCVPartition=defaultPartition;
					
					for(OpenCVPartition part:partitionRectangles) {
						if(part.getRectangle().intersects(0, lineY, Double.POSITIVE_INFINITY, lineHeight)) {
							openCVPartition=part;
							break;
						}
					}
					
					TextLine textLine=TextLine.create(hocrPartition,openCVPartition, lineY, lineHeight);
					
					for(Element word:line.children()) {
						if(!word.hasText()) {
							continue;
						}
						
						//System.out.println(word);
						
						List<String> wordTitleAttr=Arrays.asList(word.attr("title").split(";"));
						List<String> wordBounds = RegexTools.getMatches(wordTitleAttr.get(0).trim(),"(\\d+)");
						
						double y=Double.valueOf(wordBounds.get(1));
						double height=Double.valueOf(wordBounds.get(3))-y;
						double x=Double.valueOf(wordBounds.get(0));
						double width=Double.valueOf(wordBounds.get(2))-x;
						
						double fontSize=Double.valueOf(RegexTools.getMatches(word.attr("title"),"(?<=x_fsize\\s)(\\d+)").get(0).trim());
						
						TextLineWord textLineWord=TextLineWord.create(
								x,
								y,
								width,
								height,
								word.text(),
								fontSize,
								textLine);
						
						textLine.addWord(textLineWord);
					}
					
					lines.add(textLine);
				}
			}
		
			Collections.sort(lines,(a,b)->a.getY().compareTo(b.getY()));
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<TextLine> lines(){
		return lines;
	}

}
