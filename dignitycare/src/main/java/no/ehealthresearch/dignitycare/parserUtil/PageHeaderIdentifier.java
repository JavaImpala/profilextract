package no.ehealthresearch.dignitycare.parserUtil;

import java.util.function.Supplier;
import java.util.regex.Pattern;

import no.ehealthresearch.dignitycare.parserUtil.matcher.ChainMatch;
import no.ehealthresearch.dignitycare.parserUtil.matcher.Matcher;
import no.ehealthresearch.dignitycare.parserUtil.matcher.MatchingUntilRegex;

/**
 * prosseserer sider i profil pdf. Luker ut header og sender resten til sub makers. 
 * 
 * @author tor003
 *
 *		TROMSØ Kommune 05.03.2021 13:41:10
		Rapport kjørt av: Torbjørn Torsvik Utskrift av pasientjournal Side 11 / 356
		Bruker: Kalle Krank
		Fødselsdato / Persnr: 30.10.1987 / 56144
 *
 */

public class PageHeaderIdentifier{
	private static final String lastHeaderLine="^Fødselsdato.*Persnr:.*((3[01]|[12][0-9]|0[1-9]).(1[0-2]|0[1-9]).[0-9]{4}).*/.*[0-9]{5}.*";
	public static final Pattern lastHeaderLinePattern=Pattern.compile(lastHeaderLine);
	
	public static final Supplier<Matcher> matcher=()->new ChainMatch.Builder()
			.addMatcher(MatchingUntilRegex.create(lastHeaderLinePattern))
			.build();
	
}
