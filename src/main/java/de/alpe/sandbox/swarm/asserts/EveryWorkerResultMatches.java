package de.alpe.sandbox.swarm.asserts;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hamcrest.Matcher;

import de.alpe.sandbox.swarm.worker.Worker;
import de.alpe.sandbox.swarm.worker.WorkersResultCollector;

public class EveryWorkerResultMatches implements WorkersResultCollector {

	@Override
	public <T> void verify(final Collection<Worker> workers, final Matcher<T> matcher, final String aliasName) {
		List<Worker> failedWorker = new ArrayList<Worker>();
		for (Worker worker : workers) {
			if (!matcher.matches(worker.getCallResult())) {
				failedWorker.add(worker);
			}
		}
		if (!failedWorker.isEmpty()) {
			throw new java.lang.AssertionError(buildFailureMessage(failedWorker, workers.size(), matcher, aliasName));
		}
	}

	private static <T> String buildFailureMessage(final List<Worker> failedWorker, final int totalWorkerCount, final Matcher<T> matcher, final String aliasName) {
		String headline = format("%s of %s '%s' workers failed: \n", failedWorker.size(), totalWorkerCount, aliasName);
		return new WorkerResultDescriptionBuilder().withWorkers(failedWorker).withHeadline(headline).withMatcher(matcher).build();
	}
}