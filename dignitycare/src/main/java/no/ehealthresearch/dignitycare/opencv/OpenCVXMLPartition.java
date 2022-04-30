package no.ehealthresearch.dignitycare.opencv;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "partitions")
public class OpenCVXMLPartition {
	
	@Override
	public String toString() {
		return "Partitions [segment=" + segment + "]";
	}




	public List<XMLSegment> getSegment() {
		return segment;
	}




	public void setSegment(List<XMLSegment> segments) {
		if(segments!=null) {
			this.segment = segments;
		}
		
	}
	

	


	@JacksonXmlElementWrapper(localName = "segments")
	private List<XMLSegment> segment=new ArrayList<>();
	
	public static class XMLSegment {
		
		public XMLSegment() {
			
		}
		
		public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getY() {
			return y;
		}

		public void setY(double y) {
			this.y = y;
		}

		public double getWidth() {
			return width;
		}

		public void setWidth(double width) {
			this.width = width;
		}

		public double getHeight() {
			return height;
		}

		public void setHeight(double height) {
			this.height = height;
		}

		private double x,y,width,height;

		public XMLSegment(double x, double y, double width, double height) {
			super();
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
		
		
	}
	
	
}



