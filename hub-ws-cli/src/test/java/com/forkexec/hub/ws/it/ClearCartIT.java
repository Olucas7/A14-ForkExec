package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.forkexec.hub.ws.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ClearCartIT extends BaseIT {

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
	public void success()
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		client.addFoodToCart(VALID_EMAIL, foodId1, 10);
		for (FoodOrderItem foodOrderItem : client.cartContents(VALID_EMAIL)) {
			assertEquals(foodId1.getMenuId(), foodOrderItem.getFoodId().getMenuId());
			assertEquals(foodId1.getRestaurantId(), foodOrderItem.getFoodId().getRestaurantId());
		}
		client.clearCart(VALID_EMAIL);
		assertEquals(0, client.cartContents(VALID_EMAIL).size());
	}

	@Test(expected = InvalidUserIdFault_Exception.class)
	public void invalidUser()
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		client.addFoodToCart(NO_DOMAIN_EMAIL, foodId1, 5);
	}

}