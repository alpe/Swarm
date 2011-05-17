package de.alpe.sandbox.swarm;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/** 
 * copied from 
 * com.jayway.awaitility.core.ConditionFactory.MethodCaller
 */
public class MethodCaller <T> implements Callable<T> {

	private final Object target;
    private final Method method;
    private final Object[] args;

    public MethodCaller(Object target, Method method, Object[] args) {
        this.target = target;
        this.method = method;
        this.args = args;
        method.setAccessible(true);
    }

    @SuppressWarnings("unchecked")
    public T call() throws Exception {
        return (T) method.invoke(target, args);
    }
}
