package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import com.forkexec.hub.ws.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchHungryIT extends BaseIT {

	private String RESTAURANT1 = "A14_Restaurant1";
	private String RESTAURANT2 = "A14_Restaurant2";
	private Food food1;
	private Food food2;
	private List<FoodInit> initialFoods;

	@Before
	public void setupTest() throws InvalidInitFault_Exception {
		initialFoods = new ArrayList<FoodInit>();
		FoodInit foodInit1 = new FoodInit();
		food1 = new Food();
		FoodId foodId1 = new FoodId();
		FoodInit foodInit2 = new FoodInit();
		food2 = new Food();
		FoodId foodId2 = new FoodId();

		foodId1.setMenuId("1");
		foodId1.setRestaurantId(RESTAURANT1);
		foodId2.setMenuId("2");
		foodId2.setRestaurantId(RESTAURANT2);

		food1.setId(foodId1);
		food1.setPrice(5);
		food1.setDessert("mousse_de_chocolate");
		food1.setEntree("beringela_recheada");
		food1.setPlate("tofu");
		food1.setPreparationTime(30);

		food2.setId(foodId2);
		food2.setPrice(25);
		food2.setDessert("serradura");
		food2.setEntree("camarao");
		food2.setPlate("bitoque_de_tofu");
		food2.setPreparationTime(10);

		foodInit1.setFood(food1);
		foodInit1.setQuantity(50);
		foodInit2.setFood(food2);
		foodInit2.setQuantity(5);

		initialFoods.add(foodInit1);
		initialFoods.add(foodInit2);
		client.ctrlInitFood(initialFoods);
	}

	@After
	public void clean() {
		client.ctrlClear();
	}

	@Test
	public void success() throws InvalidTextFault_Exception {
		List<Food> deals = client.searchHungry("tofu");
		assertEquals(2, deals.size());
		assertEquals(deals.get(1).getId().getMenuId(), food1.getId().getMenuId());
		assertEquals(deals.get(0).getId().getMenuId(), food2.getId().getMenuId());
	}

	@Test(expected = InvalidTextFault_Exception.class)
	public void nullDescription() throws InvalidTextFault_Exception {
		client.searchHungry(null);
	}

	@Test(expected = InvalidTextFault_Exception.class)
	public void emptyDescription() throws InvalidTextFault_Exception {
		client.searchHungry("");
	}

	@Test(expected = InvalidTextFault_Exception.class)
	public void spacedDescription() throws InvalidTextFault_Exception {
		client.searchHungry("bitoque com ovo");
	}

	@Test
	public void nonExistingDescription() throws InvalidTextFault_Exception {
		assertEquals(0, client.searchHungry("cha").size());
	}

}