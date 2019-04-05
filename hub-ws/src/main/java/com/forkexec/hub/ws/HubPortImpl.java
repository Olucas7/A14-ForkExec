package com.forkexec.hub.ws;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.jws.WebService;

import com.forkexec.hub.domain.Cart;
import com.forkexec.hub.domain.CartItem;
import com.forkexec.hub.domain.Hub;
import com.forkexec.hub.domain.Meal;
import com.forkexec.hub.domain.MealId;
import com.forkexec.hub.domain.exceptions.EmptyCartException;
import com.forkexec.hub.domain.exceptions.InvalidCardNumberException;
import com.forkexec.hub.domain.exceptions.InvalidCartItemIdException;
import com.forkexec.hub.domain.exceptions.InvalidPointsException;
import com.forkexec.hub.domain.exceptions.InvalidTextException;
import com.forkexec.hub.domain.exceptions.InvalidUserIdException;
import com.forkexec.hub.domain.exceptions.NotEnoughPointsException;
import com.forkexec.pts.ws.cli.PointsClient;
import com.forkexec.pts.ws.cli.PointsClientException;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.MenuInit;
import com.forkexec.rst.ws.cli.RestaurantClient;
import com.forkexec.rst.ws.cli.RestaurantClientException;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDIRecord;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.hub.ws.HubPortType", wsdlLocation = "HubService.wsdl", name = "HubWebService", portName = "HubPort", targetNamespace = "http://ws.hub.forkexec.com/", serviceName = "HubService")
public class HubPortImpl implements HubPortType {

	/**
	 * The Endpoint manager controls the Web Service instance during its whole
	 * lifecycle.
	 */
	private HubEndpointManager endpointManager;

	/** Constructor receives a reference to the endpoint manager. */
	public HubPortImpl(HubEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}

	// Main operations -------------------------------------------------------

	@Override
	public void activateAccount(String userId) throws InvalidUserIdFault_Exception {
		try {
			Hub.getInstance().activateAccount(userId);
		} catch (InvalidUserIdException e) {
			throwInvalidUserId(e.getMessage());
		}

	}

