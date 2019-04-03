package com.forkexec.pts.ws.it;

import static org.junit.Assert.assertEquals;

import com.forkexec.pts.ws.BadInitFault_Exception;
import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;
import com.forkexec.pts.ws.InvalidPointsFault_Exception;
import com.forkexec.pts.ws.NotEnoughBalanceFault_Exception;

import org.junit.Test;

public class SpendPointsIT extends BaseIT {
    private final String VALID_EMAIL_1 = "joao.barata@tecnico.pt";
    private final String VALID_EMAIL_2 = "francisco.sousa@tecnico.pt";
    private final String NULL_EMAIL = null;
    private final String NO_AT_EMAIL = "joao.baratatecnico.pt";
    private final String EMPTY_EMAIL = "";
    private final String NO_USER_EMAIL = "@tecnico.pt";
    private final String NO_DOMAIN_EMAIL = "velhinho@";
    private final String NO_USER_DOMAIN_EMAIL = "@";
    private final int STARTPOINTS = 500;
    private final int POINTS_TO_SPEND = 400;

    public void success() throws BadInitFault_Exception, EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception,
            InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
        client.ctrlInit(STARTPOINTS);
        client.activateUser(VALID_EMAIL_1);
        assertEquals(STARTPOINTS, client.pointsBalance(VALID_EMAIL_1));
        client.spendPoints(VALID_EMAIL_1, POINTS_TO_SPEND);
        assertEquals(STARTPOINTS - POINTS_TO_SPEND, client.pointsBalance(VALID_EMAIL_1));
    }

    @Test(expected = InvalidPointsFault_Exception.class)
	public void spend0Points()
            throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints(VALID_EMAIL_1, 0);
    }
    
    @Test(expected = InvalidPointsFault_Exception.class)
	public void spendNegativePoints()
            throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints(VALID_EMAIL_1, -1);
    }

    @Test(expected = NotEnoughBalanceFault_Exception.class)
	public void spendTooMuchPoints()
            throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception,
            BadInitFault_Exception, EmailAlreadyExistsFault_Exception {
        client.ctrlInit(STARTPOINTS);
        client.activateUser(VALID_EMAIL_2);
        assertEquals(STARTPOINTS, client.pointsBalance(VALID_EMAIL_2));
        client.spendPoints(VALID_EMAIL_2, STARTPOINTS + POINTS_TO_SPEND);
    }

    @Test(expected = InvalidEmailFault_Exception.class)
	public void nullEmailTest()
            throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints(NULL_EMAIL, POINTS_TO_SPEND);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void emptyEmailTest()
            throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints(EMPTY_EMAIL, POINTS_TO_SPEND);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void noUserEmailTest()
            throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints(NO_USER_EMAIL, POINTS_TO_SPEND);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void noDomainEmailTest()
            throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints(NO_DOMAIN_EMAIL, POINTS_TO_SPEND);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void noUserNorDomainEmailTest()
            throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints(NO_USER_DOMAIN_EMAIL, POINTS_TO_SPEND);
	}

	@Test(expected = InvalidEmailFault_Exception.class)
	public void noAtEmailTest()
            throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		client.spendPoints(NO_AT_EMAIL, POINTS_TO_SPEND);
	}

}