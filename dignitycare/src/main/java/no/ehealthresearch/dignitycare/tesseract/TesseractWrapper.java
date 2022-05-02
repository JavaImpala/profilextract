package no.ehealthresearch.dignitycare.tesseract;

import static org.bytedeco.leptonica.global.lept.pixDestroy;
import static org.bytedeco.leptonica.global.lept.pixRead;

import java.nio.file.Path;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.tesseract.TessBaseAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ikke threadsafe!
 * 
 * @author tor003
 *
 */

public class TesseractWrapper implements AutoCloseable{

    private static final Logger LOGGER = LoggerFactory.getLogger(TesseractWrapper.class);
    
    
    
    private static final String LANG = "nor";
    private TessBaseAPI api;
    
    public TesseractWrapper() {
    	
    	api=new TessBaseAPI();
    	
    	int returnCode = api.Init(TessDataDir.INSTANCE.dir(), LANG);
    	
    	api.SetVariable("hocr_font_info","1");
    	
    	if (returnCode != 0) {
			throw new RuntimeException("could not initialize tesseract, error code: " + returnCode);
		}
    }

    public String extractHOCRFromImage(String path) {

		String hocrString="";
		
	    PIX image = pixRead(path);

	    LOGGER.info("The image has a width of {} and height of {}", image.w(), image.h());

	    api.SetImage(image);
	    api.SetPageSegMode(1);
	    
	    BytePointer result = api.GetHOCRText(0);
	    hocrString=result.getString();
	   
	    result.deallocate();
	    pixDestroy(image);
		
		return hocrString;
    }
    
    public String extractTextFromImage(String imagePath,int psm) {

		LOGGER.info("Analyzing image file {"+imagePath.toString()+"} with language {} for psm:"+psm);
		
		String returnString="";
		
	    PIX image = pixRead(imagePath);

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