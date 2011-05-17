package de.alpe.sandbox.swarm.asserts;

import java.util.Collection;
import java.util.Iterator;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import de.alpe.sandbox.swarm.worker.Worker;

public class WorkerResultDescriptionBuilder {

	private Collection<Worker> workers;
	private Matcher<?> matcher;
	private String headline;

	public WorkerResultDescriptionBuilder withWorkers(final Collection<Worker> workers) {
		this.workers = workers;
		return this;
	}


	public WorkerResultDescriptionBuilder withMatcher(final Matcher<?> matcher) {
		this.matcher = matcher;
		return this;
	}
	
	public WorkerResultDescriptionBuilder withHeadline(final String headline) {
		this.headline = headline;
		return this;
	}

	public String build() {
		Description description = new StringDescription();
		description.appendText(headline);
		description.appendText("\nExpected result: ");
		description.appendDescriptionOf(matcher);
		description.appendText("\n     got: ");
		description.appendText(flattenWorkerResults(workers));
		description.appendText("\n");
		return description.toString();
	}
	
	private static String flattenWorkerResults(final Collection<Worker> failedWorker) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Iterator<Worker> it = failedWorker.iterator(); it.hasNext();) {
			Worker worker = it.next();
			sb.append(worker.getCallResult());
			if (it.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}



}
