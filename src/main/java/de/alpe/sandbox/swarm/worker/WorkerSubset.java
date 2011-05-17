package de.alpe.sandbox.swarm.worker;

public class WorkerSubset {

	public static final WorkerSubset BY_ONE_WORKER = onWorkers(1, "ONE");
	public static final WorkerSubset BY_TWO_WORKERS = onWorkers(2, "TWO");
	public static final WorkerSubset BY_THREE_WORKERS = onWorkers(3, "THREE");
	public static final WorkerSubset BY_FOUR_WORKERS = onWorkers(4, "FOUR");
	public static final WorkerSubset BY_FIVE_WORKERS = onWorkers(5, "FIVE");
	public static final WorkerSubset BY_TEN_WORKERS = onWorkers(10, "TEN");

	private final int quantity;
	private final String aliasName;

	private WorkerSubset(int quantity, String alias) {
		this.quantity = quantity;
		this.aliasName = alias;
	}

	public static WorkerSubset onWorkers(int quantity) {
		return onWorkers(quantity, "with quantity: "+quantity);
	}

	public static WorkerSubset onWorkers(int quantity, String alias) {
		return new WorkerSubset(quantity, alias);
	}

	public int getQuantity() {
		return quantity;
	}

	public String getAliasName() {
		return aliasName;
	}
}
