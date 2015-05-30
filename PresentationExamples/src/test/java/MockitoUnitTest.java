
import ch.heigvd.automatedtestsmocking.presentationexamples.TimeService;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author Simon Oulevay (simon.oulevay@heig-vd.ch)
 */
public class MockitoUnitTest {

	@Mock
	private List mockedList;
	@Mock
	private TimeService mockedTimeService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void createMockObject() {

		mockedList.add("one");
		mockedList.clear();
		mockedList.add("two");

		assertNull(mockedList.get(0));
		assertEquals(mockedList.size(), 0);
		assertFalse(mockedList.isEmpty());
	}

	@Test
	public void makeItDoSomething() {

		when(mockedList.get(0)).thenReturn("yeehaw");
		when(mockedList.size()).thenReturn(1);
		when(mockedList.isEmpty()).thenReturn(false);

		assertEquals(mockedList.get(0), "yeehaw");
		assertEquals(mockedList.size(), 1);
		assertFalse(mockedList.isEmpty());

		mockedList.clear();
		assertEquals(mockedList.size(), 1);
	}

	@Test
	public void whyUseWhen() {

		Date fixedDate = new Date(-446774400000L); // 5 nov 1955
		when(mockedTimeService.getCurrentDate()).thenReturn(fixedDate);

		assertEquals(mockedTimeService.getCurrentDate().getTime(), -446774400000L);
	}

	@Test
	public void mockingExceptions() {

		when(mockedList.get(3)).thenThrow(new IndexOutOfBoundsException());
		doThrow(new RuntimeException()).when(mockedList).clear();

		try {
			mockedList.get(3);
			fail("Excepted an IndexOutOfBoundsException to be thrown, but nothing was thrown");
		} catch (IndexOutOfBoundsException e) {
			// the exception was thrown: test is successful
		}

		try {
			mockedList.clear();
			fail("Excepted an RuntimeException to be thrown, but nothing was thrown");
		} catch (RuntimeException e) {
			// the exception was thrown: test is successful
		}
	}

	@Test
	public void mockitoMatchers() {

		when(mockedList.get(anyInt())).thenReturn("yeehaw");

		assertEquals(mockedList.get(0), "yeehaw");
		assertEquals(mockedList.get(1), "yeehaw");
		assertEquals(mockedList.get(42), "yeehaw");
	}

	@Test
	public void verifyWhatHappens() {

		mockedList.add("one");
		mockedList.clear();

		verify(mockedList).add("one");  // OK
		verify(mockedList).clear();     // OK

		// Uncomment this line to see the test fail
		//verify(mockedList).add("two");
	}

	@Test
	public void verifyNumberOfInvocations() {

		mockedList.add("twice");
		mockedList.add("twice");

		verify(mockedList, times(2)).add("twice");
		verify(mockedList, never()).clear();
	}
}
