package com.forkexec.hub.domain;

import java.util.List;
import java.util.Objects;

public class Cart {
	private String id;
	private List<CartItem> items;

	public Cart(int id) {
		this.id = Integer.toString(id);
	}

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

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public int size() {
		return items.size();
	}

}