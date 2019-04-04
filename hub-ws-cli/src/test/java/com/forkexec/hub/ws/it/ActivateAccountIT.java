package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Class that tests activateAccount operation
 */
public class ActivateAccountIT extends BaseIT {
	private final String VALID_EMAIL = "joao.barata@tecnico.pt";
	private final String NULL_EMAIL = null;
	private final String NO_AT_EMAIL = "joao.baratatecnico.pt";
	private final String EMPTY_EMAIL = "";
	private final String NO_USER_EMAIL = "@tecnico.pt";
	private final String NO_DOMAIN_EMAIL = "velhinho@";
	private final String NO_USER_DOMAIN_EMAIL = "@";
	private final int INITIAL_BALANCE = 100;

	@Before
	public void setupTest() throws InvalidInitFault_Exception {
		client.ctrlInitUserPoints(INITIAL_BALANCE);
	}

	@After
	public void clean() {
		client.ctrlClear();
	}

	@Test
	public void success() throws InvalidUserIdFault_Exception {
		client.activateAccount(VALID_EMAIL);
		assertEquals(INITIAL_BALANCE, client.accountBalance(VALID_EMAIL));
	}

	/* Testing emails */
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
}
