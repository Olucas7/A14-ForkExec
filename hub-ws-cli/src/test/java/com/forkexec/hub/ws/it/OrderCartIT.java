package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.forkexec.hub.ws.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrderCartIT extends BaseIT {

	@Before
	public void setUp() throws InvalidInitFault_Exception, InvalidUserIdFault_Exception {
		createFoods();
		client.ctrlInitFood(initialFoods);
		client.ctrlInitUserPoints(INITIAL_BALANCE);
		client.activateAccount(VALID_EMAIL);
	}

	@After
	public void tearDown() {
		client.ctrlClear();
	}

	@Test
	public void success() throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception,
			InvalidUserIdFault_Exception, EmptyCartFault_Exception, NotEnoughPointsFault_Exception {
		// client.addFoodToCart(VALID_EMAIL, foodId1, 1);
		// client.addFoodToCart(VALID_EMAIL, foodId2, 1);
		// client.orderCart(VALID_EMAIL);
		// assertEquals(2, foodOrderItems.size());
	}

}