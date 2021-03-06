package de.alpe.sandbox.swarm.asserts;

import de.alpe.sandbox.swarm.worker.Worker;
import de.alpe.sandbox.swarm.worker.WorkersResultCollector;
import org.hamcrest.Matcher;

import java.util.Collection;

import static java.lang.String.format;

public class AtLeastWorkerResultMatches implements WorkersResultCollector {

	private final int number;

    AtLeastWorkerResultMatches(int number) {
        this.number = number;
    }

	@Override
	public <T> void verify(Collection<Worker> workers, Matcher<T> matcher, String aliasName) {
		int successCount = 0;
		for (Worker worker : workers) {
			if (matcher.matches(worker.getCallResult()) //
					&& ++successCount == number) {
				return;
			}
		}
		String header = format("Expected %s of %s '%s' workers to match but was %s that match: ", number, workers.size(), aliasName,
				successCount);
		String message = new WorkerResultDescriptionBuilder().withWorkers(workers).withHeadline(header).withMatcher(matcher).build();
		throw new java.lang.AssertionError(message);
	}
}