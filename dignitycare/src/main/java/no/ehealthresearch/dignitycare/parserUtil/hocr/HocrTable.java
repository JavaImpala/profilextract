package no.ehealthresearch.dignitycare.parserUtil.hocr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import no.ehealthresearch.dignitycare.parserUtil.hocr.HocrColumnSegments.HocrColumnTextLine;
import no.ehealthresearch.dignitycare.parserUtil.hocr.HocrColumnSegments.HocrColumnTextLines;
import no.ehealthresearch.dignitycare.parserUtil.hocr.HocrColumnSegments.TextInColumn;
import no.ehealthresearch.dignitycare.parserUtil.lineParser.TextLine;

/**
 * kan ikke breaks på vørste linje i segment. breaker kan bare ha en linje
 * 
 * @author tor003
 *
 */

public class HocrTable {
	private final List<HocrTableRow> rows=new ArrayList<>();
	
	public HocrTable(HocrColumnTextLines columns,Supplier<HocrRowBreaker> rowBreakers) {
		
		HocrRowBreaker currentBreaker=rowBreakers.get();
		HocrTableRow currentRows=new HocrTableRow();
		
		for(HocrColumnTextLine line:columns.getLines()) {
			List<Optional<TextInColumn>> c = line.getColumns();
			
			boolean shouldBreak = currentBreaker.shoudBreak(c);
			
			if(shouldBreak) {
				rows.add(currentRows);
				
				currentRows=new HocrTableRow();
				currentBreaker=rowBreakers.get();
				
				currentBreaker.shoudBreak(c);
				
			}
			
			currentRows.add(c);
		}
		
		rows.add(currentRows);
	}
	
	public List<HocrTableRow> rows(){
		return rows;
	}
	
	public static class HocrTableRow{
		private List<List<Optional<TextInColumn>>> srcColumns=new ArrayList<>();
		
		private void add(List<Optional<TextInColumn>> srcC) {
			this.srcColumns.add(srcC);
		}
		
		public List<Optional<List<TextLine>>> get(){
			
			HashMap<Integer,List<TextLine>> map=new HashMap<>();
			
			int maxCols=0;
			
			for(List<Optional<TextInColumn>> row:srcColumns) {
				maxCols=Integer.max(maxCols,row.size());
				
				for(int i=0;i<row.size();i++) {
					Optional<TextInColumn> col=row.get(i);
					
					if(col.isPresent()) {
						map.computeIfAbsent(i,e->new ArrayList<>()).add(col.get().getText());
					}
					
				}
			}
			
			List<Optional<List<TextLine>>> output=new ArrayList<>();
			
			for(int i=0;i<maxCols;i++) {
				if(map.containsKey(i)) {
					output.add(Optional.of(map.get(i)));
				}else {
					output.add(Optional.empty());
				}
			}
			
			return output;
		}	
		
		
	}
}