	@Override
	public void loadAccount(String userId, int moneyToAdd, String creditCardNumber)
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		try {
			Hub.getInstance().chargeAccount(userId, moneyToAdd, creditCardNumber);
		} catch (InvalidUserIdException e) {
			throwInvalidUserId(e.getMessage());
		} catch (InvalidPointsException e) {
			throwInvalidMoney(e.getMessage());
		} catch (InvalidCardNumberException e) {
			throwInvalidCreditCard(e.getMessage());
		}
	}

	@Override
	public List<Food> searchDeal(String description) throws InvalidTextFault_Exception {
		return searches(description, byPrice);
	}

	@Override
	public List<Food> searchHungry(String description) throws InvalidTextFault_Exception {
		return searches(description, byPrepTime);
	}

	@Override
	public void addFoodToCart(String userId, FoodId foodId, int foodQuantity)
			throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		if (foodQuantity <= 0)
			throwInvalidFoodQuantity("Invalid Quantity");
		try {
			Hub.getInstance().addMenuItemToCart(userId, buildMealId(foodId), foodQuantity);
		} catch (InvalidUserIdException e) {
			throwInvalidUserId(e.getMessage());
		} catch (InvalidCartItemIdException e) {
			throwInvalidFoodId(e.getMessage());
		}
	}

	@Override
	public void clearCart(String userId) throws InvalidUserIdFault_Exception {
		try {
			Hub.getInstance().clearCart(userId);
		} catch (InvalidUserIdException e) {
			throwInvalidUserId(e.getMessage());
		}
	}

	@Override
	public FoodOrder orderCart(String userId)
			throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception {

		try {
			Cart order = Hub.getInstance().orderCart(userId);
			return buildFoodOrder(order);
		} catch (InvalidUserIdException e) {
			throwInvalidUserId(e.getMessage());
		} catch (NotEnoughPointsException e) {
			throwNotEnoughPoints(e.getMessage());
		} catch (EmptyCartException e) {
			throwEmptyCart(e.getMessage());
		}
		return null;
	}

	@Override
	public int accountBalance(String userId) throws InvalidUserIdFault_Exception {
		try {
			return Hub.getInstance().getUserBalance(userId);
		} catch (InvalidUserIdException e) {
			throwInvalidUserId(e.getMessage());
		}
		return 0;
	}

	@Override
	public Food getFood(FoodId foodId) throws InvalidFoodIdFault_Exception {
		MealId mealId = buildMealId(foodId);
		try {
			Meal meal = Hub.getInstance().getMeal(mealId);
			return buildFood(meal);
		} catch (InvalidTextException e) {
			throwInvalidFoodId(e.getMessage());
		}
		return null;
	}

	@Override
	public List<FoodOrderItem> cartContents(String userId) throws InvalidUserIdFault_Exception {
		try {
			List<CartItem> order = Hub.getInstance().cartContents(userId);
			List<FoodOrderItem> foodorder = new ArrayList<FoodOrderItem>();
			foodorder.addAll(order.stream().map(cartitem -> {
				return buildFoodOrderItem(cartitem);

			}).collect(Collectors.toList()));
			return foodorder;
		} catch (InvalidUserIdException e) {
			throwInvalidUserId(e.getMessage());
		}
		return null;
	}

	// Control operations ----------------------------------------------------

	/** Diagnostic operation to check if service is running. */
	@Override
	public String ctrlPing(String inputMessage) {
		// If no input is received, return a default name.
		if (inputMessage == null || inputMessage.trim().length() == 0)
			inputMessage = "friend";

		// If the service does not have a name, return a default.
		String wsName = endpointManager.getWsName();
		if (wsName == null || wsName.trim().length() == 0)
			wsName = "Hub";

		// Build a string with a message to return.
		StringBuilder builder = new StringBuilder();
		builder.append("Hello ").append(inputMessage);
		builder.append(" from ").append(wsName);
		try {
			for (UDDIRecord e : endpointManager.getUddiNaming().listRecords("A14_Restaurant%")) {
				RestaurantClient client = new RestaurantClient(e.getUrl(), e.getOrgName());
				String msg = client.ctrlPing(inputMessage);
				builder.append("\nAND ").append(msg);
			}
		} catch (UDDINamingException | RestaurantClientException er) {
			builder.append("\nbut ").append(er.toString());
		}
		return builder.toString();
	}

	/** Return all variables to default values. */
	@Override
	public void ctrlClear() {
		try {
			for (UDDIRecord e : endpointManager.getUddiNaming().listRecords("A14_Restaurant%")) {
				new RestaurantClient(e.getUrl(), e.getOrgName()).ctrlClear();
			}
			for (UDDIRecord e : endpointManager.getUddiNaming().listRecords("A14_Points%")) {
				new PointsClient(e.getUrl(), e.getOrgName()).ctrlClear();
			}
		} catch (RestaurantClientException | UDDINamingException | PointsClientException e1) {
			throw new RuntimeException();
		}
		Hub.getInstance().reset();
	}

	/** Set variables with specific values. */
	@Override
	public void ctrlInitFood(List<FoodInit> initialFoods) throws InvalidInitFault_Exception {
		List<MenuInit> menus = new ArrayList<MenuInit>();
		try {
			for (UDDIRecord e : endpointManager.getUddiNaming().listRecords("A14_Restaurant%")) {
				List<FoodInit> toSendFoods = new ArrayList<FoodInit>();
				for (FoodInit fo : initialFoods) {
					if (fo.getFood().getId().getRestaurantId().equals(e.getOrgName())) {
						toSendFoods.add(fo);
					}
				}
				RestaurantClient client = new RestaurantClient(e.getUrl(), e.getOrgName());
				menus = buildListOfMenuInit(toSendFoods);
				if (menus.size() != 0)
					client.ctrlInit(menus);
			}
		} catch (UDDINamingException | RestaurantClientException e) {
			throw new RuntimeException();
		} catch (com.forkexec.rst.ws.BadInitFault_Exception e) {
			throwInvalidInit(e.getMessage());
		}
	}

	private List<MenuInit> buildListOfMenuInit(List<FoodInit> initialFoods) throws InvalidInitFault_Exception {
		List<MenuInit> menus = new ArrayList<MenuInit>();
		for (FoodInit food_info : initialFoods) {
			if (!isValidFoodInit(food_info))
				throwInvalidInit("invalid food init");
			MenuInit m = buildMenuInit(food_info);
			menus.add(m);
		}
		return menus;
	}

	@Override
	public void ctrlInitUserPoints(int startPoints) throws InvalidInitFault_Exception {
		try {
			for (UDDIRecord e : endpointManager.getUddiNaming().listRecords("A14_Points%")) {
				PointsClient client = new PointsClient(e.getUrl(), e.getOrgName());
				client.ctrlInit(startPoints);
			}
		} catch (UDDINamingException | PointsClientException e) {
			throw new RuntimeException();
		} catch (com.forkexec.pts.ws.BadInitFault_Exception e) {
			throwInvalidInit(e.getMessage());
		}
	}

	// Aux function ----------------------------------------------------------
	Comparator<Food> byPrepTime = Comparator.comparing(Food::getPreparationTime);
	Comparator<Food> byPrice = Comparator.comparing(Food::getPrice);

	public List<Food> searches(String description, Comparator<Food> comparator) throws InvalidTextFault_Exception {

		try {
			List<Food> foods = new ArrayList<Food>();

			foods.addAll(Hub.getInstance().searchMeals(description).stream().map(meal -> {
				Food food = buildFood(meal);
				return food;

			}).collect(Collectors.toList()));

			foods.sort(comparator);

			return foods;
		} catch (InvalidTextException e) {
			throwInvalidText(e.getMessage());
		}
		return null;
	}

	// Checkers --------------------------------------------------------------
	private boolean isValidFoodInit(FoodInit food_info) {
		return food_info.getQuantity() > 0 && isValidFood(food_info.getFood());
	}

	private boolean isValidFood(Food food) {
		return food.getPrice() > 0 && food.getPreparationTime() > 0 && validString(food.getEntree())
				&& validString(food.getPlate()) && validString(food.getDessert());
	}

	public boolean validString(String message) {
		if (message == null || message.trim().length() == 0)
			return false;

		String regex = "\\s";
		Pattern pattern = Pattern.compile(regex);
		Matcher mat = pattern.matcher(message);
		if (mat.matches()) // contains white spaces
			return false;
		return true;
	}

	// View helpers ----------------------------------------------------------
	/** Helpers to convert a domain object to a view. */

	private MealId buildMealId(FoodId id) {
		MealId cid = new MealId();
		cid.setRestaurantId(id.getRestaurantId());
		cid.setMealId(id.getMenuId());
		return cid;
	}

	private FoodOrderItem buildFoodOrderItem(CartItem cartitem) {
		FoodOrderItem food = new FoodOrderItem();
		food.setFoodId(new FoodId());
		food.getFoodId().setMenuId(cartitem.getMealId().getMealId());
		food.getFoodId().setRestaurantId(cartitem.getMealId().getRestaurantId());
		food.setFoodQuantity(cartitem.getItemQuantity());
		return food;
	}

	private FoodOrder buildFoodOrder(Cart order) {
		FoodOrderId foodOrderId = new FoodOrderId();
		foodOrderId.setId(order.getId());
		FoodOrder foodOrder = new FoodOrder();
		foodOrder.setFoodOrderId(foodOrderId);
		for (CartItem item : order.getItems()) {
			foodOrder.getItems().add(buildFoodOrderItem(item));
		}
		return foodOrder;
	}

	private FoodId buildFoodId(MealId mealId) {
		FoodId foodId = new FoodId();
		foodId.setMenuId(mealId.getMealId());
		foodId.setRestaurantId(mealId.getRestaurantId());
		return foodId;
	}

	private Food buildFood(Meal meal) {
		Food food = new Food();
		food.setId(buildFoodId(meal.getId()));
		food.setDessert(meal.getDessert());
		food.setEntree(meal.getEntree());
		food.setPlate(meal.getPlate());
		food.setPreparationTime(meal.getPreparationTime());
		food.setPrice(meal.getPrice());
		return food;
	}

	private MenuInit buildMenuInit(FoodInit food_info) {
		MenuInit m = buildMenu(food_info.getFood());
		m.setQuantity(food_info.getQuantity());
		return m;
	}

	private MenuInit buildMenu(Food food) {
		MenuInit menu_info = new MenuInit();
		Menu m = new Menu();
		MenuId menu_id = new MenuId();

		menu_id.setId(food.getId().getMenuId());
		m.setId(menu_id);
		m.setEntree(food.getEntree());
		m.setPlate(food.getPlate());
		m.setDessert(food.getDessert());
		m.setPrice(food.getPrice());
		m.setPreparationTime(food.getPreparationTime());
		menu_info.setMenu(m);
		return menu_info;
	}

	// Exception helpers -----------------------------------------------------

	private void throwInvalidInit(final String message) throws InvalidInitFault_Exception {
		InvalidInitFault faultInfo = new InvalidInitFault();
		faultInfo.setMessage(message);
		throw new InvalidInitFault_Exception(message, faultInfo);
	}

	private void throwInvalidUserId(final String message) throws InvalidUserIdFault_Exception {
		InvalidUserIdFault faultInfo = new InvalidUserIdFault();
		faultInfo.setMessage(message);
		throw new InvalidUserIdFault_Exception(message, faultInfo);
	}

	private void throwInvalidText(final String message) throws InvalidTextFault_Exception {
		InvalidTextFault faultInfo = new InvalidTextFault();
		faultInfo.setMessage(message);
		throw new InvalidTextFault_Exception(message, faultInfo);
	}

	private void throwInvalidFoodId(final String message) throws InvalidFoodIdFault_Exception {
		InvalidFoodIdFault faultInfo = new InvalidFoodIdFault();
		faultInfo.setMessage(message);
		throw new InvalidFoodIdFault_Exception(message, faultInfo);
	}

	private void throwInvalidFoodQuantity(final String message) throws InvalidFoodQuantityFault_Exception {
		InvalidFoodQuantityFault faultInfo = new InvalidFoodQuantityFault();
		faultInfo.setMessage(message);
		throw new InvalidFoodQuantityFault_Exception(message, faultInfo);
	}

	private void throwNotEnoughPoints(final String message) throws NotEnoughPointsFault_Exception {
		NotEnoughPointsFault faultInfo = new NotEnoughPointsFault();
		faultInfo.setMessage(message);
		throw new NotEnoughPointsFault_Exception(message, faultInfo);
	}

	private void throwEmptyCart(final String message) throws EmptyCartFault_Exception {
		EmptyCartFault faultInfo = new EmptyCartFault();
		faultInfo.setMessage(message);
		throw new EmptyCartFault_Exception(message, faultInfo);
	}

	private void throwInvalidCreditCard(final String message) throws InvalidCreditCardFault_Exception {
		InvalidCreditCardFault faultInfo = new InvalidCreditCardFault();
		faultInfo.setMessage(message);
		throw new InvalidCreditCardFault_Exception(message, faultInfo);
	}

	private void throwInvalidMoney(final String message) throws InvalidMoneyFault_Exception {
		InvalidMoneyFault faultInfo = new InvalidMoneyFault();
		faultInfo.setMessage(message);
		throw new InvalidMoneyFault_Exception(message, faultInfo);
	}

}
