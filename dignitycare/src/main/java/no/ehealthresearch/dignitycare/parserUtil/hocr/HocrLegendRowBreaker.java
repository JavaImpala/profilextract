package no.ehealthresearch.dignitycare.parserUtil.hocr;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import no.ehealthresearch.dignitycare.parserUtil.hocr.HocrColumnSegments.TextInColumn;

/*
 * max 2 lines
 */

public class HocrLegendRowBreaker implements HocrRowBreaker{

	private static final Set<String> legends=new HashSet<>();
	
	static {
		legends.add("Aktuell problemstilling");
		legends.add("Forløp og behandling");
		legends.add("Årsak til innleggelse");
		legends.add("Type forespørsel");
		legends.add("Spørsmål");
		legends.add("Tema kodet:");
		legends.add("Innhold");
		
		legends.add("Dato for notat");
		legends.add("Melding sendt:");
		legends.add("Meldingsid:");
		legends.add("Samtykke");
		legends.add("Utstedelsestidspunkt");
	}
	
	
	private String legend="";
	
	@Override
	public boolean shoudBreak(List<Optional<TextInColumn>> line) {
		
		
		if(line.get(0).isPresent()) {
			
			if(legend!="") {
				legend=legend+" "+line.get(0).get().getText().getLineConcatString();
				
				if(!legends.contains(legend)) {
					return true;
				}
			}else {
				legend=line.get(0).get().getText().getLineConcatString();
			}
		}
		
		return false;
	}
}
