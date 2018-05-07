package creditcard_eventsourcing.model;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.requirementsascode.TestModelRunner;

public class CreditCardModelRunnerTest {
    private CreditCardModelRunner cardModelRunner;
    private CreditCard creditCard;

    @Before
    public void setUp() throws Exception {
        creditCard = new CreditCard(UUID.randomUUID());
	cardModelRunner = new CreditCardModelRunner(creditCard, new TestModelRunner());
    }

    @Test
    public void assigningLimitOnceWorksCorrectly() {
	cardModelRunner.requestToAssignLimit(BigDecimal.TEN);
	assertEquals(BigDecimal.TEN, creditCard.availableLimit());
    }
    
    @Test
    public void assigningLimitOnceCreatesOneEvent() {
	cardModelRunner.requestToAssignLimit(BigDecimal.TEN);
	assertEquals(1, creditCard.getPendingEvents().size());
    }
    
    @Test(expected = IllegalStateException.class)
    public void assigningLimitTwiceThrowsException() {
	cardModelRunner.requestToAssignLimit(BigDecimal.TEN);
	cardModelRunner.requestToAssignLimit(BigDecimal.TEN);
    }
    
    @Test
    public void withdrawingOnceWorksCorrectly() {
	cardModelRunner.requestToAssignLimit(BigDecimal.ONE);
	cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	assertEquals(BigDecimal.ZERO, creditCard.availableLimit());
    }
    
    @Test
    public void assigningAndWithdrawingCreatesTwoEvents() {
	cardModelRunner.requestToAssignLimit(BigDecimal.ONE);
	cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	assertEquals(2, creditCard.getPendingEvents().size());
    }
    
    @Test(expected = IllegalStateException.class)
    public void assigningAndWithdrawingAndAssigningThrowsException() {
	cardModelRunner.requestToAssignLimit(BigDecimal.ONE);
	cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	cardModelRunner.requestToAssignLimit(BigDecimal.ONE);
    }
    
    @Test(expected = IllegalStateException.class)
    public void withdrawingTooMuchThrowsException() {
	cardModelRunner.requestToAssignLimit(BigDecimal.ONE);
	cardModelRunner.requestWithdrawal(new BigDecimal(2));
    }
    
    @Test(expected = IllegalStateException.class)
    public void withdrawingTooOftenThrowsException() {
	cardModelRunner.requestToAssignLimit(new BigDecimal(100));

	for (int i = 1; i <= 90; i++) {
	    cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	}
    }
    
    @Test
    public void withdrawingOftenWorksWhenCycleIsClosed() {
	cardModelRunner.requestToAssignLimit(new BigDecimal(100));
	
	for (int i = 1; i <= 44; i++) {
	    cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	}
	
	cardModelRunner.requestToCloseCycle();

	for (int i = 1; i <= 44; i++) {
	    cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	}
    }
    
    @Test
    public void repayingOnceWorksCorrectly() {
	cardModelRunner.requestToAssignLimit(BigDecimal.TEN);
	cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	cardModelRunner.requestRepay(BigDecimal.ONE);
	assertEquals(BigDecimal.TEN, creditCard.availableLimit());
    }
    
    @Test
    public void repayingTwiceWorksCorrectly() {
	cardModelRunner.requestToAssignLimit(BigDecimal.TEN);
	cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	cardModelRunner.requestRepay(BigDecimal.ONE);
	cardModelRunner.requestRepay(BigDecimal.ONE);
	assertEquals(new BigDecimal(11), creditCard.availableLimit());
    }
    
    @Test
    public void assigningWithdrawingAndRepayingCreatesThreeEvents() {
	cardModelRunner.requestToAssignLimit(BigDecimal.TEN);
	cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	cardModelRunner.requestRepay(BigDecimal.ONE);
	assertEquals(3, creditCard.getPendingEvents().size());
    }
    
    @Test
    public void withdrawingWorksAfterRepaying() {
	cardModelRunner.requestToAssignLimit(BigDecimal.TEN);
	cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	cardModelRunner.requestRepay(BigDecimal.ONE);
	cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	cardModelRunner.requestWithdrawal(BigDecimal.ONE);
	assertEquals(new BigDecimal(8), creditCard.availableLimit());
    }
}
