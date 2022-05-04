package no.ehealthresearch.dignitycare.tesseract;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import no.ehealthresearch.dignitycare.FXLauncher;

public enum TessDataDir {
	INSTANCE;
	
	private final String dir;
	
	private TessDataDir() {
		
		String dirPlaceholder="";
		
		try {
			String relPath="/data";
			
			System.out.println("leter etter resource i "+relPath+" kommer til "+URLDecoder.decode(FXLauncher.class.getResource(relPath).getPath(),System.getProperty("file.encoding"))+" ved encoding: "+System.getProperty("file.encoding"));
			
			dirPlaceholder=URLDecoder.decode(FXLauncher.class.getResource(relPath).getPath(),System.getProperty("file.encoding"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		dir=dirPlaceholder;
	}
	
	public String dir() {
		return dir;
	}
}
