package de.alpe.sandbox.swarm.worker;

import java.util.concurrent.CountDownLatch;

public class WorkerBuilder {

	private int workerNumber;
	private CountDownLatch syncPoint;
	private String groupName;

	public WorkerBuilder withNumber(int workerNumber) {
		this.workerNumber = workerNumber;
		return this;
	}

	public WorkerBuilder withSyncPoint(CountDownLatch countDownLatch) {
		this.syncPoint = countDownLatch;
		return this;
	}

	public Worker build() {
		return new Worker(buildName(), syncPoint);
	}

	private String buildName() {
		return groupName + "-worker-" + workerNumber;
	}

	public WorkerBuilder withGroupName(String groupName) {
		this.groupName = groupName;
		return this;
	}

}
