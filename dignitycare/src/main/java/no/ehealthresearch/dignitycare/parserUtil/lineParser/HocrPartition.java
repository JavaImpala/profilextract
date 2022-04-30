package no.ehealthresearch.dignitycare.parserUtil.lineParser;

import java.awt.geom.Rectangle2D;

public class HocrPartition {
	public Rectangle2D getRectangle() {
		return rectangle;
	}

	private final Rectangle2D rectangle;

	private HocrPartition(double x, double y, double width, double height) {
		this.rectangle=new Rectangle2D.Double(x, y, width, height);
		
	}
	
	public static HocrPartition create(double x1, double y1, double x2, double y2) {
		return new HocrPartition(x1, y1,x2-x1,y2-y1);
	}
	
	
}
