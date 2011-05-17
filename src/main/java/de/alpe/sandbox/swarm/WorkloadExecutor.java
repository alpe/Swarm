package de.alpe.sandbox.swarm;

import static java.util.Arrays.asList;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import de.alpe.sandbox.swarm.worker.Worker;
import de.alpe.sandbox.swarm.worker.WorkerSubset;

public class WorkloadExecutor {

	private final ExecutorService executor;
	private final Queue<Worker> availableWorkers;

	public WorkloadExecutor(final Worker[] workers) {
		this.availableWorkers = new LinkedList<Worker>(asList(workers));
		this.executor = newFixedThreadPool(workers.length);
	}

	public int getNumberOfAvailableWorkers() {
		return availableWorkers.size();
	}

	public boolean hasAllWorkersStarted() {
		return availableWorkers.isEmpty();
	}

	public <T> Collection<Worker> startWorkers(final Callable<T> workLoad, final WorkerSubset workersToStart) {
		return startWorkers(new WorkloadFactory<T>() {
			@Override
			public Callable<T> create() {
				return workLoad;
			}
		}, workersToStart);
	}

	public Collection<Worker> startWorkers(final WorkloadFactory<?> workloadFactory, final WorkerSubset workersToStart) {
		assertThat("insufficient workers configured", availableWorkers.size(), greaterThanOrEqualTo(workersToStart.getQuantity()));
		Collection<Worker> startedWorkers = new ArrayList<Worker>(workersToStart.getQuantity());
		for (int i = 0; i < workersToStart.getQuantity(); i++) {
			startedWorkers.add(submitWorkload(workloadFactory.create()));
		}
		return startedWorkers;
	}

	private Worker submitWorkload(final Callable<?> workLoad) {
		Worker worker = availableWorkers.remove();
		worker.setWorkLoad(workLoad);
		executor.submit(worker);
		return worker;
	}

	public void joinWorkers(final long timeout) {
		executor.shutdown();
		try {
			executor.awaitTermination(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}