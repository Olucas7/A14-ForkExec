package com.forkexec.hub.ws.it;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.forkexec.hub.ws.*;
import com.forkexec.hub.ws.cli.HubClient;

import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Base class for testing a Park Load properties from test.properties
 */
public class BaseIT {

	private static final String TEST_PROP_FILE = "/test.properties";
	protected static Properties testProps;

	protected static HubClient client;

	protected final int INITIAL_BALANCE = 100;
	protected final String VALID_EMAIL = "joao.barata@tecnico.pt";
	protected final String NULL_EMAIL = null;
	protected final String NO_AT_EMAIL = "joao.baratatecnico.pt";
	protected final String EMPTY_EMAIL = "";
	protected final String NO_USER_EMAIL = "@tecnico.pt";
	protected final String NO_DOMAIN_EMAIL = "velhinho@";
	protected final String NO_USER_DOMAIN_EMAIL = "@";
	protected final String RESTAURANT1 = "A14_Restaurant1";
	protected final String RESTAURANT2 = "A14_Restaurant2";

	protected final String VALID_NUMBER = "4024007102923926";
	protected final String INVALID_NUMBER ="123456";
	protected final String NULL_CREDIT_CARD = null;
	protected final String SPACED_CREDIT_CARD ="40240 07102 923926";
	protected final String EMPTY_CREDIT_CARD ="";
	protected final int VALID_MONEY = 50;
	protected final int INVALID_MONEY = 80;

	protected Food food1;
	protected Food food2;
	protected FoodId foodId1;
	protected FoodId foodId2;
	protected FoodInit foodInit1;
	protected FoodInit foodInit2;
	protected List<FoodInit> initialFoods;


	@BeforeClass
	public static void oneTimeSetup() throws Exception {
		testProps = new Properties();
		try {
			testProps.load(BaseIT.class.getResourceAsStream(TEST_PROP_FILE));
			System.out.println("Loaded test properties:");
			System.out.println(testProps);
		} catch (IOException e) {
			final String msg = String.format("Could not load properties file {}", TEST_PROP_FILE);
			System.out.println(msg);
			throw e;
		}

		final String uddiEnabled = testProps.getProperty("uddi.enabled");
		final String verboseEnabled = testProps.getProperty("verbose.enabled");

		final String uddiURL = testProps.getProperty("uddi.url");
		final String wsName = testProps.getProperty("ws.name");
		final String wsURL = testProps.getProperty("ws.url");

		if ("true".equalsIgnoreCase(uddiEnabled)) {
			client = new HubClient(uddiURL, wsName);
		} else {
			client = new HubClient(wsURL);
		}
		client.setVerbose("true".equalsIgnoreCase(verboseEnabled));
	}

	@AfterClass
	public static void cleanup() {
	}

	public void createFoods() {
		initialFoods = new ArrayList<FoodInit>();
		foodInit1 = new FoodInit();
		food1 = new Food();
		foodId1 = new FoodId();
		foodInit2 = new FoodInit();
		food2 = new Food();
		foodId2 = new FoodId();

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
	}

}
