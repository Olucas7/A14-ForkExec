package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.forkexec.hub.ws.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AddFoodToCartIT extends BaseIT {

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
	public void success() throws InvalidTextFault_Exception, InvalidUserIdFault_Exception, InvalidFoodIdFault_Exception,
			InvalidFoodQuantityFault_Exception {
		client.addFoodToCart(VALID_EMAIL, foodId1, 10);
		for (FoodOrderItem foodOrderItem : client.cartContents(VALID_EMAIL)) {
			assertEquals(foodId1.getMenuId(), foodOrderItem.getFoodId().getMenuId());
			assertEquals(foodId1.getRestaurantId(), foodOrderItem.getFoodId().getRestaurantId());
		}
	}

	@Test(expected = InvalidFoodQuantityFault_Exception.class)
	public void invalidFoodQuantity()
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		client.addFoodToCart(VALID_EMAIL, foodId1, -1);
	}

	@Test(expected = InvalidFoodIdFault_Exception.class)
	public void invalidFoodId()
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		FoodId foodId = new FoodId();
		foodId.setMenuId("19");
		foodId.setRestaurantId(RESTAURANT1);
		client.addFoodToCart(VALID_EMAIL, foodId, 10);
	}

	@Test(expected = InvalidUserIdFault_Exception.class)
	public void invalidEmail()
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		client.addFoodToCart(NO_AT_EMAIL, foodId1, 10);
	}

}