package no.ehealthresearch.dignitycare.profil.fhir;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import no.ehealthresearch.dignitycare.profil.reports.ProfilRawCarePlan;

public class FhirIdFactory<E>{
	private Map<Object,String> ids=new HashMap<>();
	
	private final Function<E,String> stringFactory;
	private final Function<E,Object> keyMaker;
	
	private FhirIdFactory(Function<E,String> stringFactory,Function<E,Object> keyMaker) {
		
		this.stringFactory=stringFactory;
		this.keyMaker=keyMaker;
	}
	
	public String getId(E substrate) {
		return ids.computeIfAbsent(
				keyMaker.apply(substrate),
				n->{
					return stringFactory.apply(substrate);
				});
	}

	public static FhirIdFactory<String> create(String stem) {
		return new FhirIdFactory<String>(
				new Function<String,String>() {
					private int idCounter=0;
					@Override
					public String apply(String arg0) {
						idCounter++;
						
						return stem+idCounter;
					}
				},
				e->e);
	}
	
	public static FhirIdFactory<ProfilRawCarePlan> createCarePlanIdFactory(String stem) {
		return new FhirIdFactory<ProfilRawCarePlan>(
				new Function<ProfilRawCarePlan,String>() {
					private int idCounter=0;
					@Override
					public String apply(ProfilRawCarePlan arg0) {
						idCounter++;
						
						return "CarePlan/"+stem+idCounter;
					}
				},
				e->e);
	}
}	
