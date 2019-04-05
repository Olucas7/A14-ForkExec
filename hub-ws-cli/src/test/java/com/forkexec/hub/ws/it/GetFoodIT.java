package com.forkexec.hub.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import com.forkexec.hub.ws.Food;
import com.forkexec.hub.ws.FoodId;
import com.forkexec.hub.ws.FoodInit;
import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;

import org.junit.Before;

public class GetFoodIT extends BaseIT {

    private List<FoodInit> foods = new ArrayList<FoodInit>();

    private FoodInit init = new FoodInit();

    private Food food = new Food();

    private FoodId food_id = new FoodId();

    @Before
    public void setUp() throws InvalidInitFault_Exception, InvalidUserIdFault_Exception {
        food_id.setMenuId("10");
        food.setId(food_id);
        food.setEntree("azeitonas");
        food.setPlate("carapaus grelhados");
        food.setDessert("baba de camelo");
        food.setPreparationTime(47);
        food.setPrice(32);

        init.setFood(food);
        init.setQuantity(5);
        foods.add(init);
        client.ctrlInitFood(foods);
    }

    /*
     * public void success() { assertEquals("10", food.getId().getMenuId()); }
     */

}