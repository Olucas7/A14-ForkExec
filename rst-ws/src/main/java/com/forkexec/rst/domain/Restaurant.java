package com.forkexec.rst.domain;

import java.util.ArrayList;
import java.util.List;

import com.forkexec.rst.domain.Exceptions.BadMenuIdException;
import com.forkexec.rst.domain.Exceptions.BadTextException;
import com.forkexec.rst.domain.Exceptions.InsufficientQuantityException;

/**
 * Restaurant
 *
 * A restaurant server.
 *
 */
public class Restaurant {

	private static List<Carte> _database = new ArrayList<Carte>();

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

	public synchronized void resetState() {
		_database = new ArrayList<Carte>();
		_menuOrderCounter = 0;
	}

	public Carte getMenu(String menuId) throws BadMenuIdException {
		for(Carte c: _database) {
			if (c.get_id().equals(menuId)) {
				return c;
			}
		}
		throw new BadMenuIdException("no menu found with given menuid");
	}

	public List<Carte> searchMenus(String descriptionString) throws BadTextException {
		List<Carte> cartes = new ArrayList<Carte>();
		for(Carte c: _database) {
			String entree = c.get_entree();
   			String plate = c.get_plate();
			String dessert = c.get_dessert();

			if(entree.contains(descriptionString) ||
				plate.contains(descriptionString) ||
				dessert.contains(descriptionString)) {
					cartes.add(c);
				}
			
		}

		if (cartes.isEmpty()) {
			throw new BadTextException("no menus found with given descripton");
		}

		return cartes;
	}

	public synchronized Order orderMenu(String arg0, int arg1)
			throws  BadMenuIdException, InsufficientQuantityException {
		Carte carte = getMenu(arg0);  //throws BadMenuIdException
		int quantity = carte.get_quantity();
		
		if (arg1 > quantity) {
			throw new InsufficientQuantityException("not enough quantity for order");
		}	
		
		int index = _database.indexOf(carte);
		
		//update database
		Carte new_carte = new Carte(carte.get_id(), carte.get_entree(), carte.get_plate(), carte.get_dessert(), 
		carte.get_price(), carte.get_preparationTime(), carte.get_quantity() - arg1);
		_database.set(index, new_carte);

		//create menu order
		Order order = new Order(String.valueOf(_menuOrderCounter+1), arg0, arg1);
		return order;
	}

	public synchronized void initCartes(List<Carte> cartes) {
		_database.addAll(cartes);
	}
	
}
