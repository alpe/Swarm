package de.alpe.sandbox.swarm.asserts;

import java.util.Collection;

import org.hamcrest.Matcher;

import de.alpe.sandbox.swarm.worker.Worker;
import de.alpe.sandbox.swarm.worker.WorkersResultCollector;

public class ResultAssertionFactory {

	private final Collection<Worker> workersToVerify;
	private final String workerGroupAliasName;


	public ResultAssertionFactory(final Collection<Worker> workersToVerify, final String aliasName) {
		this.workersToVerify = workersToVerify;
		this.workerGroupAliasName = aliasName;
	}


	public ResultAssertion buildResultAssertion(final WorkersResultCollector resultCollector, final Matcher<?> matcher) {
		return new ResultAssertion() {
			@Override
			public void verify() {
				resultCollector.verify(workersToVerify, matcher, workerGroupAliasName);
			}
		};
	}

}