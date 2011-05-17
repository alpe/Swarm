package de.alpe.sandbox.swarm.worker;

import static de.alpe.sandbox.swarm.worker.WorkerSubset.onWorkers;
import static java.util.concurrent.Executors.callable;

import java.util.Collection;
import java.util.concurrent.Callable;

import com.jayway.awaitility.core.MethodCallRecorder;

import de.alpe.sandbox.swarm.MethodCaller;
import de.alpe.sandbox.swarm.WorkloadExecutor;
import de.alpe.sandbox.swarm.WorkloadFactory;
import de.alpe.sandbox.swarm.WorkloadRecorder;
import de.alpe.sandbox.swarm.asserts.AssertionVerificator;
import de.alpe.sandbox.swarm.asserts.ResultAsserter;
import de.alpe.sandbox.swarm.asserts.ResultAssertionFactory;

public class WorkerGroup implements WorkloadRecorder {

	private static final String DEFAULT_WORKER_ALIAS = "";

	private final WorkloadExecutor workloadExecutor;
	private final AssertionVerificator assertionVerificator;

	private long executionTimeout = 10000L;

	public WorkerGroup(final Worker[] workers) {
		workloadExecutor = new WorkloadExecutor(workers);
		assertionVerificator = new AssertionVerificator();
	}

	@Override
	public <T> ResultAsserter<T> toCall(final T ignore) {
		return toCall(ignore, onWorkers(workloadExecutor.getNumberOfAvailableWorkers(), DEFAULT_WORKER_ALIAS));
	}

	@Override
	public <T> ResultAsserter<T> toCall(final T ignore, final WorkerSubset workersToStart) {
		MethodCaller<T> workload = new MethodCaller<T>(MethodCallRecorder.getLastTarget(), MethodCallRecorder.getLastMethod(),
				MethodCallRecorder.getLastArgs());
		return toCall(workload, workersToStart);
	}

	@Override
	public <T> ResultAsserter<T> toCall(final WorkloadFactory<T> workloadFactory) {
		return toCall(workloadFactory, onWorkers(workloadExecutor.getNumberOfAvailableWorkers(), DEFAULT_WORKER_ALIAS));
	}

	@Override
	public WorkloadRecorder toCall(final Runnable workload) {
		return toCall(workload, onWorkers(workloadExecutor.getNumberOfAvailableWorkers(), DEFAULT_WORKER_ALIAS));
	}

	@Override
	public WorkloadRecorder toCall(final Runnable workload, final WorkerSubset workersToStart) {
		toCall(callable(workload, workersToStart));
		return this;
	}

	@Override
	public <T> ResultAsserter<T> toCall(final Callable<T> workload) {
		return toCall(workload, onWorkers(workloadExecutor.getNumberOfAvailableWorkers(), DEFAULT_WORKER_ALIAS));
	}

	@Override
	public <T> ResultAsserter<T> toCall(final Callable<T> workload, final WorkerSubset workersToStart) {
		return toCall(singleWorkloadFactory(workload), workersToStart);
	}

	@Override
	public <T> ResultAsserter<T> toCall(final WorkloadFactory<T> workloadFactory, final WorkerSubset workersToStart) {
		Collection<Worker> startedWorkers = workloadExecutor.startWorkers(workloadFactory, workersToStart);
		if (workloadExecutor.hasAllWorkersStarted()) {
			postProcess();
		}
		return new ResultAsserter<T>(this, assertionVerificator, new ResultAssertionFactory(startedWorkers, workersToStart.getAliasName()));
	}

	private static <T> WorkloadFactory<T> singleWorkloadFactory(final Callable<T> workload) {
		return new WorkloadFactory<T>() {
			@Override
			public Callable<T> create() {
				return workload;
			}
		};
	}

	public void setTimeout(final long timeout) {
		this.executionTimeout = timeout;
	}

	private void postProcess() {
		workloadExecutor.joinWorkers(executionTimeout);
		assertionVerificator.start();
	}

}