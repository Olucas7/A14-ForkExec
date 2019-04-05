package com.forkexec.pts.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.forkexec.pts.ws.BadInitFault_Exception;
import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;

import org.junit.Test;

public class PointsBalanceIT extends BaseIT {

	@Test
	public void success()
			throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception, BadInitFault_Exception {

		client.ctrlInit(STARTPOINTS);

		client.activateUser(VALID_EMAIL_1);
		assertEquals(STARTPOINTS, client.pointsBalance(VALID_EMAIL_1));
	}

	// Testing emails
	// -------------------------------------------------------------------------
	@Test(expected = InvalidEmailFault_Exception.class)
	public void nullEmailTest() throws InvalidEmailFault_Exception {
		client.pointsBalance(NULL_EMAIL);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void emptyEmailTest() throws InvalidEmailFault_Exception {
		client.pointsBalance(EMPTY_EMAIL);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void noUserEmailTest() throws InvalidEmailFault_Exception {
		client.pointsBalance(NO_USER_EMAIL);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void noDomainEmailTest() throws InvalidEmailFault_Exception {
		client.pointsBalance(NO_DOMAIN_EMAIL);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void noUserNorDomainEmailTest() throws InvalidEmailFault_Exception {
		client.pointsBalance(NO_USER_DOMAIN_EMAIL);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void noAtEmailTest() throws InvalidEmailFault_Exception {
		client.pointsBalance(NO_AT_EMAIL);
	}
}