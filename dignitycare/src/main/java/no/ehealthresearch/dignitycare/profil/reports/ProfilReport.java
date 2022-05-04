package no.ehealthresearch.dignitycare.profil.reports;

public class ProfilReport{
	private int reportNumber=-1;
	
	private String stringDate="01.01.1970";
	private String writtenStringDate="01.01.1970";
	private String daytimeReportType="Ukjent";
	
	private boolean changedPlan=false;
	private boolean changedStatus=false;
	
	private boolean priority=false;
	
	private String author="Ukjent";
	
	private StringBuilder contentBuilder=new StringBuilder();
	private String content="Ikke fylt ut";

	private boolean closed=false;
	
	public int getReportNumber() {
		return reportNumber;
	}

	public void setReportNumber(int reportNumber) {
		this.reportNumber = reportNumber;
	}

	public String getStringDate() {
		return stringDate;
	}

	public void setStringDate(String stringDate) {
		this.stringDate = stringDate;
	}

	public String getWrittenStringDate() {
		return writtenStringDate;
	}

	public void setWrittenStringDate(String writtenStringDate) {
		this.writtenStringDate = writtenStringDate;
	}

	public String getDaytimeReportType() {
		return daytimeReportType;
	}

	public void setDaytimeReportType(String daytimeReportType) {
		this.daytimeReportType = daytimeReportType;
	}

	public void setChangedStatus(boolean statusChanged) {
		this.changedStatus = statusChanged;
	}
	
	

	public boolean isPriority() {
		return priority;
	}

	public void setPriority(boolean priority) {
		this.priority = priority;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void addContent(String string) {
		this.contentBuilder.append(string);
	}
	
	public boolean isChangePlan() {
		return changedPlan;
	}

	public void setChangedPlan(boolean changePlan) {
		this.changedPlan = changePlan;
	}

	public boolean isClosed(){
		return closed;
	}
	public void close() {
		this.closed=true;
		this.content=contentBuilder.toString();
	}

	@Override
	public String toString() {
		return "ProfilReport [reportNumber=" + reportNumber + ", stringDate=" + stringDate + ", writtenStringDate="
				+ writtenStringDate + ", daytimeReportType=" + daytimeReportType + ", statusChanged=" + changedStatus
				+ ", priority=" + priority + ", author=" + author + ", content="
				+ content + "]";
	}

	
}
