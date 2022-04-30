package no.ehealthresearch.dignitycare.opencv;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * finner og lager bokser!
 * 
 * @author tor003
 *
 */

public class ImageBoxProcessor {
	public static void main(String[] args) {
		//media/tor003/2A9C-E69E/forskninguttrekkABB050321full-psm1/image-600.png;
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		/*
		File folder=new File("/media/tor003/2A9C-E69E/forskninguttrekkABB050321full-psm1/");
	   
		for(File fileEntry:folder.listFiles()) {
			
			TextPreProcessor processor = new TextPreProcessor();
	        //System.out.println(fileEntry.getAbsolutePath());
			processor.proccess(fileEntry.getAbsolutePath());
	        
	        //break;
	        
		}
		*/
		int page=1;
		
		String folder="/media/tor003/2A9C-E69E/forskninguttrekkABB050321full-psm1/";
		
		while(true) {
			System.out.println("============");
			
			String stringPage=String.format("%03d",page);
			
			String configFile=folder+"image-"+stringPage+"-segments.xml";
			
			System.out.println("============ "+configFile);
			
			try {
				createBoxXML(configFile);
			}catch (Exception e) {
				e.printStackTrace();
				break;
			} 
			
			page++;		
		}		
	}
	
	public static void createBoxXML(String imagePath) {
		ObjectMapper objectMapper = new XmlMapper();
			
		OpenCVXMLPartition segments=new OpenCVXMLPartition();
		
		List<OpenCVXMLPartition.XMLSegment> segs=new ArrayList<>();
		
		segments.setSegment(segs);
		
		for(Rectangle2D rect:FindBoxes.proccess(new File(imagePath).getAbsolutePath())) {
			segs.add(new OpenCVXMLPartition.XMLSegment(rect.getMinX(),rect.getMinY(),rect.getWidth(),rect.getHeight()));
		}
	
		try {
			objectMapper.writeValue(new File(imagePath+".segments.xml"),segments);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
