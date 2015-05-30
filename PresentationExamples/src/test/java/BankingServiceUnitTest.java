
import ch.heigvd.automatedtestsmocking.presentationexamples.BankingService;
import ch.heigvd.automatedtestsmocking.presentationexamples.Customer;
import ch.heigvd.automatedtestsmocking.presentationexamples.CustomerAccountException;
import ch.heigvd.automatedtestsmocking.presentationexamples.CustomerAccountManager;
import ch.heigvd.automatedtestsmocking.presentationexamples.DatabaseException;
import ch.heigvd.automatedtestsmocking.presentationexamples.Operation;
import ch.heigvd.automatedtestsmocking.presentationexamples.TimeService;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * @author Simon Oulevay (simon.oulevay@heig-vd.ch)
 */
public class BankingServiceUnitTest {

	@Mock
	private TimeService timeService;
	@Mock
	private CustomerAccountManager accountManager;
	@InjectMocks
	private BankingService bankingService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testPositiveBalance() {

		when(timeService.getCurrentDate()).thenReturn(getDate("2000-01-01"));

		Customer customer = new Customer("Peter Gibbons");

		// prepare credit and debit operations that amount to 45
		List<Operation> operations = new ArrayList<>();
		operations.add(new Operation("credit", 20));
		operations.add(new Operation("debit", 30));
		operations.add(new Operation("credit", 5));
		operations.add(new Operation("credit", 50));

		when(accountManager.findOperationsSince(any(Customer.class), any(Date.class))).thenReturn(operations);

		double balance = bankingService.getMonthBalance(customer);

		// check that the account manager was called with the correct arguments
		verify(accountManager).findOperationsSince(customer, getDate("1999-12-01"));

		// check that the balance is correct
		assertEquals(45.0, balance, 0);
	}

	@Test
	public void testNegativeBalance() {

		when(timeService.getCurrentDate()).thenReturn(getDate("2001-01-01"));

		Customer customer = new Customer("Peter Gibbons");

		// prepare credit and debit operations that amount to -15
		List<Operation> operations = new ArrayList<>();
		operations.add(new Operation("credit", 20));
		operations.add(new Operation("debit", 10));
		operations.add(new Operation("credit", 5));
		operations.add(new Operation("debit", 30));

		when(accountManager.findOperationsSince(any(Customer.class), any(Date.class))).thenReturn(operations);

		double balance = bankingService.getMonthBalance(customer);

		// check that the account manager was called with the correct arguments
		verify(accountManager).findOperationsSince(customer, getDate("2000-12-01"));

		// check that the balance is correct
		assertEquals(-15.0, balance, 0);
	}

	@Test
	public void testZeroBalanceWithNoOperations() {

		when(timeService.getCurrentDate()).thenReturn(getDate("2010-05-05"));

		Customer customer = new Customer("Peter Gibbons");

		// no operations last month
		List<Operation> operations = new ArrayList<>();

		when(accountManager.findOperationsSince(any(Customer.class), any(Date.class))).thenReturn(operations);

		double balance = bankingService.getMonthBalance(customer);

		// check that the account manager was called with the correct arguments
		verify(accountManager).findOperationsSince(customer, getDate("2010-04-05"));

		// check that the balance is correct
		assertEquals(0.0, balance, 0);
	}

	@Test
	public void testZeroBalanceWithOperations() {

		when(timeService.getCurrentDate()).thenReturn(getDate("2000-01-01"));

		Customer customer = new Customer("Peter Gibbons");

		// prepare credit and debit operations that amount to 0
		List<Operation> operations = new ArrayList<>();
		operations.add(new Operation("credit", 20));
		operations.add(new Operation("debit", 30));
		operations.add(new Operation("credit", 20));
		operations.add(new Operation("debit", 10));

		when(accountManager.findOperationsSince(any(Customer.class), any(Date.class))).thenReturn(operations);

		double balance = bankingService.getMonthBalance(customer);

		// check that the account manager was called with the correct arguments
		verify(accountManager).findOperationsSince(customer, getDate("1999-12-01"));

		// check that the balance is correct
		assertEquals(0.0, balance, 0);
	}

	@Test
	public void testDatabaseError() {

		when(timeService.getCurrentDate()).thenReturn(getDate("2000-01-01"));

		Customer customer = new Customer("Peter Gibbons");

		DatabaseException databaseException = new DatabaseException("bug");
		when(accountManager.findOperationsSince(any(Customer.class), any(Date.class))).thenThrow(databaseException);

		try {
			bankingService.getMonthBalance(customer);
			fail("Expected CustomerAccountException to be thrown");
		} catch (CustomerAccountException e) {
			assertEquals("Could not compute balance", e.getMessage());
			assertEquals(databaseException, e.getCause());
		}

		// check that the account manager was called with the correct arguments
		verify(accountManager).findOperationsSince(customer, getDate("1999-12-01"));
	}

	private static Date getDate(String date) {

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		try {
			return format.parse(date);
		} catch (ParseException ex) {
			throw new IllegalArgumentException("Invalid date " + date);
		}
	}
}
