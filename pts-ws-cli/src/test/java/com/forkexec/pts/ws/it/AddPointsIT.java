package com.forkexec.pts.ws.it;

import static org.junit.Assert.assertEquals;

import com.forkexec.pts.ws.BadInitFault_Exception;
import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;
import com.forkexec.pts.ws.InvalidPointsFault_Exception;

import org.junit.Test;

public class AddPointsIT extends BaseIT {

	public void success() throws InvalidEmailFault_Exception, InvalidEmailFault_Exception,
			EmailAlreadyExistsFault_Exception, BadInitFault_Exception, InvalidPointsFault_Exception {

		client.ctrlInit(STARTPOINTS);

		client.activateUser(VALID_EMAIL_1);
		assertEquals(STARTPOINTS, client.pointsBalance(VALID_EMAIL_1));

		client.addPoints(VALID_EMAIL_1, POINTS_TO_ADD);
		assertEquals(STARTPOINTS + POINTS_TO_ADD, client.pointsBalance(VALID_EMAIL_1));
	}

	// Testing points
	// -------------------------------------------------------------------------
	@Test(expected = InvalidPointsFault_Exception.class)
	public void add0Points() throws InvalidPointsFault_Exception, InvalidEmailFault_Exception {
		client.addPoints(VALID_EMAIL_1, 0);
	}

	@Test(expected = InvalidPointsFault_Exception.class)
	public void addNegativePoints() throws InvalidPointsFault_Exception, InvalidEmailFault_Exception {
		client.addPoints(VALID_EMAIL_1, -1);
	}

	// Testing emails
	// -------------------------------------------------------------------------
	@Test(expected = InvalidEmailFault_Exception.class)
	public void nullEmailTest() throws InvalidPointsFault_Exception, InvalidEmailFault_Exception {
		client.addPoints(NULL_EMAIL, POINTS_TO_ADD);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void emptyEmailTest() throws InvalidPointsFault_Exception, InvalidEmailFault_Exception {
		client.addPoints(EMPTY_EMAIL, POINTS_TO_ADD);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void noUserEmailTest() throws InvalidPointsFault_Exception, InvalidEmailFault_Exception {
		client.addPoints(NO_USER_EMAIL, POINTS_TO_ADD);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void noDomainEmailTest() throws InvalidPointsFault_Exception, InvalidEmailFault_Exception {
		client.addPoints(NO_DOMAIN_EMAIL, POINTS_TO_ADD);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void noUserNorDomainEmailTest() throws InvalidPointsFault_Exception, InvalidEmailFault_Exception {
		client.addPoints(NO_USER_DOMAIN_EMAIL, POINTS_TO_ADD);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void noAtEmailTest() throws InvalidPointsFault_Exception, InvalidEmailFault_Exception {
		client.addPoints(NO_AT_EMAIL, POINTS_TO_ADD);
	}

}