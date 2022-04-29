package no.ehealthresearch.dignitycare.tesseract;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.util.Pair;
import no.ehealthresearch.dignitycare.grunt.DiligentWorkTicket;
import no.ehealthresearch.dignitycare.grunt.SimpleDiligentGrunt;

public class ImageToText {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageToText.class);
	
    public static void extract(String path) {
    	File file=Paths.get(path).toFile();
    	System.out.println();
    	System.out.println("analyserer pdf: "+path);
    	
    	try (PDDocument document = PDDocument.load(file)) {
        	
    	    PDFRenderer pdfRenderer = new PDFRenderer(document);
    	    
    	    String dirName=file.getParent()+"/"+file.getName()+"-"+System.currentTimeMillis();
    	    
    	    System.out.println();
    	    System.out.println("===");
    	    System.out.println("resultat legges i:"+dirName);
    	    System.out.println("deler opp pdf i "+document.getNumberOfPages()+" bilder");
    	    System.out.println("===");
    	    System.out.println();
    	    
    	    new File(dirName).mkdirs();
    	    
    	    SimpleDiligentGrunt grunt=SimpleDiligentGrunt.create();
    	    
    	    for (int pageNum = 0; pageNum < document.getNumberOfPages(); ++pageNum) {
    	    	
    	    	BufferedImage bim = pdfRenderer.renderImageWithDPI(
    	    	          pageNum, 300, ImageType.RGB);
    	    	     
    	        File pageImgFile = new File(dirName+"/page"+pageNum+".png");
    	        
    	      
    	        System.out.println("lagrer bilde "+pageNum+" til "+pageImgFile.getPath());
    	        ImageIO.write(bim, "png",pageImgFile);
    	        
    	        /*
    	         * tesseract
    	         */
    	        
    	    	grunt.setToWork(
    	    		DiligentWorkTicket.create(
    	    			new Pair<Integer,Path>(pageNum,pageImgFile.toPath()), 
    	    			e->{
    	    				int page=e.getKey();
    	    				int pageVisNum=page+1;
    	    				
    	    				Path imgPath=e.getValue();
    	    				
    	    				TesseractWrapper tess=new TesseractWrapper();
    	    				
    	    				
    	    				System.out.println("analyserer bilde "+pageVisNum);
    	    				
    	    				try (PrintWriter out = new PrintWriter(dirName+"/page"+page+"horc.hocr")) {
	    	    	        	System.out.println("analyserer bilde "+pageVisNum+" => ekstraherer hocr til: page"+page+"horc.hocr");
	    	    	            out.println(tess.extractHOCRFromImage(imgPath));
	    	    	        } catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
	    	    	        
	    	    	        try (PrintWriter out = new PrintWriter(dirName+"/page"+page+"ocr6.txt")) {
	    	    	        	System.out.println("analyserer bilde "+pageVisNum+" => ekstraherer ocr psm6 tekst til: page"+page+"ocr6.txt");
	    	    	            out.println(tess.extractTextFromImage(imgPath,6));
	    	    	        } catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
	    	    	        
	    	    	        try (PrintWriter out = new PrintWriter(dirName+"/page"+page+"ocr1.txt")) {
	    	    	        	System.out.println("analyserer bilde "+pageVisNum+" => ekstraherer ocr psm1 tekst til: page"+page+"ocr1.txt");
	    	    	            out.println(tess.extractTextFromImage(imgPath,1));
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
	    	    	        
    	    				return Optional.empty();
    	    			}, 
    	    			e->{
    	    				
    	    			}));
    	    	
    	        
    	        
    	        
    	        
    	       
    	    }
    	    
    	    document.close();
    	    
    	    

        }catch(IOException orr) {
        	orr.printStackTrace();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
