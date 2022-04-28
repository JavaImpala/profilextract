package pdf;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import profilextract.TesseractJavaDemo;

public class Launcher {
	public static void main(String[] args) {
		System.out.println("??? ");
	
		
		
        try (PDDocument document = PDDocument.load(Paths.get(Launcher.class.getResource("/pdf/samplepdf.pdf").toURI()).toFile())) {
        	
    	    PDFRenderer pdfRenderer = new PDFRenderer(document);
    	    
    	    String dirName=System.currentTimeMillis()+"";
    	    
    	    new File(dirName).mkdirs();
    	    
    	    for (int page = 0; page < document.getNumberOfPages(); ++page) {
    	        BufferedImage bim = pdfRenderer.renderImageWithDPI(
    	          page, 300, ImageType.RGB);
    	        
    	        System.out.println("skriver fil "+"page"+page);
    	        
    	        File pageImgFile = new File(dirName+"/page"+page);
    	        
    	        ImageIO.write(bim, "png",pageImgFile);
    	        
    	        /*
    	         * tesseract
    	         */
    	        
    	        try (PrintWriter out = new PrintWriter(dirName+"/page"+page+"horc.hocr")) {
    	            out.println( TesseractJavaDemo.extractHOCR("nor",pageImgFile.toPath()));
    	        }
    	        
    	        try (PrintWriter out = new PrintWriter(dirName+"/page"+page+"ocr6.txt")) {
    	            out.println( TesseractJavaDemo.extractText("nor",pageImgFile.toPath(),6));
    	        }
    	        
    	        try (PrintWriter out = new PrintWriter(dirName+"/page"+page+"ocr1.txt")) {
    	            out.println( TesseractJavaDemo.extractText("nor",pageImgFile.toPath(),1));
    	        }
    	        
    	       
    	    }
    	    document.close();

        }catch(IOException orr) {
        	orr.printStackTrace();
        } catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}
