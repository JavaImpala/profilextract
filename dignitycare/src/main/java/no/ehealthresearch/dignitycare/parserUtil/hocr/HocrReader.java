package no.ehealthresearch.dignitycare.parserUtil.hocr;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HocrReader {
	
	
	public HocrReader(List<String> lines) {
		Document elements = Jsoup.parse(String.join("",lines));
		
		for(Element element:elements.body().select("*")) {
			element.tag();
		}
	}
	
	
}	
