package com.forkexec.hub.ws;

import java.awt.MenuItem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.jws.WebService;

import com.forkexec.hub.domain.Cart;
import com.forkexec.hub.domain.CartItem;
import com.forkexec.hub.domain.MealId;
import com.forkexec.hub.domain.Hub;
import com.forkexec.hub.domain.exceptions.EmptyCartException;
import com.forkexec.hub.domain.exceptions.InvalidCartItemIdException;
import com.forkexec.hub.domain.exceptions.InvalidUserIdException;
import com.forkexec.hub.domain.exceptions.NotEnoughPointsException;
import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;
import com.forkexec.pts.ws.cli.PointsClient;
import com.forkexec.pts.ws.cli.PointsClientException;
import com.forkexec.rst.ws.BadTextFault_Exception;
import com.forkexec.rst.ws.Menu;
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
			for (UDDIRecord p : endpointManager.getUddiNaming().listRecords("A14_Points%")) {
				PointsClient pointsClient = new PointsClient(p.getUrl(), p.getOrgName());
				pointsClient.activateUser(userId);
			}
		} catch (EmailAlreadyExistsFault_Exception | InvalidEmailFault_Exception e) {
			throwInvalidUserId("Invalid User Id");
		} catch (UDDINamingException | PointsClientException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void loadAccount(String userId, int moneyToAdd, String creditCardNumber)
			throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		// TODO Auto-generated method stub

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
			Hub.getInstance().addMenuItemToCart(userId, buildCartItemId(foodId), foodQuantity);
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
		// TODO
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
				builder.append("\nand ").append(msg);
			}
		} catch (UDDINamingException | RestaurantClientException er) {
			builder.append("\nbut ").append(er.toString());
		}
		return builder.toString();
	}

	/** Return all variables to default values. */
	@Override
	public void ctrlClear() {
	}

	/** Set variables with specific values. */
	@Override
	public void ctrlInitFood(List<FoodInit> initialFoods) throws InvalidInitFault_Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void ctrlInitUserPoints(int startPoints) throws InvalidInitFault_Exception {
		// TODO Auto-generated method stub

	}

	// Aux function ----------------------------------------------------------
	Comparator<Food> byPrepTime = Comparator.comparing(Food::getPreparationTime);
	Comparator<Food> byPrice = Comparator.comparing(Food::getPrice);

	public List<Food> searches(String description, Comparator<Food> comparator) throws InvalidTextFault_Exception {
		try {
			List<Food> foods = new ArrayList<Food>();
			for (UDDIRecord r : endpointManager.getUddiNaming().listRecords("A14_Restaurants%")) {
				RestaurantClient restaurantClient = new RestaurantClient(r.getUrl(), r.getOrgName());

				foods.addAll(restaurantClient.searchMenus(description).stream().map(menu -> {
					Food food = buildFood(menu);
					food.getId().setRestaurantId(r.getOrgName());
					return food;

				}).collect(Collectors.toList()));

				foods.sort(comparator);
			}
			return foods;
		} catch (BadTextFault_Exception e) {
			throwInvalidText("Invalid food description");
		} catch (UDDINamingException | RestaurantClientException e) {
			e.printStackTrace();
		}
		return null;
	}

	// View helpers ----------------------------------------------------------

	/** Helper to convert a domain object to a view. */
	private Food buildFood(Menu menu) {
		Food food = new Food();
		food.setId(new FoodId());
		food.getId().setMenuId(menu.getId().getId());
		food.setEntree(menu.getEntree());
		food.setPlate(menu.getPlate());
		food.setDessert(menu.getDessert());
		food.setPrice(menu.getPrice());
		food.setPreparationTime(menu.getPreparationTime());
		return food;
	}

	private MealId buildCartItemId(FoodId id) {
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

	// Exception helpers -----------------------------------------------------

	/** Helper to throw a new BadInit exception. */
	private void throwBadInitRst(final String message) throws com.forkexec.rst.ws.BadInitFault_Exception {
		com.forkexec.rst.ws.BadInitFault faultInfo = new com.forkexec.rst.ws.BadInitFault();
		faultInfo.setMessage(message);
		throw new com.forkexec.rst.ws.BadInitFault_Exception(message, faultInfo);
	}

	private void throwBadInitPts(final String message) throws com.forkexec.pts.ws.BadInitFault_Exception {
		com.forkexec.pts.ws.BadInitFault faultInfo = new com.forkexec.pts.ws.BadInitFault();
		faultInfo.setMessage(message);
		throw new com.forkexec.pts.ws.BadInitFault_Exception(message, faultInfo);
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

}
