package com.forkexec.rst.domain;

import java.util.ArrayList;
import java.util.List;

import com.forkexec.rst.ws.BadMenuIdFault;
import com.forkexec.rst.ws.BadMenuIdFault_Exception;
import com.forkexec.rst.ws.BadQuantityFault_Exception;
import com.forkexec.rst.ws.BadTextFault;
import com.forkexec.rst.ws.BadTextFault_Exception;
import com.forkexec.rst.ws.InsufficientQuantityFault;
import com.forkexec.rst.ws.InsufficientQuantityFault_Exception;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.MenuInit;
import com.forkexec.rst.ws.MenuOrder;
import com.forkexec.rst.ws.MenuOrderId;

/**
 * Restaurant
 *
 * A restaurant server.
 *
 */
public class Restaurant {

	private static List<MenuInit> _database = new ArrayList<MenuInit>();

	private static long _menuOrderCounter = 0; 

	// Singleton -------------------------------------------------------------

	/** Private constructor prevents instantiation from other classes. */
	private Restaurant() {
		// Initialization of default values
	}

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance()
	 * or the first access to SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final Restaurant INSTANCE = new Restaurant();
	}

	public static synchronized Restaurant getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public static synchronized void resetState() {
		_database = new ArrayList<MenuInit>();
		_menuOrderCounter = 0;
	}

	public Menu getMenu(MenuId menuId) throws BadMenuIdFault_Exception {
		return getMenuInitbyMenuId(menuId).getMenu();
	}

	public List<Menu> searchMenus(String descriptionString) throws BadTextFault_Exception {
		List<Menu> menus = new ArrayList<Menu>();
		for(MenuInit menu_info: _database) {
			Menu m = menu_info.getMenu();
			String entree = m.getEntree();
   			String plate = m.getPlate();
			String dessert = m.getDessert();

			if(entree.contains(descriptionString) ||
				plate.contains(descriptionString) ||
				dessert.contains(descriptionString)) {
					menus.add(m);
				}
			
		}

		if (menus.isEmpty()) {
			throw new BadTextFault_Exception("no menus found with given descripton", new BadTextFault());
		}

		return menus;
	}

	public synchronized MenuOrder orderMenu(MenuId arg0, int arg1)
			throws BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception {
		MenuInit menu_info = getMenuInitbyMenuId(arg0);
		int quantity = menu_info.getQuantity();
		
		if (arg1 > quantity) {
			throw new InsufficientQuantityFault_Exception("not enoughquantity for oder", new InsufficientQuantityFault());
		}	
		
		int index = _database.indexOf(menu_info);
		
		//update database
		MenuInit new_info = new MenuInit();
		new_info.setMenu(menu_info.getMenu());
		new_info.setQuantity(quantity-arg1);
		_database.set(index, new_info);

		//create menu order
		MenuOrderId order_id = new MenuOrderId();
		order_id.setId(String.valueOf(_menuOrderCounter+1));

		MenuOrder order = new MenuOrder();
		order.setId(order_id);
		order.setMenuId(arg0);
		order.setMenuQuantity(arg1);
		
		return order;
	}
	
	private MenuInit getMenuInitbyMenuId(MenuId id) throws BadMenuIdFault_Exception {
		for(MenuInit menu_info: _database) {
			Menu m = menu_info.getMenu();
			if (m.getId().equals(id)) {
				return menu_info;
			}
		}
		throw new BadMenuIdFault_Exception("invalid menu id", new BadMenuIdFault());
	}
	
}
