package com.forkexec.hub.domain;

import java.util.Objects;

public class CartItem { /* Same as Food or almost Menu */
	private MealId mealId;
	private int itemQuantity;

	public CartItem(MealId mealId2, int itemQuantity2) {
		mealId = mealId2;
		itemQuantity = itemQuantity2;
	}

	/**
	 * @return the cartItemId
	 */
	public MealId getMealId() {
		return mealId;
	}

	/**
	 * @return the itemQuantity
	 */
	public int getItemQuantity() {
		return itemQuantity;
	}

	public void addQuantity(int deltaItemQuantity) {
		itemQuantity += deltaItemQuantity;
	}

	@Override
	public boolean equals(Object obj) {
		CartItem obj_eq = (CartItem) obj;
		MealId cartItemId_this = mealId;
		MealId cartItemId_eq = obj_eq.getMealId();
		return cartItemId_this.getMealId() == cartItemId_eq.getMealId()
				&& cartItemId_this.getRestaurantId() == cartItemId_eq.getRestaurantId();
	}
}