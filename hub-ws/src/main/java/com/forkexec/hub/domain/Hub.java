package com.forkexec.hub.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.forkexec.hub.domain.exceptions.NotEnoughPointsException;
import com.forkexec.hub.domain.exceptions.EmptyCartException;
import com.forkexec.hub.domain.exceptions.InvalidCardNumberException;
import com.forkexec.hub.domain.exceptions.InvalidCartItemIdException;
import com.forkexec.hub.domain.exceptions.InvalidPointsException;
import com.forkexec.hub.domain.exceptions.InvalidTextException;
import com.forkexec.hub.domain.exceptions.InvalidUserIdException;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;
import com.forkexec.pts.ws.InvalidPointsFault_Exception;
import com.forkexec.pts.ws.NotEnoughBalanceFault_Exception;
import com.forkexec.pts.ws.cli.PointsClient;
import com.forkexec.pts.ws.cli.PointsClientException;
import com.forkexec.rst.ws.BadMenuIdFault_Exception;
import com.forkexec.rst.ws.BadQuantityFault_Exception;
import com.forkexec.rst.ws.BadTextFault_Exception;
import com.forkexec.rst.ws.InsufficientQuantityFault_Exception;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.MenuOrder;
import com.forkexec.rst.ws.cli.RestaurantClient;
import com.forkexec.rst.ws.cli.RestaurantClientException;

