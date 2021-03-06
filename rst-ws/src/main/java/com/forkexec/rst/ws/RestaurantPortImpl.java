package com.forkexec.rst.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jws.WebService;

import com.forkexec.rst.domain.Carte;
import com.forkexec.rst.domain.Order;
import com.forkexec.rst.domain.Restaurant;
import com.forkexec.rst.domain.Exceptions.BadMenuIdException;
import com.forkexec.rst.domain.Exceptions.BadTextException;
import com.forkexec.rst.domain.Exceptions.InsufficientQuantityException;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.rst.ws.RestaurantPortType", wsdlLocation = "RestaurantService.wsdl", name = "RestaurantWebService", portName = "RestaurantPort", targetNamespace = "http://ws.rst.forkexec.com/", serviceName = "RestaurantService")
public class RestaurantPortImpl implements RestaurantPortType {

	/**
	 * The Endpoint manager controls the Web Service instance during its whole
	 * lifecycle.
	 */
	private RestaurantEndpointManager endpointManager;

	/** Constructor receives a reference to the endpoint manager. */
	public RestaurantPortImpl(RestaurantEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}

	// Main operations -------------------------------------------------------

	@Override
	public Menu getMenu(MenuId menuId) throws BadMenuIdFault_Exception {
		if (!validString(menuId.getId())) {
			throwBadMenuId("invalid menu id");
		}
		Carte carte;
		try {
			carte = Restaurant.getInstance().getMenu(menuId.getId());
			return convertCarteToMenu(carte);
		} catch (BadMenuIdException e) {
			throwBadMenuId(e.getId());
		}
		return null;
		// checkar string do menuid?
	}

	@Override
	public List<Menu> searchMenus(String descriptionText) throws BadTextFault_Exception {
		if (!validString(descriptionText)) {
			throwBadText("invalid description");
		}
		List<Carte> cartes;
		try {
			cartes = Restaurant.getInstance().searchMenus(descriptionText);
			return convertListOfCartesToListOfMenus(cartes);
		} catch (BadTextException e) {
			throwBadText(e.getMessage());
		}
		return null;
	}

	@Override
	public MenuOrder orderMenu(MenuId arg0, int arg1)
			throws BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception {
		if (!validString(arg0.getId())) {
			throwBadMenuId("invalid menu id");
		}

		if (arg1 < 1) {
			throwBadQuantity("invalid quantity");
		}

		try {
			Order order;
			order = Restaurant.getInstance().orderMenu(arg0.getId(), arg1);
			return convertOrderToMenuOrder(order);
		} catch (BadMenuIdException e) {
			throwBadMenuId(e.getId());
		} catch (InsufficientQuantityException e) {
			throwInsufficientQuantity(e.getMessage());
		}
		return null;
	}

	// Validation operations --------------------------------------------------

	public boolean validString(String message) {
		if (message == null || message.trim().length() == 0 || message.contains(" "))
			return false;
		return true;
	}

	private boolean isValidMenuInit(MenuInit menu_info) {
		return menu_info.getQuantity() > 0 && isValidMenu(menu_info.getMenu());
	}

	private boolean isValidMenu(Menu menu) {
		return menu.getPrice() > 0 && menu.getPreparationTime() > 0 && validStringFood(menu.getEntree())
				&& validStringFood(menu.getPlate()) && validStringFood(menu.getDessert());
	}

	private boolean validStringFood(String food) {
		if (food == null || food.trim().length() == 0)
			return false;
		return true;
	}

	// Control operations ----------------------------------------------------

	/** Diagnostic operation to check if service is running. */
	@Override
	public String ctrlPing(String inputMessage) {
		// If no input is received, return a default name.
		if (inputMessage == null || inputMessage.trim().length() == 0)
			inputMessage = "friend";

		// If the park does not have a name, return a default.
		String wsName = endpointManager.getWsName();
		if (wsName == null || wsName.trim().length() == 0)
			wsName = "Restaurant";

		// Build a string with a message to return.
		StringBuilder builder = new StringBuilder();
		builder.append("Hello ").append(inputMessage);
		builder.append(" from ").append(wsName);
		for (Carte c : Restaurant.getInstance().getMenus()) {
			builder.append("\nwith ").append(c.toString());
		}
		return builder.toString();
	}

	/** Return all variables to default values. */
	@Override
	public void ctrlClear() {
		Restaurant.getInstance().resetState();
	}

