package no.ehealthresearch.dignitycare.grunt;

public class LazyQueueForDiligentGrunt implements DiligentGrunt{
	public static LazyQueueForDiligentGrunt createLazyQueueForDiligentGrunt(DiligentGrunt grunt) {
		return new LazyQueueForDiligentGrunt(grunt);
	}

	private final DiligentGrunt grunt;
	
	private DiligentWorkTicket<?,?,?> unresolvedTicket;
	private boolean isWorking=false;
	
	private LazyQueueForDiligentGrunt(DiligentGrunt grunt) {
		this.grunt=grunt;
	}
	
	
	
	private void resolve() {
		
		if(unresolvedTicket!=null) {
			DiligentWorkTicket<?, ?, ?> cached = unresolvedTicket;
			unresolvedTicket=null;
			grunt.setToWork(cached);
		}else {
			isWorking=false;
		}
		
		
	}
	
	@Override
	public <S,R,W> void setToWork(DiligentWorkTicket<S,R,W> ticket) {
		DiligentWorkTicket<S, R, W> intercepted = DiligentWorkTicket.create(
				ticket.getSubstrate(),
				ticket.getRequisiteMaker(), 
				ticket.getWorker(),l->{
					
					ticket.getListener().accept(l);
					resolve();
				});
		
		if(isWorking) {
			unresolvedTicket=intercepted;
		}else {
			grunt.setToWork(intercepted);
		}
	}
}
