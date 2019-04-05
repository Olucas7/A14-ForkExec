package pt.ulisboa.tecnico.sdis.ws.it;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ValidateNumberIT extends BaseIT {
    @Test
    public void success() {
        assertTrue(client.validateNumber(VALID_NUMBER));
    }

    @Test
    public void invalidNumber() {
        assertFalse(client.validateNumber(INVALID_NUMBER));
    }

    @Test
    public void nullNumber() {
        assertFalse(client.validateNumber(NULL_CREDIT_CARD));
    }

    @Test
    public void spacedNumber() {
        assertFalse(client.validateNumber(SPACED_CREDIT_CARD));
    }

    @Test
    public void emptyNumber() {
        assertFalse(client.validateNumber(EMPTY_CREDIT_CARD));
    }
}