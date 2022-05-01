package no.ehealthresearch.dignitycare.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javafx.util.Pair;

public class SplitPDFIntoImages {
	
	public static void extract(String pathToPDF,String pathToResultFolder,Consumer<String> imageCreated,Consumer<Integer> pageCount) {
    	File file=Paths.get(pathToPDF).toFile();
    	
    	System.out.println("analyserer pdf: "+pathToPDF);
    	
    	try (PDDocument document = PDDocument.load(file)) {
        	
    	    PDFRenderer pdfRenderer = new PDFRenderer(document);
    	    
    	    int counter=0;
    	    
    	    for (int pageNum = 0; pageNum < document.getNumberOfPages(); ++pageNum) {
    	    	
    	    	BufferedImage bim = pdfRenderer.renderImageWithDPI(
    	    	          pageNum, 300, ImageType.GRAY);
    	    	     
    	        File pageImgFile = new File(pathToResultFolder+"/page"+pageNum+".tiff");
    	        
    	        if(pageImgFile.exists()) {
    	        	
    	        	continue;
    	        }
    	        
    	        System.out.println("lagrer bilde "+pageNum+" til "+pageImgFile.getPath());
    	        ImageIO.write(bim, "tiff",pageImgFile);
    	        
    	        counter++;
    	        imageCreated.accept(pageImgFile.getPath());
    	    }
    	    
    	    
    	    
    	    System.out.println("ferdig å omgjøre pdf til bilder. Laget "+counter);
    	    pageCount.accept(counter);
    	    
    	    document.close();
    	    
        }catch(IOException orr) {
        	orr.printStackTrace();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
}
