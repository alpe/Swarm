package de.alpe.sandbox.swarm;

import java.util.concurrent.Callable;

import de.alpe.sandbox.swarm.asserts.ResultAsserter;
import de.alpe.sandbox.swarm.worker.WorkerSubset;

public interface WorkloadRecorder {

	<T> ResultAsserter<T> toCall(final T ignore);

	<T> ResultAsserter<T> toCall(final T ignore, final WorkerSubset workersToStart);

	<T> ResultAsserter<T> toCall(final WorkloadFactory<T> workloadFactory);

	WorkloadRecorder toCall(final Runnable workload);

	WorkloadRecorder toCall(final Runnable workload, final WorkerSubset workersToStart);

	<T> ResultAsserter<T> toCall(final Callable<T> workload);

	<T> ResultAsserter<T> toCall(final Callable<T> workload, final WorkerSubset workersToStart);

	<T> ResultAsserter<T> toCall(final WorkloadFactory<T> workloadFactory, final WorkerSubset workersToStart);

}