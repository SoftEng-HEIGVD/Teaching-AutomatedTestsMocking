package ch.heigvd.automatedtestsmocking.presentationexamples;

import java.util.Date;
import java.util.List;

/**
 * @author Simon Oulevay (simon.oulevay@heig-vd.ch)
 */
public interface CustomerAccountManager {

	List<Operation> findOperationsSince(Customer customer, Date date);
}
