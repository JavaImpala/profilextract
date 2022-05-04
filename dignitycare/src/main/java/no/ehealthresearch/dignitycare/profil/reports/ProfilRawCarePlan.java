package no.ehealthresearch.dignitycare.profil.reports;

import java.util.ArrayList;
import java.util.List;

public class ProfilRawCarePlan {
	private ProfilRawCarePlanDescriptors descriptors;
	
	private List<CarePlanDescription> descriptions=new ArrayList<>();

	public ProfilRawCarePlanDescriptors getDescriptors() {
		return descriptors;
	}

	public void setDescriptors(ProfilRawCarePlanDescriptors descriptors) {
		this.descriptors = descriptors;
	}

	public void setDescriptions(List<CarePlanDescription> descriptions) {
		this.descriptions = descriptions;
	}

	public List<CarePlanDescription> getDescriptions() {
		return descriptions;
	}

	public void addDescription(CarePlanDescription description) {
		this.descriptions.add(description);
	}	 
}
