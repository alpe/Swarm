package de.alpe.sandbox.swarm;

import com.jayway.awaitility.core.MethodCallRecorder;

import de.alpe.sandbox.swarm.worker.WorkerGroupBuilder;

public class SwarmProvider {

	private final int workerCount;

	private SwarmProvider(final int workerCount) {
		this.workerCount = workerCount;
	}

	public WorkloadRecorder workers() {
		return new WorkerGroupBuilder().setWorkers(workerCount).build();
	}

	public static SwarmProvider swarm(final int workerCount) {
		return new SwarmProvider(workerCount);
	}

	@SuppressWarnings("unchecked")
	public static <T> T on(final T object) {
		return  (T) MethodCallRecorder.createProxy(object);
	}

}