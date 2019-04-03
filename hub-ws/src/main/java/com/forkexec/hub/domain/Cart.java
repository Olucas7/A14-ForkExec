package com.forkexec.hub.domain;

import java.util.List;
import java.util.Objects;

public class Cart {
	private List<CartItem> items;

	public void addToCart(CartItem cartItem) {
		int index = items.indexOf(cartItem);
		if (index != -1) {
			items.get(index).addQuantity(cartItem.getItemQuantity());
		} else {
			items.add(cartItem);
		}

	}

	/**
	 * @return the items
	 */
	public List<CartItem> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<CartItem> items) {
		this.items = items;
	}

}