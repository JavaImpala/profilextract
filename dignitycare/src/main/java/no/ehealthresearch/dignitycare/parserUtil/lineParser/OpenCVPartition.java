package no.ehealthresearch.dignitycare.parserUtil.lineParser;

import java.awt.geom.Rectangle2D;

public class OpenCVPartition {
	public Rectangle2D getRectangle() {
		return rectangle;
	}

	private final Rectangle2D rectangle;

	private OpenCVPartition(double x, double y, double width, double height) {
		this.rectangle=new Rectangle2D.Double(x, y, width, height);
		
	}
	
	public static OpenCVPartition create(double x1, double y1, double width, double height) {
		return new OpenCVPartition(x1, y1,width,height);
	}

	@Override
	public String toString() {
		return "OpenCVPartition [rectangle=" + rectangle + "]";
	}
	
	
	
	
}
