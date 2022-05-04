package no.ehealthresearch.dignitycare.profil.fhir;

import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.BundleBuilder;
import no.ehealthresearch.dignitycare.profil.reports.ProfilRawCarePlan;

public class FHIRResources {
	
	private BundleBuilder bundle;
	
	private Patient patient;
	
	private FhirAuthorRepo practitionerFactory;
	private FhirIdFactory<ProfilRawCarePlan> carePlanFactory;
	private FhirIdFactory<String> docIdFactory;
	
	private final Organization org;
	
	public FHIRResources(FhirContext context) {
		this.bundle=new BundleBuilder(context);
		
		org=new Organization();
		org.setName("Tromsø kommune");
		org.setId("Organization/tromsokommune");
		
		bundle.addTransactionCreateEntry(org);
		
		patient=new Patient();
		patient.setId("Patient/hn11079911");
		patient.addName().setFamily("Andersen").addGiven("Anna");
		
		bundle.addTransactionUpdateEntry(patient);
		
		this.practitionerFactory=FhirAuthorRepo.create(org,"tromsokommune",(r)->bundle.addTransactionUpdateEntry(r));
		
		this.carePlanFactory=FhirIdFactory.createCarePlanIdFactory("tromsokommune");
		this.docIdFactory=FhirIdFactory.create("DocumentReference/tromsokommune");
		
		//lager custom search for docrefs->careplan
		
		
	}
	
	public Patient getPatient() {
		return patient;
	}

	public FhirAuthorRepo getPractitionerFactory() {
		return practitionerFactory;
	}
	
	public FhirIdFactory<ProfilRawCarePlan> getCarePlanIdFactory() {
		return carePlanFactory;
	}
	
	public FhirIdFactory<String> getDocIdFactory() {
		return docIdFactory;
	}

	public void addResource(IBaseResource resource) {
		this.bundle.addTransactionUpdateEntry(resource);
	}
	
	public IBaseBundle getBundle() {
		
		return bundle.getBundle();
	}

	public Organization getOrg() {
		return org;
	}

	
	
}
