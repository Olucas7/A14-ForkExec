package com.forkexec.hub.domain;

import java.util.Objects;

public class CartItemId { /* Same as FoodId or almost MenuId */

	private String restaurantId;
	private String menuId;

	/**
	 * @param restaurantId the restaurantId to set
	 */
	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}

	/**
	 * @param menuId the menuId to set
	 */
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	/**
	 * @return the menuId
	 */
	public String getMenuId() {
		return menuId;
	}

	/**
	 * @return the restaurantId
	 */
	public String getRestaurantId() {
		return restaurantId;
	}

	public boolean checkValid() {
		return restaurantId != null && restaurantId != "" && menuId != null && menuId != "";
	}

}