	/** Set variables with specific values. */
	@Override
	public void ctrlInit(List<MenuInit> initialMenus) throws BadInitFault_Exception {
		if (initialMenus == null || initialMenus.isEmpty()) {
			throwBadInit("null list of menus to initialize");
		}
		List<Carte> cartes = convertListOfMenuInitToListOfCartes(initialMenus);
		Restaurant.getInstance().initCartes(cartes);
	}

	// View helpers ----------------------------------------------------------

	// /** Helper to convert a domain object to a view. */
	// private ParkInfo buildParkInfo(Park park) {
	// ParkInfo info = new ParkInfo();
	// info.setId(park.getId());
	// info.setCoords(buildCoordinatesView(park.getCoordinates()));
	// info.setCapacity(park.getMaxCapacity());
	// info.setFreeSpaces(park.getFreeDocks());
	// info.setAvailableCars(park.getAvailableCars());
	// return info;
	// }

	private List<Carte> convertListOfMenuInitToListOfCartes(List<MenuInit> initialMenus) throws BadInitFault_Exception {
		List<Carte> cartes = new ArrayList<Carte>();
		for (MenuInit menu_info : initialMenus) {
			if (isValidMenuInit(menu_info)) {
				Carte c = convertMenuInitToCarte(menu_info);
				cartes.add(c);
			} else {
				throwBadInit("invalid menu init " + menu_info.getMenu().getId());
			}
		}
		return cartes;
	}

	private Carte convertMenuInitToCarte(MenuInit menu_info) {
		Carte carte = convertMenuToCarte(menu_info.getMenu());
		carte.set_quantity(menu_info.getQuantity());
		return carte;
	}

	private Carte convertMenuToCarte(Menu menu) {
		Carte carte = new Carte();
		carte.set_id(menu.getId().getId());
		carte.set_entree(menu.getEntree());
		carte.set_plate(menu.getPlate());
		carte.set_dessert(menu.getDessert());
		carte.set_price(menu.getPrice());
		carte.set_preparationTime(menu.getPreparationTime());
		return carte;
	}

	private Menu convertCarteToMenu(Carte carte) {
		Menu m = new Menu();
		MenuId mid = new MenuId();
		mid.setId(carte.get_id());
		m.setId(mid);
		m.setEntree(carte.get_entree());
		m.setPlate(carte.get_plate());
		m.setDessert(carte.get_dessert());
		m.setPrice(carte.get_price());
		m.setPreparationTime(carte.get_preparationTime());
		return m;
	}

	private List<Menu> convertListOfCartesToListOfMenus(List<Carte> cartes) {
		List<Menu> menus = new ArrayList<Menu>();
		for (Carte c : cartes) {
			menus.add(convertCarteToMenu(c));
		}
		return menus;
	}

	private MenuOrder convertOrderToMenuOrder(Order order) {
		MenuOrder mo = new MenuOrder();
		MenuOrderId moid = new MenuOrderId();
		MenuId mid = new MenuId();

		moid.setId(order.get_id());
		mid.setId(order.get_menuId());

		mo.setId(moid);
		mo.setMenuId(mid);
		mo.setMenuQuantity(order.get_menuQuantity());

		return mo;
	}

	// Exception helpers -----------------------------------------------------

	/** Helper to throw a new BadInit exception. */
	private void throwBadInit(final String message) throws BadInitFault_Exception {
		BadInitFault faultInfo = new BadInitFault();
		faultInfo.message = message;
		throw new BadInitFault_Exception(message, faultInfo);
	}

	private void throwBadText(final String message) throws BadTextFault_Exception {
		BadTextFault faultInfo = new BadTextFault();
		faultInfo.message = message;
		throw new BadTextFault_Exception(message, faultInfo);
	}

	private void throwBadMenuId(final String message) throws BadMenuIdFault_Exception {
		BadMenuIdFault faultInfo = new BadMenuIdFault();
		faultInfo.message = message;
		throw new BadMenuIdFault_Exception(message, faultInfo);
	}

	private void throwBadQuantity(final String message) throws BadQuantityFault_Exception {
		BadQuantityFault faultInfo = new BadQuantityFault();
		faultInfo.message = message;
		throw new BadQuantityFault_Exception(message, faultInfo);
	}

	private void throwInsufficientQuantity(final String message) throws InsufficientQuantityFault_Exception {
		InsufficientQuantityFault faultInfo = new InsufficientQuantityFault();
		faultInfo.message = message;
		throw new InsufficientQuantityFault_Exception(message, faultInfo);
	}

}
