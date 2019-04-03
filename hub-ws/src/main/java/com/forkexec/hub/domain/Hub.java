package com.forkexec.hub.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.forkexec.hub.domain.exceptions.EmptyCartException;
import com.forkexec.hub.domain.exceptions.InvalidCartItemIdException;
import com.forkexec.hub.domain.exceptions.InvalidUserIdException;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;
import com.forkexec.pts.ws.cli.PointsClient;
import com.forkexec.pts.ws.cli.PointsClientException;
import com.forkexec.rst.ws.BadMenuIdFault_Exception;
import com.forkexec.rst.ws.Menu;
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

	private static int cartCount = 0;

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

	public void addMenuItemToCart(String userId, MealId cartItemId, int itemQuantity)
			throws InvalidUserIdException, InvalidCartItemIdException {
		checkUserId(userId);
		checkCartItemId(cartItemId);
		carts.putIfAbsent(userId, new Cart(cartCount++));
		carts.get(userId).addToCart(new CartItem(cartItemId, itemQuantity));
	}

	public void clearCart(String userId) throws InvalidUserIdException {
		checkUserId(userId);
		carts.remove(userId);
	}

	public List<CartItem> cartContents(String userId) throws InvalidUserIdException {
		checkUserId(userId);
		return carts.get(userId).getItems();
	}

	public Cart orderCart(String userId) throws InvalidUserIdException, EmptyCartException {
		checkUserId(userId);
		Cart cart = carts.get(userId);
		if (cart == null || cart.size() == 0) {
			throw new EmptyCartException();
		}

		/*
		 * Passos: ir aos restaurantes calcular o preço total do cart ir ao pontos ve se
		 * o utilizador tem saldo ir aos restaurantes encomendar as encomendas ir aos
		 * pontos descontar o saldo
		 */
		for (RestaurantClient r : connectToRestaurants()) {
			// r.orderMenu(meal, quant)
		}

		return null;
	}

	/* ------------------- VERIFICADORES ------------------- */
	private void checkCartItemId(MealId cartItemId) throws InvalidCartItemIdException {
		getMealById(cartItemId);
	}

	public Meal getMealById(MealId mealId) throws InvalidCartItemIdException {
		if (!mealId.checkValid()) {
			throw new InvalidCartItemIdException();
		}
		try {
			RestaurantClient r = connectToRestaurant(mealId.getRestaurantId());
			MenuId menuIdd = new MenuId();
			menuIdd.setId(mealId.getMealId());
			Meal meal = buildMeal(r.getMenu(menuIdd));
			meal.getId().setRestaurantId(mealId.getRestaurantId());
			return meal;
		} catch (BadMenuIdFault_Exception e) {
			throw new InvalidCartItemIdException();
		}

	}

	private void checkUserId(String userId) throws InvalidUserIdException {
		getUserBalance(userId);
	}

	public int getUserBalance(String userId) throws InvalidUserIdException {
		if (userId == null || userId == "") {
			throw new InvalidUserIdException();
		}
		try {

			int bal = 0;
			for (PointsClient p : connectToPoints()) {
				bal = p.pointsBalance(userId);
			}
			return bal;

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

	/** Helper to convert a domain object to a view. */
	private Meal buildMeal(Menu menu) {
		Meal meal = new Meal();
		meal.setId(new MealId());
		meal.getId().setMealId(menu.getId().getId());
		meal.setEntree(menu.getEntree());
		meal.setPlate(menu.getPlate());
		meal.setDessert(menu.getDessert());
		meal.setPrice(menu.getPrice());
		meal.setPreparationTime(menu.getPreparationTime());
		return meal;
	}

}
