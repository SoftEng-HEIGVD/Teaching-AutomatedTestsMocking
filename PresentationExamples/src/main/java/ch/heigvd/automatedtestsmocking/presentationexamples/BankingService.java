package ch.heigvd.automatedtestsmocking.presentationexamples;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Simon Oulevay (simon.oulevay@heig-vd.ch)
 */
public class BankingService {

	//@EJB
	private CustomerAccountManager accountManager;
	//@EJB
	private TimeService timeService;

	public double getLastMonthBalance(Customer customer) {

		// get last month date
		Calendar cal = Calendar.getInstance();
		cal.setTime(timeService.getCurrentDate());
		cal.add(Calendar.MONTH, -1);
		Date lastMonth = cal.getTime();

		// fetch operations during the last month
		List<Operation> operations;

		try {
			operations = accountManager.findOperationsSince(customer, lastMonth);
		} catch (DatabaseException de) {
			throw new CustomerAccountException("Could not compute balance", de);
		}

		double balance = 0;

		// compute the balance from the operations
		for (Operation operation : operations) {
			if ("credit".equals(operation.getType())) {
				balance = balance + operation.getAmount();
			} else if ("debit".equals(operation.getType())) {
				balance = balance - operation.getAmount();
			}
		}

		return balance;
	}
}
