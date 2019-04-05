package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AccountBalanceIT extends BaseIT {
    @Before
    public void setUp() throws InvalidInitFault_Exception, InvalidUserIdFault_Exception {
        client.ctrlInitUserPoints(INITIAL_BALANCE);
        client.activateAccount(VALID_EMAIL);
    }

    @After
    public void tearDown() {
        client.ctrlClear();
    }

    @Test
    public void success() throws InvalidUserIdFault_Exception {
        assertEquals(INITIAL_BALANCE, client.accountBalance(VALID_EMAIL));
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void nullEmailTest() throws InvalidUserIdFault_Exception {
        client.accountBalance(NULL_EMAIL);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void emptyEmailTest() throws InvalidUserIdFault_Exception {
        client.accountBalance(EMPTY_EMAIL);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void noUserEmailTest() throws InvalidUserIdFault_Exception {
        client.accountBalance(NO_USER_EMAIL);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void noDomainEmailTest() throws InvalidUserIdFault_Exception {
        client.accountBalance(NO_DOMAIN_EMAIL);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void noUserNorDomainEmailTest() throws InvalidUserIdFault_Exception {
        client.accountBalance(NO_USER_DOMAIN_EMAIL);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void noAtEmailTest() throws InvalidUserIdFault_Exception {
        client.accountBalance(NO_AT_EMAIL);
    }

}