package de.alpe.sandbox.swarm.asserts;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.hamcrest.Matcher;
import org.junit.Test;

import de.alpe.sandbox.swarm.worker.Worker;
import de.alpe.sandbox.swarm.worker.WorkerGroup;
import de.alpe.sandbox.swarm.worker.WorkersResultCollector;

public class ResultAsserterTest {

	@Test
	public void shouldScheduleResultAssertionWhenVerifyThatCalled() throws Exception {
		// given
		WorkerGroup workerGroup = null;
		Collection<Worker> workersToVerify = emptyList();
		String aliasName = "aliasName";
		AssertionVerificator assertionVerificator = mock(AssertionVerificator.class);
		ResultAsserter<Object> resultAsserter = new ResultAsserter<Object>(workerGroup, assertionVerificator, new ResultAssertionFactory(workersToVerify, aliasName));
		
		// when
		WorkersResultCollector anyResultCollector = null;
		Matcher<Object> anyMatcher = null;
		resultAsserter.andVerifyThat(anyResultCollector, anyMatcher);
		
		// then
		verify(assertionVerificator).scheduleResultAssertion((ResultAssertion) notNull());
	}
	
	@Test
	public void shouldXXX() throws Exception {
		// given
		WorkerGroup workerGroup = null;
		Collection<Worker> workersToVerify = emptyList();
		String aliasName = "aliasName";
		AssertionVerificator assertionVerificator = mock(AssertionVerificator.class);
		ResultAsserter<Object> resultAsserter = new ResultAsserter<Object>(workerGroup, assertionVerificator, new ResultAssertionFactory(workersToVerify, aliasName));
		// when
		WorkersResultCollector anyResultCollector = null;
		Matcher<Object> anyMatcher = is(new Object());
		resultAsserter.andVerifyThat(anyResultCollector, anyMatcher);
		
		// then
		verify(assertionVerificator).scheduleResultAssertion((ResultAssertion) notNull());
	}
}
