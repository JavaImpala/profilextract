package no.ehealthresearch.dignitycare.parserUtil.lineParser;

public class TextLineWord {
	
	
	public double getFontSize() {
		return fontSize;
	}



	private final double x;
	private final double relY;//relative to line
	private final double y; 
	private final double width;
	private final double height;
	
	private final double fontSize;
	
	private final TextLine parentLine;
	
	private final String word;
	

	private TextLineWord(
			double x,
			double y,
			double relY,
			double width, 
			double height, 
			double fontSize, 
			String word,
			TextLine parentLine) {
		this.x = x;
		this.y = y;
		this.relY=relY;
		this.width = width;
		this.height = height;
		this.fontSize=fontSize;
		this.word=word;
		this.parentLine = parentLine;
	}
	
	public static TextLineWord create(
			double x, 
			double y, 
			double width, 
			double height, 
			String word,
			double fontSize, 
			TextLine parentLine) {
		return new TextLineWord(
				x, 
				y, 
				y-parentLine.getY(),
				width,
				height, 
				fontSize,
				word,
				parentLine);
	}
	
	public static TextLineWord createWithoutParent(
			double x, 
			double y, 
			double width, 
			double height, 
			String word,
			double fontSize) {
		return new TextLineWord(
				x, 
				y, 
				y,
				width,
				height, 
				fontSize,
				word,
				null);
	}	
	
	public double getX() {
		return x;
	}
	
	
	public double getRelY() {
		return relY;
	}

	public double getY() {
		return y;
	}
	public double getWidth() {
		return width;
	}
	public double getHeight() {
		return height;
	}
	public TextLine getParentLine() {
		return parentLine;
	}
	
	public String getWord() {
		return word;
	}
	
	@Override
	public String toString() {
		return "TextLineWord [fontSize=" + fontSize + ", word=" + word + "]";
	}

	
}
