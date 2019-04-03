package com.forkexec.pts.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.forkexec.pts.ws.BadInitFault_Exception;
import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;

import org.junit.Test;

public class PointsBalanceIT extends BaseIT {

    private final String VALID_EMAIL = "joao.barata@tecnico.pt";
	private final String NULL_EMAIL = null;
	private final String NO_AT_EMAIL = "joao.baratatecnico.pt";
	private final String EMPTY_EMAIL = "";
	private final String NO_USER_EMAIL = "@tecnico.pt";
	private final String NO_DOMAIN_EMAIL = "velhinho@";
	private final String NO_USER_DOMAIN_EMAIL = "@";
	private final int STARTPOINTS = 500;

	@Test
	public void success() throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception {
		try {
			client.ctrlInit(STARTPOINTS);
		} catch (BadInitFault_Exception e) {
			fail();
		}
		client.activateUser(VALID_EMAIL);
		assertEquals(STARTPOINTS, client.pointsBalance(VALID_EMAIL));
	}

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