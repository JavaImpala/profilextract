package no.ehealthresearch.dignitycare.tesseract;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Optional;

import org.apache.commons.lang3.mutable.MutableInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.util.Pair;
import no.ehealthresearch.dignitycare.grunt.DiligentWorkTicket;
import no.ehealthresearch.dignitycare.grunt.SimpleDiligentGrunt;

public class ImageToText {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageToText.class);
	
	SimpleDiligentGrunt grunt=SimpleDiligentGrunt.create();
	private int pageCounter=0;
	
    public void extract(String imagePath,Runnable finished) {
    	
    	grunt.setToWork(
    		DiligentWorkTicket.create(
    			new Pair<String,Integer>(imagePath,pageCounter++), 
    			pair->{
    				String e=pair.getKey();
    				int pageVisNum=pair.getValue();
    				
    				TesseractWrapper tess=new TesseractWrapper();
    				
    				System.out.println("analyserer bilde "+e);
    				
    				try (PrintWriter out = new PrintWriter(e+".hocr")) {
	    	        	System.out.println("analyserer bilde "+pageVisNum+" => ekstraherer hocr til: "+e+".hocr");
	    	            out.println(tess.extractHOCRFromImage(e));
	    	        } catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    	        
	    	        try (PrintWriter out = new PrintWriter(e+".ocr6.txt")) {
	    	        	System.out.println("analyserer bilde "+pageVisNum+" => ekstraherer ocr psm6 tekst til: "+e+".ocr6.txt");
	    	            out.println(tess.extractTextFromImage(e,6));
	    	        } catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    	        
	    	        try (PrintWriter out = new PrintWriter(e+".ocr1.txt")) {
	    	        	System.out.println("analyserer bilde "+pageVisNum+" => ekstraherer ocr psm1 tekst til: "+e+"ocr1.txt");
	    	            out.println(tess.extractTextFromImage(e,1));
	    	        } catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
    	    	    
	    	        try {
						tess.close();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    	        
    				return Optional.ofNullable(e);
    			}, 
    			e->{
    				System.out.println("ferdig å analysere bilde. "+e);
    				finished.run();
    				
    			}));
    }
    
}
