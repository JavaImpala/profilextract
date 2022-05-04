package no.ehealthresearch.dignitycare.profil.fhir;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Reference;

public class FhirAuthorRepo {
	private final Map<String,PractitionerRole> practitioner=new HashMap<>();
	private FhirIdFactory<String> practIdFactory;
	private FhirIdFactory<String> roleIdFactory;
	
	private final Organization org;
	
	private final Consumer<IBaseResource> resourceConsumer;
	
	private FhirAuthorRepo(FhirIdFactory<String> practIdFactory,FhirIdFactory<String> roleIdFactory,Organization organization,Consumer<IBaseResource> consumer) {
		
		this.practIdFactory = practIdFactory;
		this.roleIdFactory = roleIdFactory;
		this.resourceConsumer=consumer;
		this.org=organization;
	}
	
	public PractitionerRole get(String name) {
		if(practitioner.get(name)==null) {
			
			String practId=practIdFactory.getId(name);
			
			Practitioner pract=new Practitioner();
			pract.setId(practId);
			
			if(name.contains(" ")) {
				pract.addName(
						new HumanName()
							.addGiven(name.substring(0,name.lastIndexOf(" ")).trim())
							.setFamily(name.substring(name.lastIndexOf(" ")+1).trim()));
			}else {
				pract.addName(
						new HumanName()
							.setFamily(name));
			}
			
			
			resourceConsumer.accept(pract);
			
			String roleId=roleIdFactory.getId(name);
			
			PractitionerRole role=new PractitionerRole();
			role.setId(roleId);
			role.setPractitioner(new Reference(practId));
			role.setOrganization(new Reference(org.getId()));
			
			practitioner.put(name,role);
			
			resourceConsumer.accept(role);
		}
		
		return practitioner.get(name);
	}
	
	public static FhirAuthorRepo create(Organization org,String stem,Consumer<IBaseResource> resourceConsumer) {
		return new FhirAuthorRepo(
				FhirIdFactory.create("Practitioner/tromsokommune"),
				FhirIdFactory.create("PractitionerRole/tromsokommune"),
				org,
				resourceConsumer);
	}
}
