package no.ehealthresearch.dignitycare.grunt;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class DiligentWorkTicket<S,R,W>{
	private final S substrate;
	private final Function<S, R> requisiteMaker; 
	private final Function<R, W> worker;
	private final Consumer<Optional<W>> listener;
	
	private DiligentWorkTicket(
			S substrate, 
			Function<S, R> requisiteMaker, 
			Function<R, W> worker,
			Consumer<Optional<W>> listener) {
		this.substrate=substrate;
		this.requisiteMaker=requisiteMaker;
		this.worker=worker;
		this.listener=listener;
	}
	
	public static <S,R,W> DiligentWorkTicket<S,R,W> create(
			S substrate, 
			Function<S, R> requisiteMaker, 
			Function<R, W> worker,
			Consumer<Optional<W>> listener){
		return new DiligentWorkTicket<>(substrate,requisiteMaker,worker,listener);
	}
	
	public static <R,W> DiligentWorkTicket<R,R,W> create(
			R substrate, 
			Function<R, W> worker,
			Consumer<Optional<W>> listener){
		return new DiligentWorkTicket<>(substrate,r->r,worker,listener);
	}

	public S getSubstrate() {
		return substrate;
	}

	public Function<S, R> getRequisiteMaker() {
		return requisiteMaker;
	}

	public Function<R, W> getWorker() {
		return worker;
	}

	public Consumer<Optional<W>> getListener() {
		return listener;
	}
	
	
	
}