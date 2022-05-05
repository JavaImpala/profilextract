package no.ehealthresearch.dignitycare.tesseract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public enum TessDataDir {
	INSTANCE;
	
	private final String dir;
	
	private TessDataDir() {
		
		String dirPlaceholder="";
		
		try {
			File tempDir = Files.createTempDirectory("tessdata_"+System.currentTimeMillis()).toFile();
			dirPlaceholder=tempDir.getAbsolutePath();
			
			tempDir.deleteOnExit();
			
			System.out.println("lagrer tessdata i temp "+dirPlaceholder);
			
			saveTemp(dirPlaceholder,"/data/nor.traineddata","nor.traineddata");
			saveTemp(dirPlaceholder,"/data/osd.traineddata","osd.traineddata");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dir=dirPlaceholder;
	}
	
	private void saveTemp(String dir,String relPath,String fileName) throws IOException{
		InputStream input = getClass().getResourceAsStream(relPath);
		
		File file = new File(dir, fileName);
		
        OutputStream out = new FileOutputStream(file);
        int read;
        byte[] bytes = new byte[1024];

        while ((read = input.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.close();
        file.deleteOnExit();
	}
	
	
	
	public String dir() {
		return dir;
	}
	
	
}
