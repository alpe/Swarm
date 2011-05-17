package de.alpe.sandbox.swarm;

import java.util.concurrent.Callable;

public interface WorkloadFactory<T> {

	 Callable<T> create();
}
