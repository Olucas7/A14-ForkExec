package com.forkexec.pts.ws.it;

import static org.junit.Assert.assertEquals;

import com.forkexec.pts.ws.BadInitFault_Exception;
import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;

import org.junit.Test;

/**
 * Class that tests ActivateUser operation
 */
public class ActivateUserIT extends BaseIT {

	@Test
	public void success()
			throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception, BadInitFault_Exception {
		client.ctrlInit(STARTPOINTS);

		client.activateUser(VALID_EMAIL_1);
		assertEquals(STARTPOINTS, client.pointsBalance(VALID_EMAIL_1));
	}

	// Testing emails
	// -------------------------------------------------------------------------
	@Test(expected = EmailAlreadyExistsFault_Exception.class)
	public void alreadyExistsEmailTest() throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception {
		client.activateUser(VALID_EMAIL_1);
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
