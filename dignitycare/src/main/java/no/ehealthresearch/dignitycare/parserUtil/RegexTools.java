package no.ehealthresearch.dignitycare.parserUtil;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RegexTools {
	public static String getLastWordOfString(String string) {
		
        Pattern p = Pattern.compile("\\b(\\w+)\\b$");
        Matcher m = p.matcher(string);
       
        return  m.find() ? m.group(m.groupCount()-1):"";
	    
	}
	
	public static String getValueAfter(String testStr, String key){
        Pattern p = Pattern.compile("(?<="+key+"\\s)(\\w+)");
        Matcher m = p.matcher(testStr);
       
        return  m.find() ? m.group(1): null;
    }
	
	public static String getAllValuesAfter(String testStr, String key){
        Pattern p = Pattern.compile("(?<="+key+"\\s)(.*)");
        Matcher m = p.matcher(testStr);
       
        return  m.find() ? m.group(1): null;
    }
	
	public static String getValuesAfter(String testStr, String key,int count){
        Pattern p = Pattern.compile("(?<="+key+"\\s)(((\\w+)(\\s*)){"+count+"})");
        Matcher m = p.matcher(testStr);
       
        return  m.find() ? m.group(1): null;
    }
	
	public static String getValueAfterAndBefore(String testStr, String beforeTarget,String afterTarget){
        Pattern p = Pattern.compile("(?<="+beforeTarget+")(.+?)(?=("+afterTarget+"))");
        Matcher m = p.matcher(testStr);
       
        return  m.find() ? m.group(1): null;
    }
	
	public static List<String> getMatches(String string, String regex){
       return Pattern.compile(regex)
                .matcher(string)
                .results()
                .map(MatchResult::group)
                .collect(Collectors.toList());
    }
}
