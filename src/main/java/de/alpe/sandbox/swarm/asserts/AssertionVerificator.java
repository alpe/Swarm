package de.alpe.sandbox.swarm.asserts;

import java.util.LinkedList;
import java.util.List;


public class AssertionVerificator {

	private volatile boolean workloadCompleted = false;

	private final List<ResultAssertion> resultVerificationCallbacks = new LinkedList<ResultAssertion>();

	public void scheduleResultAssertion(final ResultAssertion resultAssertion) {
		if (workloadCompleted) {
			resultAssertion.verify();
		} else {
			resultVerificationCallbacks.add(resultAssertion);
		}
	}

	public void start() {
		workloadCompleted = true;
		for (ResultAssertion c : resultVerificationCallbacks) {
			c.verify();
		}
	}

}