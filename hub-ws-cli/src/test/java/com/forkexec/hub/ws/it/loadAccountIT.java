package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;
import com.forkexec.hub.ws.InvalidMoneyFault_Exception;
import com.forkexec.hub.ws.InvalidCreditCardFault_Exception;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Class that tests loadAccount operation
 */

public class loadAccountIT extends BaseIT {

    @Before
    public void setUp() throws InvalidInitFault_Exception, InvalidUserIdFault_Exception {
        client.activateAccount(VALID_EMAIL);
        client.ctrlInitUserPoints(INITIAL_BALANCE);
    }

    @After
    public void tearDown() {
        client.ctrlClear();
    }

    @Test
    public void success()
            throws InvalidUserIdFault_Exception, InvalidMoneyFault_Exception, InvalidCreditCardFault_Exception {

        assertEquals(INITIAL_BALANCE, client.accountBalance(VALID_EMAIL));
        client.loadAccount(VALID_EMAIL, VALID_MONEY, VALID_NUMBER);
        assertEquals(5600, client.accountBalance(VALID_EMAIL));

    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void alreadyExistsEmailTest() throws InvalidUserIdFault_Exception {
        client.activateAccount(VALID_EMAIL);
        client.activateAccount(VALID_EMAIL);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void nullEmailTest() throws InvalidUserIdFault_Exception {
        client.activateAccount(NULL_EMAIL);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void emptyEmailTest() throws InvalidUserIdFault_Exception {
        client.activateAccount(EMPTY_EMAIL);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void noUserEmailTest() throws InvalidUserIdFault_Exception {
        client.activateAccount(NO_USER_EMAIL);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void noDomainEmailTest() throws InvalidUserIdFault_Exception {
        client.activateAccount(NO_DOMAIN_EMAIL);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void noUserNorDomainEmailTest() throws InvalidUserIdFault_Exception {
        client.activateAccount(NO_USER_DOMAIN_EMAIL);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void noAtEmailTest() throws InvalidUserIdFault_Exception {
        client.activateAccount(NO_AT_EMAIL);
    }

    @Test(expected = InvalidCreditCardFault_Exception.class)
    public void invalidNumber()
            throws InvalidMoneyFault_Exception, InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception {
        client.loadAccount(VALID_EMAIL, VALID_MONEY, INVALID_NUMBER);
    }

    @Test(expected = InvalidCreditCardFault_Exception.class)
    public void nullNumber()
            throws InvalidMoneyFault_Exception, InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception {
        client.loadAccount(VALID_EMAIL, VALID_MONEY, NULL_CREDIT_CARD);
    }

    @Test(expected = InvalidCreditCardFault_Exception.class)
    public void spacedNumber()
            throws InvalidUserIdFault_Exception, InvalidMoneyFault_Exception, InvalidCreditCardFault_Exception {
        client.loadAccount(VALID_EMAIL, VALID_MONEY, SPACED_CREDIT_CARD);
    }

    @Test(expected = InvalidCreditCardFault_Exception.class)
    public void emptyNumber()
            throws InvalidMoneyFault_Exception, InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception {
        client.loadAccount(VALID_EMAIL, VALID_MONEY, EMPTY_CREDIT_CARD);
    }

    @Test(expected = InvalidMoneyFault_Exception.class)
    public void invalidMoney()
            throws InvalidMoneyFault_Exception, InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception {
        client.loadAccount(VALID_EMAIL, INVALID_MONEY, VALID_NUMBER);
    }

    @Test(expected = InvalidMoneyFault_Exception.class)
    public void negativeMoney()
            throws InvalidMoneyFault_Exception, InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception {
        client.loadAccount(VALID_EMAIL, NEGATIVE_MONEY, VALID_NUMBER);
    }

}