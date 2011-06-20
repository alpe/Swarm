package de.alpe.sandbox.swarm.worker;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class Worker implements Runnable {

	private final CountDownLatch syncPoint;
    private final String name;
    private Callable<?> workLoad;
    private Object callResult;
    private Exception workloadExecutionException;

    protected Worker(final String name, final CountDownLatch syncPoint) {
		this.name = name;
		this.syncPoint = syncPoint;
	}

	public void setWorkLoad(final Callable<?> workLoad) {
		this.workLoad = workLoad;
	}

    public Exception getWorkloadExecutionException() {
        return workloadExecutionException;
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
            this.workloadExecutionException = e;
            throw new RuntimeException(workloadExecutionException);
		}
	}


	@Override
	public String toString() {
		return "Worker [name=" + name + "]";
	}

}