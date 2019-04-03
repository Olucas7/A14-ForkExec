
package com.forkexec.hub.domain;

public class Meal {

	protected String id;
	protected String entree;
	protected String plate;
	protected String dessert;
	protected int price;
	protected int preparationTime;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the entree
	 */
	public String getEntree() {
		return entree;
	}

	/**
	 * @param entree the entree to set
	 */
	public void setEntree(String entree) {
		this.entree = entree;
	}

	/**
	 * @return the plate
	 */
	public String getPlate() {
		return plate;
	}

	/**
	 * @param plate the plate to set
	 */
	public void setPlate(String plate) {
		this.plate = plate;
	}

	/**
	 * @return the dessert
	 */
	public String getDessert() {
		return dessert;
	}

	/**
	 * @param dessert the dessert to set
	 */
	public void setDessert(String dessert) {
		this.dessert = dessert;
	}

	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @return the preparationTime
	 */
	public int getPreparationTime() {
		return preparationTime;
	}

	/**
	 * @param preparationTime the preparationTime to set
	 */
	public void setPreparationTime(int preparationTime) {
		this.preparationTime = preparationTime;
	}

}
