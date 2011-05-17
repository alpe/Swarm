package de.alpe.sandbox.swarm.worker;

import de.alpe.sandbox.swarm.asserts.AtLeastWorkerResultMatches;
import de.alpe.sandbox.swarm.asserts.EveryWorkerResultMatches;

public class WorkerResultCollectors {

	public static WorkersResultCollector everyReceivedResult() {
		return new EveryWorkerResultMatches();
	}

	public static WorkersResultCollector anyReceivedResult() {
		return new AtLeastWorkerResultMatches(1);
	}

	public static WorkersResultCollector atLeast(int number) {
		return new AtLeastWorkerResultMatches(number);
	}

}
