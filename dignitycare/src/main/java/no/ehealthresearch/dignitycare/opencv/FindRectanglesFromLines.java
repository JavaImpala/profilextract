package no.ehealthresearch.dignitycare.opencv;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FindRectanglesFromLines {
	
	
	public static List<Rectangle2D> get(List<Line2D> horisontalLines,List<Line2D> verticalLines){
		
		//sorting lines
		
		Collections.sort(horisontalLines,(a,b)->Double.valueOf(a.getY1()).compareTo(b.getY1()));
		Collections.sort(verticalLines,(a,b)->Double.valueOf(a.getX1()).compareTo(b.getX1()));
		
		List<Rectangle2D> rectangles=new ArrayList<>();
		
		for(int h=0;h<horisontalLines.size();h++) {
			
			
			List<Rectangle2D> localRectangles=new ArrayList<>();
			
			Line2D topHorisontalLine=horisontalLines.get(h);
			
			for(int ho=h+1;ho<horisontalLines.size();ho++) {
				
				
				Line2D bottomHorisontalLine=horisontalLines.get(ho);
				
				Line2D prevVerticalLine=null;
				
				o:
				for(int v=0;v<verticalLines.size();v++) {
					Line2D verticalLine=verticalLines.get(v);
					
					if(verticalLine.intersectsLine(topHorisontalLine) && verticalLine.intersectsLine(bottomHorisontalLine)) {
						
						if(prevVerticalLine!=null) {
							Rectangle2D rectCandidate = new Rectangle2D.Double(
									prevVerticalLine.getX1(),
									topHorisontalLine.getY1(),
									verticalLine.getX1()-prevVerticalLine.getX1(),
									bottomHorisontalLine.getY1()-topHorisontalLine.getY1());
							
							for(Rectangle2D localRect:localRectangles) {
								//hvis overlap med eksisterende i local
								if(!(rectCandidate.getMaxX()<localRect.getMinX() || rectCandidate.getMinX()>localRect.getMaxX())) {
									continue o;
								}
							}
							
							localRectangles.add(rectCandidate);
						}
						
						prevVerticalLine=verticalLine;
					}
					
				}
			}
			
			
			rectangles.addAll(localRectangles);
		}
		
		return rectangles;
	}
	
	
	
	
	
}
