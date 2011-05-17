package de.alpe.sandbox.swarm;

import static de.alpe.sandbox.swarm.SwarmProvider.swarm;
import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.Callable;

import org.junit.Test;

import de.alpe.sandbox.swarm.WorkloadFactory;

public class DummyTest {

	@Test
	public void manyClientsOneMethod_differentMethodsCalled() throws Exception {
		// given
		final StatefulService userRepository = new StatefulService();

		final StringBuilder sb = new StringBuilder();
		// when
		swarm(20).workers().toCall(createThreadFactory(userRepository, sb));
		// then

		assertThat(sb.toString(), sb.toString(), is(""));
	}

	private WorkloadFactory<Void> createThreadFactory(final StatefulService userRepository, final StringBuilder sb) {
		return new WorkloadFactory<Void>() {
			@Override
			public Callable<Void> create() {
				int value = Double.valueOf(Math.random() * Integer.MAX_VALUE).intValue();
				int returnedValue = userRepository.setState(value);
				if (value != returnedValue) {
					sb.append(format("richtig in die hose!!! erwartet %s war %s ", value, returnedValue));
				}
				return null;
			}
		};
	}

	public static class StatefulService {

		private volatile int state;
		private final Object monitor = new Object();

		public int setState(final int state) {
			synchronized (monitor) {
				this.state = state;
				hiePassiertDieVerarbeitung();
				return this.state;
			}
		}

		private void hiePassiertDieVerarbeitung() {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}

	}
}
