package no.ehealthresearch.dignitycare.tesseract;

import static org.bytedeco.leptonica.global.lept.pixDestroy;
import static org.bytedeco.leptonica.global.lept.pixRead;

import java.nio.file.Path;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.tesseract.TessBaseAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.ehealthresearch.dignitycare.FXLauncher;

/**
 * ikke threadsafe!
 * 
 * @author tor003
 *
 */

public class TesseractWrapper implements AutoCloseable{

    private static final Logger LOGGER = LoggerFactory.getLogger(TesseractWrapper.class);

    
    private static final String TESSDATA_DIR = FXLauncher.class.getResource("/data").getPath();
    private static final String LANG = "nor";
    
    private TessBaseAPI api;
    
   
    
    public TesseractWrapper() {
    	LOGGER.info("TessBaseAPI init fint datadir:"+TESSDATA_DIR+" and lang:"+LANG);
    	
    	api=new TessBaseAPI();
    	
    	int returnCode = api.Init(TESSDATA_DIR, LANG);
    	
    	if (returnCode != 0) {
			throw new RuntimeException("could not initialize tesseract, error code: " + returnCode);
		 }
    }

    public String extractHOCRFromImage(Path imagePath) {

		
		
		String hocrString="";
		
	    PIX image = pixRead(imagePath.toFile().getAbsolutePath());

	    LOGGER.info("The image has a width of {} and height of {}", image.w(), image.h());

	    api.SetImage(image);
	    api.SetPageSegMode(1);
	  
	    
	    BytePointer result = api.GetHOCRText(0);
	    hocrString=result.getString();
	    result.deallocate();
	    
	    pixDestroy(image);
		
		return hocrString;

    }
    
    public String extractTextFromImage(Path imagePath,int psm) {

		LOGGER.info("Analyzing image file {"+imagePath.toString()+"} with language {} for psm:"+psm);
		
		String returnString="";
		
	    PIX image = pixRead(imagePath.toFile().getAbsolutePath());

	    LOGGER.info("The image has a width of {} and height of {}", image.w(), image.h());

	    api.SetImage(image);
	    api.SetPageSegMode(psm);
	  
	    BytePointer result = api.GetUTF8Text();
	    returnString=result.getString();
	    result.deallocate();
	    
	   
	    pixDestroy(image);
		
	
		return returnString;
    }

	@Override
	public void close() throws Exception {
		 api.End();
		 api.close();
	}

}