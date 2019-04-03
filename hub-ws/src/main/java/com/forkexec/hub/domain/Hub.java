package com.forkexec.hub.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.forkexec.hub.domain.exceptions.InvalidCartItemIdException;
import com.forkexec.hub.domain.exceptions.InvalidUserIdException;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;
import com.forkexec.pts.ws.cli.PointsClient;
import com.forkexec.pts.ws.cli.PointsClientException;
import com.forkexec.rst.ws.BadMenuIdFault_Exception;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.cli.RestaurantClient;
import com.forkexec.rst.ws.cli.RestaurantClientException;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDIRecord;

/**
 * Hub
 *
 * A restaurants hub server.
 *
 */
public class Hub {

	private static Map<String, Cart> carts = new HashMap<String, Cart>();

	private static UDDINaming uddi;

	// Singleton -------------------------------------------------------------

	/** Private constructor prevents instantiation from other classes. */
	private Hub() {
		// Initialization of default values
	}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final Hub INSTANCE = new Hub();
	}

	public static synchronized Hub getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public static void setUDDINaming(UDDINaming uddiNaming) {
		uddi = uddiNaming;
	}

	public void addMenuItemToCart(String userId, CartItemId cartItemId, int itemQuantity)
			throws InvalidUserIdException, InvalidCartItemIdException {
		checkUserId(userId);
		checkCartItemId(cartItemId);
		carts.putIfAbsent(userId, new Cart());
		carts.get(userId).addToCart(new CartItem(cartItemId, itemQuantity));
	}

	/* ------------------- VERIFICADORES ------------------- */
	public void checkCartItemId(CartItemId cartItemId) throws InvalidCartItemIdException {
		if (!cartItemId.checkValid()) {
			throw new InvalidCartItemIdException();
		}
		try {
			RestaurantClient r = connectToRestaurant(cartItemId.getRestaurantId());
			MenuId menuIdd = new MenuId();
			menuIdd.setId(cartItemId.getMenuId());
			r.getMenu(menuIdd);
		} catch (BadMenuIdFault_Exception e) {
			throw new InvalidCartItemIdException();
		}

	}

	private void checkUserId(String userId) throws InvalidUserIdException {
		if (userId == null || userId == "") {
			throw new InvalidUserIdException();
		}
		try {

			for (PointsClient p : connectToPoints()) {
				p.pointsBalance(userId);
			}

		} catch (InvalidEmailFault_Exception e) {
			throw new InvalidUserIdException();
		}
	}

	/* ------------------- CONNECT TO SERVERS ------------------- */

	public List<PointsClient> connectToPoints() {
		List<PointsClient> points = new ArrayList<PointsClient>();

		try {
			for (UDDIRecord p : uddi.listRecords("A14_Points%")) {
				PointsClient point = new PointsClient(p.getUrl(), p.getOrgName());
				points.add(point);
			}
			return points;

		} catch (UDDINamingException | PointsClientException e) {
			throw new RuntimeException(e);
		}
	}

	public RestaurantClient connectToRestaurant(String restaurantName) {
		return connectToRestaurantAux(restaurantName).get(0);

	}

	public List<RestaurantClient> connectToRestaurants() {
		return connectToRestaurantAux("A14_Points%");
	}

	private List<RestaurantClient> connectToRestaurantAux(String restaurantString) {
		List<RestaurantClient> rests = new ArrayList<RestaurantClient>();

		try {
			for (UDDIRecord r : uddi.listRecords(restaurantString)) {
				RestaurantClient rest = new RestaurantClient(r.getUrl(), r.getOrgName());
				rests.add(rest);
			}
			return rests;

		} catch (UDDINamingException | RestaurantClientException e) {
			throw new RuntimeException(e);
		}
	}

}
