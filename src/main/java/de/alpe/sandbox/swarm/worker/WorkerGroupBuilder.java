package de.alpe.sandbox.swarm.worker;

import java.util.concurrent.CountDownLatch;

import de.alpe.sandbox.swarm.WorkloadRecorder;

public class WorkerGroupBuilder {

	private Worker[] workers = new Worker[0];
	private String groupName = "";

	public WorkerGroupBuilder setWorkers(int workerCount) {
		workers = new Worker[workerCount];
		return this;
	}


	public WorkloadRecorder build() {
		int workerCount = workers.length;
		final CountDownLatch countDownLatch = new CountDownLatch(
				workerCount);
		for (int i = 0; i < workerCount; i++) {
			workers[i] = new WorkerBuilder().withNumber(i).withGroupName(groupName)
					.withSyncPoint(countDownLatch).build();
		}
		return new WorkerGroup(workers);
	}

}