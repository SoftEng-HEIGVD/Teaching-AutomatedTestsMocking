package ch.heigvd.automatedtestsmocking.presentationexamples;

/**
 * @author Simon Oulevay (simon.oulevay@heig-vd.ch)
 */
public class CustomerAccountException extends RuntimeException {

	public CustomerAccountException() {
	}

	public CustomerAccountException(String msg) {
		super(msg);
	}

	public CustomerAccountException(String message, Throwable cause) {
		super(message, cause);
	}
}
