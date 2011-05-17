package de.alpe.sandbox.swarm;

import static de.alpe.sandbox.swarm.SwarmProvider.on;
import static de.alpe.sandbox.swarm.SwarmProvider.swarm;
import static de.alpe.sandbox.swarm.worker.WorkerSubset.BY_THREE_WORKERS;
import static de.alpe.sandbox.swarm.worker.WorkerSubset.BY_TWO_WORKERS;
import static de.alpe.sandbox.swarm.worker.WorkerSubset.onWorkers;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.junit.Test;

import de.alpe.sandbox.swarm.worker.WorkerResultCollectors;

public class ManyToOneTests {

	List<Object> userRepository;
	
	@Test
	public void manyClientsOneMethod_callable() throws Exception {
		// given
		userRepository = new ArrayList<Object>();
		
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
		userRepository = new ArrayList<Object>();
		
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
		userRepository = new ArrayList<Object>();
		
		// when
		swarm(5).workers().toCall(on(userRepository).add("anyObject"));
		
		// then
		assertThat(userRepository.size(), is(5));
	}

	@Test
	public void manyClientsOneMethod_verifyResult_every() throws Exception {
		// given
		userRepository = new ArrayList<Object>();
		
		// when
		swarm(5).workers().toCall(on(userRepository).add("anyObject")).andVerifyThat(WorkerResultCollectors.everyReceivedResult(), is(equalTo(true)));
		
		// then
		assertThat(userRepository.size(), is(5));
	}
	@Test
	public void manyClientsOneMethod_within() throws Exception {
		// given
		userRepository = new ArrayList<Object>();
		
		// when
		swarm(5).workers().toCall(on(userRepository).add("anyObject"))
			.withTimeout(1000L).andVerifyThat(WorkerResultCollectors.everyReceivedResult(), is(equalTo(true)));
		
		// then
		assertThat(userRepository.size(), is(5));
	}

	@Test
	public void manyClientsOneMethod_verifyResult_any() throws Exception {
		// given
		userRepository = new ArrayList<Object>();
		
		// when
		swarm(5).workers().toCall(on(userRepository).add("anyObject")).andVerifyThat(WorkerResultCollectors.anyReceivedResult(), is(equalTo(true)));
		
		// then
		assertThat(userRepository.size(), is(5));
	}

	@Test
	public void manyClientsOneMethod_differentMethodsCalled() throws Exception {
		// given
		userRepository = new ArrayList<Object>();
		
		// when
		swarm(5).workers().toCall(on(userRepository).add("anyObject"), BY_TWO_WORKERS) //
				.workers().toCall(on(userRepository).add("otherObject"), BY_THREE_WORKERS);
		
		// then
		assertThat(userRepository.size(), is(5));
		assertThat(countElements(userRepository, "anyObject"), is(2));
		assertThat(countElements(userRepository, "otherObject"), is(3));
	}
	

	@Test
	public void manyClientsOneMethod_differentMethodsCalled_multipleResultAssertions() throws Exception {
		// given
		userRepository = new ArrayList<Object>();
		
		// when
		swarm(5).workers().toCall(on(userRepository).add("anyObject"), onWorkers(2, "alias")) //
				.andVerifyThat(WorkerResultCollectors.everyReceivedResult(), is(equalTo(true)))//
				.andVerifyThat(WorkerResultCollectors.anyReceivedResult(), is(equalTo(true)))//
				.workers().toCall(on(userRepository).add("otherObject"), BY_THREE_WORKERS) //
				.andVerifyThat(WorkerResultCollectors.everyReceivedResult(), is(equalTo(true)))
				.andVerifyThat(WorkerResultCollectors.atLeast(2), is(equalTo(true)))
				;
		// then
		assertThat(userRepository.size(), is(5));

	}

	private static int countElements(final List<Object> userRepository, final String source) {
		int result = 0;
		for (Object object : userRepository) {
			System.out.println(format("%s equals %s: %s", object, source, object.equals(source)));
			if (object.equals(source)) {
				result++;
			}
		}
		return result;
	}
}
