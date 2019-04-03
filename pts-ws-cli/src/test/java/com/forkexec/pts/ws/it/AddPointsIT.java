package com.forkexec.pts.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.forkexec.pts.ws.BadInitFault_Exception;
import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;
import com.forkexec.pts.ws.InvalidPointsFault_Exception;

import org.junit.Test;

public class AddPointsIT extends BaseIT {
    private final String VALID_EMAIL = "joao.barata@tecnico.pt";
    private final String NULL_EMAIL = null;
    private final String NO_AT_EMAIL = "joao.baratatecnico.pt";
    private final String EMPTY_EMAIL = "";
    private final String NO_USER_EMAIL = "@tecnico.pt";
    private final String NO_DOMAIN_EMAIL = "velhinho@";
    private final String NO_USER_DOMAIN_EMAIL = "@";
    private final int STARTPOINTS = 500;
    private final int POINTS_TO_ADD = 1200;

    public void success()
            throws InvalidEmailFault_Exception, InvalidEmailFault_Exception, EmailAlreadyExistsFault_Exception {
        try {
            client.ctrlInit(STARTPOINTS);
        } catch (BadInitFault_Exception e) {
            fail();
        }
        client.activateUser(VALID_EMAIL);
        assertEquals(STARTPOINTS, client.pointsBalance(VALID_EMAIL));
        try {
            client.addPoints(VALID_EMAIL, POINTS_TO_ADD);
        } catch (InvalidPointsFault_Exception e) {
            fail();
        }
        assertEquals(STARTPOINTS + POINTS_TO_ADD, client.pointsBalance(VALID_EMAIL));
    }
    
    @Test(expected = InvalidPointsFault_Exception.class)
	public void add0Points() throws InvalidPointsFault_Exception, InvalidEmailFault_Exception {
		client.addPoints(VALID_EMAIL, 0);
    }
    
    @Test(expected = InvalidPointsFault_Exception.class)
	public void addNegativePoints() throws InvalidPointsFault_Exception, InvalidEmailFault_Exception {
		client.addPoints(VALID_EMAIL, -1);
    }
    
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