package no.ehealthresearch.dignitycare.tesseract;

import no.ehealthresearch.dignitycare.GlobalShutdown;

public enum TessDataDir {
	INSTANCE;
	
	private final String dir;
	
	private TessDataDir() {
		
		String dirPlaceholder="";
		
		
		String relPath="./data";
		
		System.out.println("leter etter resource i "+relPath+" kommer til "+GlobalShutdown.class.getResource(relPath).getPath());
		
		//dirPlaceholder=GlobalShutdown.class.getResource(relPath).getPath();
		
		
		dir=dirPlaceholder;
	}
	
	public String dir() {
		return dir;
	}
}
