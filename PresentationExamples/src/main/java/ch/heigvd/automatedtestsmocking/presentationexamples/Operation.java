package ch.heigvd.automatedtestsmocking.presentationexamples;

/**
 * @author Simon Oulevay (simon.oulevay@heig-vd.ch)
 */
public class Operation {

	private String type;
	private double amount;

	public Operation(String type, double amount) {
		this.type = type;
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
