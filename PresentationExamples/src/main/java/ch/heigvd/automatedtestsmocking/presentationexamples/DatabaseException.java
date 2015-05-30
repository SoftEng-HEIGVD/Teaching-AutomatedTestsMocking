package ch.heigvd.automatedtestsmocking.presentationexamples;

/**
 * @author Simon Oulevay (simon.oulevay@heig-vd.ch)
 */
public class DatabaseException extends RuntimeException {

	public DatabaseException() {
	}

	public DatabaseException(String msg) {
		super(msg);
	}
}
