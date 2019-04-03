package com.forkexec.pts.ws.it;

import static org.junit.Assert.assertEquals;

import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;

import org.junit.Test;

/**
 * Class that tests ActivateUser operation
 */
public class ActivateUserIT extends BaseIT {

	private final String NO_AT_EMAIL = null;
	private final String VALID_EMAIL = "joao.barata@tecnico.pt";
	private final String NULL_EMAIL = null;
	private final String EMPTY_EMAIL = "";
	private final String NO_USER_EMAIL = "@tecnico.pt";
	private final String NO_DOMAIN_EMAIL = "velhinho@";
	private final String NO_USER_DOMAIN_EMAIL = "@";

	@Test
	public void success() throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception {
		client.activateUser(VALID_EMAIL);
		assertEquals(100, client.pointsBalance(VALID_EMAIL));
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void nullEmailTest() throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception {
		client.activateUser(NULL_EMAIL);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void emptyEmailTest() throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception {
		client.activateUser(EMPTY_EMAIL);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void noUserEmailTest() throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception {
		client.activateUser(NO_USER_EMAIL);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void noDomainEmailTest() throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception {
		client.activateUser(NO_DOMAIN_EMAIL);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void noUserNorDomainEmailTest() throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception {
		client.activateUser(NO_USER_DOMAIN_EMAIL);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void noAtEmailTest() throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception {
		client.activateUser(NO_AT_EMAIL);
	}
}
