package no.ehealthresearch.dignitycare.profil.reports;

public class ProfilRawCarePlanDescriptors{
	private String planCategory="";
	private String planArea="";
	private String action="";
	private String startDate="";
	private String endDate="";
	
	private String author;
	
	public String getPlanCategory() {
		return planCategory;
	}
	
	/**
	 * Plankategori: Helsehjelp
	 * @param planCategory
	 */
	
	public void setPlanCategory(String planCategory) {
		this.planCategory = planCategory;
	}
	
	/**
	 * Planområde: Kontakt lege/nettverk/pårørend
	 * @param planCategory
	 */
	
	public String getPlanArea() {
		return planArea;
	}

	public void setPlanArea(String planArea) {
		this.planArea = planArea;
	}

	public String getAction() {
		return action;
	}

	/**
	 * Tiltak:
	 * 
	 * @param action
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * [0-9]{2}[.][0-9]{2}[.][0-9]{4}
	 * @return
	 */
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * [0-9]{2}[.][0-9]{2}[.][0-9]{4}
	 * @return
	 */
	
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}
