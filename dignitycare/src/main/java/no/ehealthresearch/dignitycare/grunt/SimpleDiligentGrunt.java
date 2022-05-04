package no.ehealthresearch.dignitycare.grunt;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javafx.concurrent.Task;
import no.ehealthresearch.dignitycare.GlobalShutdown;

/**
 * diligent grunt tar kun en tråd om gangen!
 * @author tor003
 *
 */

public class SimpleDiligentGrunt implements DiligentGrunt{
	private final LinkedList<Supplier<Task<?>>> tasks=new LinkedList<>();
	private boolean working=false;
	
	private final ExecutorService executor=Executors.newSingleThreadExecutor();
	
	private SimpleDiligentGrunt() {
		
	}
	
	public static SimpleDiligentGrunt create() {
		return new SimpleDiligentGrunt();
	}
	
	private void processTask() {
		if(!working && !tasks.isEmpty() && !GlobalShutdown.INSTANCE.isShutDown()) {
			executor.submit(tasks.pop().get());
		}
	}
	
	private void reload() {
		working=false;
		processTask();
	}
	
	public <S,R,W> void setToWork(DiligentWorkTicket<S,R,W> ticket) {
		tasks.add(()->{
			
			Task<W> task=new Task<>() {
				private final R cachedSubstrate= ticket.getRequisiteMaker().apply(ticket.getSubstrate());
				
				@Override
				protected W call() throws Exception {
					return ticket.getWorker().apply(cachedSubstrate);
				}
			};
			
			task.setOnSucceeded(h->{
				ticket.getListener().accept(Optional.ofNullable(task.getValue()));
				reload();
			});
			
			task.setOnCancelled(h->{
				ticket.getListener().accept(Optional.empty());
				reload();
			});
			
			task.setOnFailed(h->{
				task.getException().printStackTrace(System.out);
				
				ticket.getListener().accept(Optional.empty());
				reload();
			});
			
			return task;
		});
		
		processTask();
	}
	
	public <R,W> void setToWork(R substrate,Function<R,W> worker,Consumer<Optional<W>> listener) {
		setToWork(DiligentWorkTicket.create(substrate,worker,listener));
	}

	
}