import pt.ulisboa.tecnico.sdis.ws.cli.CreditCardClient;
import pt.ulisboa.tecnico.sdis.ws.cli.CreditCardClientException;
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

	public static synchronized void setUDDINaming(UDDINaming uddiNaming) {
		uddi = uddiNaming;
	}

	public synchronized void chargeAccount(String userId, int moneyToAdd, String creditCardNumber)
			throws InvalidUserIdException, InvalidPointsException, InvalidCardNumberException {

		checkUserId(userId);
		CreditCardClient creditCard = connectToCreditCard();
		if (!(creditCard.validateNumber(creditCardNumber)))
			throw new InvalidCardNumberException();
		int pointsToAdd = convertMoneyToPoints(moneyToAdd);
		for (PointsClient points : connectToPoints()) {
			try {

				points.addPoints(userId, pointsToAdd);
			} catch (InvalidEmailFault_Exception e) {
				throw new InvalidUserIdException();
			} catch (InvalidPointsFault_Exception e) {
				throw new InvalidPointsException();
			}
		}
	}

	private synchronized int convertMoneyToPoints(int moneyToAdd) throws InvalidPointsException {
		switch (moneyToAdd) {
		case 10:
			return 1000;
		case 20:
			return 2100;
		case 30:
			return 3300;
		case 50:
			return 5500;
		default:
			throw new InvalidPointsException();
		}
	}

	public synchronized void activateAccount(String userId) throws InvalidUserIdException {
		try {
			for (PointsClient pointsClient : connectToPoints()) {
				pointsClient.activateUser(userId);
			}
			carts.putIfAbsent(userId, new Cart(cartCount++));
		} catch (InvalidEmailFault_Exception e) {
			throw new InvalidUserIdException();
		}
	}

	public synchronized List<Meal> searchMeals(String description) throws InvalidTextException {
		List<Meal> meals = new ArrayList<Meal>();
		for (RestaurantClient restaurantClient : connectToRestaurants()) {
			try {
				meals.addAll(restaurantClient.searchMenus(description).stream().map(menu -> {
					Meal meal = buildMeal(menu);
					meal.getId().setRestaurantId(restaurantClient.getWsURL());
					return meal;

				}).collect(Collectors.toList()));
			} catch (BadTextFault_Exception e) {
				throw new InvalidTextException();
			}
		}
		return meals;
	}

	public synchronized Meal getMeal(MealId mealId) throws InvalidTextException {
		Menu menu;
		try {
			menu = connectToRestaurant(mealId.getRestaurantId()).getMenu(buildMenuId(mealId));
		} catch (BadMenuIdFault_Exception e) {
			throw new InvalidTextException();
		}
		return buildMeal(menu);
	}

	public synchronized void addMenuItemToCart(String userId, MealId cartItemId, int itemQuantity)
			throws InvalidUserIdException, InvalidCartItemIdException {
		checkUserId(userId);
		checkCartItemId(cartItemId);
		carts.get(userId).addToCart(new CartItem(cartItemId, itemQuantity));
	}

	public synchronized void clearCart(String userId) throws InvalidUserIdException {
		checkUserId(userId);
		carts.remove(userId);
		carts.putIfAbsent(userId, new Cart(cartCount++));
	}

	public synchronized List<CartItem> cartContents(String userId) throws InvalidUserIdException {
		checkUserId(userId);
		return carts.get(userId).getItems();
	}

	public synchronized Cart orderCart(String userId)
			throws InvalidUserIdException, NotEnoughPointsException, EmptyCartException {
		checkUserId(userId);

		Cart cart = carts.get(userId);
		Cart finalCart = new Cart(cart.getId());
		int totalPrice = 0, userBalance = 0, finalPrice = 0;
		Map<MealId, Meal> meals = new HashMap<MealId, Meal>();

		if (cart == null || cart.size() == 0) {
			throw new EmptyCartException();
		}
		/*
		 * ir aos restaurantes calcular o preço total do cart
		 */
		for (CartItem item : cart.getItems()) {
			String rest = item.getMealId().getRestaurantId();
			MenuId menuId = buildMenuId(item.getMealId());
			Menu menu = null;
			try {
				menu = connectToRestaurant(rest).getMenu(menuId);
			} catch (BadMenuIdFault_Exception e) {
				/* Impossivel acontecer mas */
				throw new RuntimeException(e);
			}
			Meal meal = buildMeal(menu);
			meals.put(meal.getId(), meal);
			totalPrice += meal.getPrice() * item.getItemQuantity();
		}

		/*
		 * ir ao pontos ve se o utilizador tem saldo
		 */

		for (PointsClient p : connectToPoints()) {
			try {
				userBalance += p.pointsBalance(userId);
			} catch (InvalidEmailFault_Exception e) {
				throw new InvalidUserIdException();
			}
		}

		if (totalPrice > userBalance) {
			throw new NotEnoughPointsException();
		}
		/*
		 * ir aos restaurantes encomendar as encomendas
		 */

		for (CartItem item : cart.getItems()) {
			String rest = item.getMealId().getRestaurantId();
			MenuId menuId = buildMenuId(item.getMealId());
			MenuOrder menuOrder = null;
			try {
				menuOrder = connectToRestaurant(rest).orderMenu(menuId, item.getItemQuantity());
			} catch (BadMenuIdFault_Exception e) {
				/* Impossivel acontecer mas */
				throw new RuntimeException(e);
			} catch (BadQuantityFault_Exception e) {
				/* Impossivel acontecer mas */
				throw new RuntimeException(e);
			} catch (InsufficientQuantityFault_Exception e) {
				continue;
			}
			/* Constroi cart */
			CartItem cartItem = buildCartItem(menuOrder);
			cartItem.getMealId().setRestaurantId(rest);
			finalCart.getItems().add(cartItem);
			/* Calcula preco */
			finalPrice += meals.get(cartItem.getMealId()).getPrice() * item.getItemQuantity();
		}
		/*
		 * ir aos pontos descontar o saldo
		 */
		for (PointsClient p : connectToPoints()) {
			try {
				p.spendPoints(userId, finalPrice);
			} catch (InvalidEmailFault_Exception e) {
				throw new InvalidUserIdException();
			} catch (InvalidPointsFault_Exception e) {
				/* Impossivel acontecer mas */
				throw new RuntimeException(e);
			} catch (NotEnoughBalanceFault_Exception e) {
				throw new NotEnoughPointsException();
			}
		}

		return finalCart;
	}

	public synchronized void reset() {
		carts.clear();
		cartCount = 0;
	}

	/* ------------------- VERIFICADORES ------------------- */
	private synchronized void checkCartItemId(MealId cartItemId) throws InvalidCartItemIdException {
		getMealById(cartItemId);
	}

	public synchronized Meal getMealById(MealId mealId) throws InvalidCartItemIdException {
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

	private synchronized void checkUserId(String userId) throws InvalidUserIdException {
		getUserBalance(userId);
	}

	public synchronized int getUserBalance(String userId) throws InvalidUserIdException {
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

	public synchronized List<PointsClient> connectToPoints() {
		List<PointsClient> points = new ArrayList<PointsClient>();

		try {
			PointsClient point = new PointsClient(uddi.getUDDIUrl(), "A14_Points");
			points.add(point);
			if (points.isEmpty())
				throw new RuntimeException();
			return points;
		} catch (PointsClientException e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized CreditCardClient connectToCreditCard() {
		try {
			CreditCardClient creditCard = new CreditCardClient("http://ws.sd.rnl.tecnico.ulisboa.pt:8080/cc");
			return creditCard;
		} catch (CreditCardClientException e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized RestaurantClient connectToRestaurant(String restaurantName) {
		return connectToRestaurantAux(restaurantName).get(0);

	}

	public synchronized List<RestaurantClient> connectToRestaurants() {
		return connectToRestaurantAux("A14_Restaurant%");
	}

	private synchronized List<RestaurantClient> connectToRestaurantAux(String restaurantString) {
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
	private synchronized Meal buildMeal(Menu menu) {
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

	private synchronized MenuId buildMenuId(MealId mealId) {
		MenuId menuId = new MenuId();
		menuId.setId(mealId.getMealId());
		return menuId;
	}

	private synchronized CartItem buildCartItem(MenuOrder menuOrder) {
		MealId mealId = new MealId();
		mealId.setMealId(menuOrder.getMenuId().getId());
		CartItem cartItem = new CartItem(mealId, menuOrder.getMenuQuantity());
		return cartItem;
	}
}
