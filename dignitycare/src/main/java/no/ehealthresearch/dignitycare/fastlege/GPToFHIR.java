package no.ehealthresearch.dignitycare.fastlege;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.ehealthresearch.dignitycare.parserUtil.pageProcessor.Page;
import no.ehealthresearch.dignitycare.parserUtil.pageProcessor.PageParserManager;
import no.ehealthresearch.dignitycare.parserUtil.struct.StructPage;
import no.ehealthresearch.dignitycare.tesseract.ImageToText;



public class GPToFHIR {
	private static final Logger log = LoggerFactory.getLogger(ImageToText.class);
	private static final String pagePrefix="";
	
	public static void extract(String location) {
		
		List<Supplier<PageParserManager>> pageParsers=new ArrayList<>();
		pageParsers.add(()->GPNoteParserManager.create());
		
		//lager reportMaker
		MutableObject<Optional<PageParserManager>> currentManager=new MutableObject<>(Optional.empty());	
		MutableInt counter=new MutableInt();
		
		new PageLineSupplier(
				location,
				pageLineIteratorSupplier->{
					
					counter.increment();
					
					log.info("");
					log.info("mottar side "+counter.getValue());
					System.out.println("mottar side "+counter.getValue());
					
					if(currentManager.getValue().isPresent()) {
						log.info("leser side i current pageSupplier "+currentManager.getValue().get().getClass());
						
						boolean success=currentManager
								.getValue()
								.get()
								.getPageParser()
								.tryToProccessPage(pageLineIteratorSupplier);
						
						if(success) {
							log.info("klarte det, går til neste");
							//if success, skip to next page
							return;
						}else {
							log.info("klarte det ikke");
							currentManager.setValue(Optional.empty());
						}
						
					}
						
					//if not sees if anybody else can
					//loops through pageparsers if one accept, keep it
					for(Supplier<PageParserManager> pageParser:pageParsers) {
						PageParserManager instance=pageParser.get();
						
						log.info("forsøker å lese side med med ny pageparsermanager "+instance.getClass());
						
						boolean success=instance
								.getPageParser()
								.tryToProccessPage(pageLineIteratorSupplier);
						
						if(success) {
							log.info("klarer det!");
							currentManager.setValue(Optional.of(instance));
							return;
						}else {
							log.info("klarer det ikke");
						}
					}
					
					log.info("klarte ikke å lese side");
				});
		
		
	}
	
	private static class PageLineSupplier{
		
		
		private PageLineSupplier(String location,Consumer<Page> listener) {
			
			int pageNumber=0;
			
			while(true) {
				try {
					
					listener.accept(
							new Page(
									fileToTextLines(location+"/page"+pageNumber+"-proc.tiff.ocr1.txt"),
									fileToTextLines(location+"/page"+pageNumber+"-proc.tiff.ocr6.txt"),
									new StructPage(
											new File(location+"/page"+pageNumber+"-proc.tiff.hocr"),
											new File(location+"/page"+pageNumber+"-proc.tiff.segments.xml"))));
					pageNumber++;
					
					
				}catch(Exception e) {
					
					System.out.println("");
					System.out.println("===================");
					System.out.println("===================");
					System.out.println("");
					System.out.println("finner ingen flere filer å undersøke");
					//e.printStackTrace(System.out);
					break;
					//e.printStackTrace();
				}
			}
			
		}
		
		private List<String> fileToTextLines(String path) throws Exception{
			FileReader reader=new FileReader(path);
			List<String> lines=new BufferedReader(reader).lines().collect(Collectors.toList());
		
			reader.close();
			
			return lines;
		}
	}

}
