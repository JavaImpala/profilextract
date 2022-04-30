package no.ehealthresearch.dignitycare;

public enum GlobalShutdown {
	INSTANCE;
	
	private boolean isShutDown=false;

	public boolean isShutDown() {
		return isShutDown;
	}

	public void shutDown() {
		this.isShutDown = false;
	}
	
	
}
