package de.alpe.sandbox.swarm.worker;

import java.util.Collection;

import org.hamcrest.Matcher;

public interface WorkersResultCollector {

	<T> void verify(Collection<Worker> workers, Matcher<T> matcher, String aliasName);

}