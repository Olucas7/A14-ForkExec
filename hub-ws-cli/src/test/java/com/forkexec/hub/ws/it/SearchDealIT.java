package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.forkexec.hub.ws.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchDealIT extends BaseIT {

	@Before
	public void setUp() throws InvalidInitFault_Exception {
		createFoods();
		client.ctrlInitFood(initialFoods);
	}

	@After
	public void tearDown() {
		client.ctrlClear();
	}

	@Test
	public void success() throws InvalidTextFault_Exception {
		List<Food> deals = client.searchDeal("tofu");
		assertEquals(2, deals.size());
		assertEquals(deals.get(0).getId().getMenuId(), food1.getId().getMenuId());
		assertEquals(deals.get(1).getId().getMenuId(), food2.getId().getMenuId());
	}

	@Test(expected = InvalidTextFault_Exception.class)
	public void nullDescription() throws InvalidTextFault_Exception {
		client.searchDeal(null);
	}

	@Test(expected = InvalidTextFault_Exception.class)
	public void emptyDescription() throws InvalidTextFault_Exception {
		client.searchDeal("");
	}

	@Test(expected = InvalidTextFault_Exception.class)
	public void spacedDescription() throws InvalidTextFault_Exception {
		client.searchDeal("bitoque com ovo");
	}

	@Test
	public void nonExistingDescription() throws InvalidTextFault_Exception {
		assertEquals(0, client.searchDeal("cha").size());
	}

}