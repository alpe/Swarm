package de.alpe.sandbox.swarm.examples;

import de.alpe.sandbox.swarm.asserts.WorkerResultCollectors;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static de.alpe.sandbox.swarm.SwarmProvider.on;
import static de.alpe.sandbox.swarm.SwarmProvider.swarm;
import static de.alpe.sandbox.swarm.asserts.WorkerResultCollectors.anyReceivedResult;
import static de.alpe.sandbox.swarm.asserts.WorkerResultCollectors.atLeast;
import static de.alpe.sandbox.swarm.asserts.WorkerResultCollectors.everyReceivedResult;
import static de.alpe.sandbox.swarm.worker.WorkerSubset.BY_THREE_WORKERS;
import static de.alpe.sandbox.swarm.worker.WorkerSubset.BY_TWO_WORKERS;
import static de.alpe.sandbox.swarm.worker.WorkerSubset.onWorkers;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ManyToOneTests {


	@Test
	public void manyClientsOneMethod_callable() throws Exception {
		// given
		final List<Object> userRepository = new ArrayList<Object>();
		
		// when
		swarm(5).workers().toCall(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return userRepository.add("anyObject");
			}
		});
		
		// then
		assertThat(userRepository.size(), is(5));
	}

	@Test
	public void manyClientsOneMethod_Runnable() throws Exception {
		// given
		final List<Object> userRepository = new ArrayList<Object>();
		
		// when
		swarm(5).workers().toCall(new Runnable() {
			@Override
			public void run() {
				userRepository.add(0, "anyObject"); // void method
			}
		});
		// then
		assertThat(userRepository.size(), is(5));
	}

	@Test
	public void manyClientsOneMethod_proxy() throws Exception {
		// given
		final List<Object> userRepository = new ArrayList<Object>();
		
		// when
		swarm(5).workers().toCall(on(userRepository).add("anyObject"));
		
		// then
		assertThat(userRepository.size(), is(5));
	}

	@Test
	public void manyClientsOneMethod_verifyResult_every() throws Exception {
		// given
		final List<Object> userRepository = new ArrayList<Object>();
		
		// when
		swarm(5).workers()
                .toCall(on(userRepository).add("anyObject"))
                .andAssertThat(everyReceivedResult(),
                        is(equalTo(true)));
		
		// then
		assertThat(userRepository.size(), is(5));
	}

	@Test
	public void manyClientsOneMethod_within() throws Exception {
		// given
		final List<Object> userRepository  = new ArrayList<Object>();
		
		// when
		swarm(5).workers()
                .toCall(on(userRepository).add("anyObject"))
                .withTimeout(1000L)
                .andAssertThat(everyReceivedResult(), is(equalTo(true)));
		
		// then
		assertThat(userRepository.size(), is(5));
	}

	@Test
	public void manyClientsOneMethod_verifyResult_any() throws Exception {
		// given
		final List<Object> userRepository = new ArrayList<Object>();
		
		// when
		swarm(5).workers()
                .toCall(on(userRepository).add("anyObject"))
                .andAssertThat(anyReceivedResult(), is(equalTo(true)));
		
		// then
		assertThat(userRepository.size(), is(5));
	}

	@Test
	public void manyClientsOneMethod_differentMethodsCalled() throws Exception {
		// given
		final List<Object> userRepository = new ArrayList<Object>();
		
		// when
		swarm(5).workers()
                .toCall(on(userRepository).add("anyObject"),
                        BY_TWO_WORKERS) //
				.workers()
                .toCall(on(userRepository).add("otherObject"),
                        BY_THREE_WORKERS);
		
		// then
		assertThat(userRepository.size(), is(5));
		assertThat(countElements(userRepository, "anyObject"), is(2));
		assertThat(countElements(userRepository, "otherObject"), is(3));
	}
	

	@Test
	public void manyClientsOneMethod_differentMethodsCalled_multipleResultAssertions() throws Exception {
		// given
		final List<Object> userRepository = new ArrayList<Object>();
		
		// when
		swarm(5).workers()
                .toCall(on(userRepository).add("anyObject"), onWorkers(2, "alias")) //
				.andAssertThat(everyReceivedResult(), is(equalTo(true)))//
				.andAssertThat(anyReceivedResult(), is(equalTo(true)))//
				.workers()
                .toCall(on(userRepository).add("otherObject"),
                        BY_THREE_WORKERS) //
				.andAssertThat(everyReceivedResult(), is(equalTo(true)))
				.andAssertThat(atLeast(2), is(equalTo(true)))
				;
		// then
		assertThat(userRepository.size(), is(5));

	}

	private static int countElements(final List<Object> userRepository, final String source) {
		int result = 0;
		for (Object object : userRepository) {
			if (object.equals(source)) {
				result++;
			}
		}
		return result;
	}
}
