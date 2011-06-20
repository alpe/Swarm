package de.alpe.sandbox.swarm.asserts;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.alpe.sandbox.swarm.worker.WorkersResultCollector;

import org.hamcrest.Matcher;
import org.junit.Test;

public class ResultAsserterTest {

	@Test
	public void shouldUseResultAssertionFactoryToBuildAssertionObject() throws Exception {
		// given
        WorkersResultCollector resultCollector = mock(WorkersResultCollector.class);
        Matcher<Object> matcher = is(Object.class);

        ResultAssertionFactory resultAsserterFactory = mock(ResultAssertionFactory.class);
        when(resultAsserterFactory.buildResultAssertion(resultCollector, matcher)).thenReturn(null);

        ResultAsserter<Object> resultAsserter = new ResultAsserter<Object>(null, mock(AssertionVerificator.class), resultAsserterFactory);

        // when
		resultAsserter.andVerifyThat(resultCollector, matcher);
		
		// then
        verify(resultAsserterFactory).buildResultAssertion(resultCollector, matcher);
	}
	@Test
	public void shouldScheduleResultAssertionWhenVerifyThatCalled() throws Exception {
		// given
        WorkersResultCollector anyResultCollector = mock(WorkersResultCollector.class);
        Matcher<Object> matcher = is(Object.class);

        ResultAssertion resultAssertion = mock(ResultAssertion.class);
        ResultAssertionFactory resultAsserterFactory = mock(ResultAssertionFactory.class);
        when(resultAsserterFactory.buildResultAssertion(anyResultCollector, matcher)).thenReturn(resultAssertion);

        AssertionVerificator assertionVerificator = mock(AssertionVerificator.class);
        ResultAsserter<Object> resultAsserter = new ResultAsserter<Object>(null, assertionVerificator, resultAsserterFactory);

        // when
		resultAsserter.andVerifyThat(anyResultCollector, matcher);

		// then
		verify(assertionVerificator).scheduleResultAssertion(resultAssertion);
	}

}