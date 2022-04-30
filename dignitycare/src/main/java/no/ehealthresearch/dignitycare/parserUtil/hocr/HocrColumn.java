package no.ehealthresearch.dignitycare.parserUtil.hocr;

import java.util.Objects;

public class HocrColumn{
	

	private final Double start,stop;

	public HocrColumn(double start, double stop) {
		this.start = start;
		this.stop = stop;
	}
	
	public double overlap(double beginning,double end) {
		if(beginning>stop || end<start) {
			return 0;
		}
		
		double span=end-beginning;
		double overlap= span-(Double.max(start,beginning)-beginning)-(end-Double.min(end,stop));
		
		return overlap/span;
	}
	
	
	
	public double dist(double beginning) {
		return Math.abs(beginning-start);
	}

	public Double getStart() {
		return start;
	}

	public Double getStop() {
		return stop;
	}

	

	@Override
	public int hashCode() {
		return Objects.hash(start, stop);
	}
}
