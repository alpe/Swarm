package de.alpe.sandbox.swarm.asserts;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;


public class AssertionVerificatorTest {

	@Test
	public void shouldCallAllScheduledVerificatorsWhenStarted() throws Exception {
		// given
		ResultAssertion firstAssertion = mock(ResultAssertion.class);
		ResultAssertion secondAssertion = mock(ResultAssertion.class);
		AssertionVerificator assertionVerificator = new AssertionVerificator();
		assertionVerificator.scheduleResultAssertion(firstAssertion);
		assertionVerificator.scheduleResultAssertion(secondAssertion);
		
		// when
		assertionVerificator.start();
		
		// then
		verify(firstAssertion).verify();
		verify(secondAssertion).verify();
	}
	@Test
	public void shouldCallResultAssertionWhenScheduledAfterStarted() throws Exception {
		// given
		ResultAssertion assertion = mock(ResultAssertion.class);
		AssertionVerificator assertionVerificator = new AssertionVerificator();
		
		assertionVerificator.start();
		// when
		assertionVerificator.scheduleResultAssertion(assertion);
		
		// then
		verify(assertion).verify();
	}
}
