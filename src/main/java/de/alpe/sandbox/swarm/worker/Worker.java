package de.alpe.sandbox.swarm.worker;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class Worker implements Runnable {

	private final CountDownLatch syncPoint;
	private Callable<?> workLoad;
	private Object callResult;
	private final String name;

	public Worker(final String name, final CountDownLatch syncPoint) {
		this.name = name;
		this.syncPoint = syncPoint;
	}

	public void setWorkLoad(final Callable<?> workLoad) {
		this.workLoad = workLoad;
	}

	public Object getCallResult() {
		return callResult;
	}

	@Override
	public void run() {
		syncPoint.countDown();
		try {
			syncPoint.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		try {
			callResult = this.workLoad.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return "Worker [name=" + name + "]";
	}

}