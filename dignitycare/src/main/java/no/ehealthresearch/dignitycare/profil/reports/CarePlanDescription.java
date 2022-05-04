package no.ehealthresearch.dignitycare.profil.reports;

public class CarePlanDescription {
	private String author="";
	private String fromDate="";
	private String toDate="";
	
	private StringBuilder content;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getContent() {
		return content.toString();
	}

	public void addContentLine(String content) {
		this.content.append(content);
	}
}
