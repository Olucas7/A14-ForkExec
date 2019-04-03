package com.forkexec.hub.domain;

import java.util.Objects;

public class CartItem { /* Same as Food or almost Menu */
	private CartItemId cartItemId;
	private int itemQuantity;

	public CartItem(CartItemId cartItemId2, int itemQuantity2) {
		cartItemId = cartItemId2;
		itemQuantity = itemQuantity2;
	}

	/**
	 * @return the cartItemId
	 */
	public CartItemId getCartItemId() {
		return cartItemId;
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
		CartItemId cartItemId_this = cartItemId;
		CartItemId cartItemId_eq = obj_eq.getCartItemId();
		return cartItemId_this.getMealId() == cartItemId_eq.getMealId()
				&& cartItemId_this.getRestaurantId() == cartItemId_eq.getRestaurantId();
	}
}