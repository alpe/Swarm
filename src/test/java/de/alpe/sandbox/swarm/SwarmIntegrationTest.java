package de.alpe.sandbox.swarm;

import static de.alpe.sandbox.swarm.SwarmProvider.on;
import static de.alpe.sandbox.swarm.SwarmProvider.swarm;
import static java.lang.System.currentTimeMillis;
import static java.util.Collections.max;
import static java.util.Collections.min;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class SwarmIntegrationTest {

    @Test
    public void shouldExecuteWorkloadSimultan() throws Exception {
        // given
        ThreadCollector threadCollector = new ThreadCollector();
        // when
        swarm(50).workers().toCall(on(threadCollector).doSomething());

        // then
        Collection<Long> executionTimes = threadCollector.getExecutionTimes();
        assertThat(executionTimes.size(), is(50));
        Long minExecutionStart = min(executionTimes);
        Long maxExecutionStart = max(executionTimes);
        assertThat(maxExecutionStart - minExecutionStart, lessThan(50L));

    }

    private static class ThreadCollector {
        private final Map<Thread, Long> threadMap = new java.util.concurrent.ConcurrentHashMap<Thread, Long>();

        public Object doSomething() {
            threadMap.put(Thread.currentThread(), currentTimeMillis());
            return null;
        }

        public Collection<Long> getExecutionTimes() {
            return new ArrayList<Long>(threadMap.values());
        }

    }
}
