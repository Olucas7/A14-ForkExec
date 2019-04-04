package com.forkexec.hub.domain;

import java.util.Objects;

public class CartItem { /* Same as Food or almost Menu */
	private MealId mealId;
	private int itemQuantity;

	public CartItem(MealId mealId, int itemQuantity) {
		this.mealId = mealId;
		this.itemQuantity = itemQuantity;
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
		return obj_eq.getMealId() == this.mealId;
	}

	/**
	 * @param mealId the mealId to set
	 */
	public void setMealId(MealId mealId) {
		this.mealId = mealId;
	}

	/**
	 * @param itemQuantity the itemQuantity to set
	 */
	public void setItemQuantity(int itemQuantity) {
		this.itemQuantity = itemQuantity;
	}
}