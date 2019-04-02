package com.forkexec.pts.ws.it;

import static org.junit.Assert.assertEquals;

import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;

import org.junit.Test;

/**
 * Class that tests ActivateUser operation
 */
public class ActivateUserIT extends BaseIT {

	final String VALID_EMAIL = "joao.barata@tecnico.pt";
	final String INVALID_EMAIL_1 = "tecnico";

	@Test
	public void success() throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception {
		client.activateUser(VALID_EMAIL);
		assertEquals(100, client.pointsBalance(VALID_EMAIL));
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void invalidOneEmailTest() throws InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception {
		client.activateUser(INVALID_EMAIL_1);
	}
}
