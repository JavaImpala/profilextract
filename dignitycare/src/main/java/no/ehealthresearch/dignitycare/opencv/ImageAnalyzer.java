package no.ehealthresearch.dignitycare.opencv;

import java.io.File;
import java.util.function.Consumer;

import javafx.util.Pair;
import no.ehealthresearch.dignitycare.grunt.DiligentWorkTicket;
import no.ehealthresearch.dignitycare.grunt.SimpleDiligentGrunt;

/**
 * image preprocess før ocr. leter etter bokser og lagrer resultat i egen xml fil
 * @author tor003
 *
 */

public class ImageAnalyzer {
	private final SimpleDiligentGrunt grunt=SimpleDiligentGrunt.create();
	
	public void analyzeImage(String pathToImage,Consumer<String> writtenPreproccessedImage) {
		TextPreProcessor imageProc=new TextPreProcessor ();
		
		grunt.setToWork(
	    		DiligentWorkTicket.create(
	    			pathToImage, 
	    			e->{
	    				String preproccessed=imageProc.proccess(pathToImage);
	    				System.out.println("saving "+preproccessed);
	    				
	    	    	    ImageBoxProcessor.createBoxXML(preproccessed);
    	    	        
	    				return preproccessed;
	    			}, 
	    			e->{
	    				e.ifPresent(s->writtenPreproccessedImage.accept(
    						s));
	    			}));
	}
}
