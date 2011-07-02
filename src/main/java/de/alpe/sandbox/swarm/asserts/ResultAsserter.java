package de.alpe.sandbox.swarm.asserts;



import org.hamcrest.Matcher;

import de.alpe.sandbox.swarm.WorkloadRecorder;
import de.alpe.sandbox.swarm.worker.WorkerGroup;
import de.alpe.sandbox.swarm.worker.WorkersResultCollector;

public class ResultAsserter<T>  {

	private final WorkerGroup workerGroup;
	private final AssertionVerificator assertionVerificator;
	private final ResultAssertionFactory resultAsserterFactory;

	public ResultAsserter(final WorkerGroup workerGroup, final AssertionVerificator assertionVerificator, final ResultAssertionFactory resultAsserterFactory) {
		this.resultAsserterFactory = resultAsserterFactory;
		this.workerGroup = workerGroup;
		this.assertionVerificator = assertionVerificator;
	}

	public ResultAsserter<T> andAssertThat(final WorkersResultCollector resultCollector, final Matcher<T> matcher) {
		assertionVerificator.scheduleResultAssertion(resultAsserterFactory.buildResultAssertion(resultCollector, matcher));
		return this;
	}

	public WorkloadRecorder workers() {
		return this.workerGroup;
	}

	public ResultAsserter<T> withTimeout(final long timeout) {
		this.workerGroup.setTimeout(timeout);
		return this;
	}

}
