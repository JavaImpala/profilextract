package no.ehealthresearch.dignitycare.parserUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {

	public static Date parse(String stringDate) {
		try {
			return new SimpleDateFormat("dd.MM.yyyy").parse(stringDate);
		} catch (ParseException e) {
			System.out.println("klarte ikke parser "+stringDate+" returnerer epoch");
			
			return new Date();
		}
	}

}
