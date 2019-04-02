package com.forkexec.rst.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jws.WebService;

import com.forkexec.rst.domain.Carte;
import com.forkexec.rst.domain.Order;
import com.forkexec.rst.domain.Restaurant;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.rst.ws.RestaurantPortType",
            wsdlLocation = "RestaurantService.wsdl",
            name ="RestaurantWebService",
            portName = "RestaurantPort",
            targetNamespace="http://ws.rst.forkexec.com/",
            serviceName = "RestaurantService"
)
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
		Carte carte = Restaurant.getInstance().getMenu(menuId.getId());
		return convertCarteToMenu(carte);
		//checkar string do menuid?
	}


	@Override
	public List<Menu> searchMenus(String descriptionText) throws BadTextFault_Exception {
		checkMenuDescription(descriptionText);
		List<Carte> cartes = Restaurant.getInstance().searchMenus(descriptionText);
		return convertListOfCartesToListOfMenus(cartes);
	}

	@Override
	public MenuOrder orderMenu(MenuId arg0, int arg1)
			throws BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception {
		if (arg1 < 1) {
			throw new BadQuantityFault_Exception("bad quantity", new BadQuantityFault());
		}
		Order order = Restaurant.getInstance().orderMenu(arg0.getId(), arg1);
		return convertOrderToMenuOrder(order);
	}

	public void checkMenuDescription(String descriptionString) throws BadTextFault_Exception {
        String message_null = "null description";
        String message_invalid = "description with whitespaces";


        if (descriptionString == null || descriptionString.trim().length() == 0) 
            throwBadText(message_null);

        String regex = "\\s";
        Pattern pattern = Pattern.compile(regex);
        Matcher mat = pattern.matcher(descriptionString);
        if(mat.matches()) //contains white spaces
            throwBadText(message_invalid);
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
		// TODO Auto-generated method stub
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

}
