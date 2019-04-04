package com.forkexec.rst.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.After;

import com.forkexec.rst.ws.*;

import org.junit.Test;

public class orderMenuIT extends BaseIT {

    private List<MenuInit> menuInitials = new ArrayList<MenuInit>();

    private MenuInit menu_init = new MenuInit();

    private Menu menu = new Menu();

    private MenuId ok_id_menu = new MenuId();
    private MenuId empty_id_menu = new MenuId();
    private MenuId null_id_menu = new MenuId();
    private MenuId spaced_id_menu = new MenuId();
    private MenuId non_existing_id_menu = new MenuId();

    @Before
    public void setUp() throws BadInitFault_Exception {
        ok_id_menu.setId("10");
        menu.setId(ok_id_menu);
        menu.setPrice(5);
        menu.setDessert("mousse_de_chocolate");
        menu.setEntree("beringela_recheada");
        menu.setPlate("tofu");
        menu.setPreparationTime(10);
        menu_init.setMenu(menu);
        menu_init.setQuantity(50);
        menuInitials.add(menu_init);
        client.ctrlInit(menuInitials);
    }

    @Test
    public void success() throws BadMenuIdFault_Exception,BadQuantityFault_Exception,InsufficientQuantityFault_Exception{
        assertEquals("10", menu.getId().getId());
        assertEquals(50, menu_init.getQuantity());
        assertEquals(5, menu.getPrice());
        assertEquals("mousse_de_chocolate", menu.getDessert());
        assertEquals("beringela_recheada", menu.getEntree());
        assertEquals("tofu", menu.getPlate());
        assertEquals(10, menu.getPreparationTime());
        assertEquals(50, menu_init.getQuantity());
        client.orderMenu(ok_id_menu,50);
        
    }

    @Test(expected = BadMenuIdFault_Exception.class)
    public void blankId() throws BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception {
        empty_id_menu.setId(null);
        client.orderMenu(empty_id_menu,50);

    }

    @Test(expected = BadMenuIdFault_Exception.class)
    public void null_id() throws BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception {
        null_id_menu.setId(null);
        client.orderMenu(null_id_menu,50);
    }

    @Test(expected = BadMenuIdFault_Exception.class)
    public void spaced_id_menu() throws BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception {
        spaced_id_menu.setId("6 9");
        client.orderMenu(spaced_id_menu,50);
    }

    @Test(expected = BadMenuIdFault_Exception.class)
    public void nonExistingId() throws BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception {
        non_existing_id_menu.setId("8");
        client.orderMenu(non_existing_id_menu,50);
    }

    @Test(expected = BadQuantityFault_Exception.class)
    public void badQuantity() throws BadQuantityFault_Exception, BadMenuIdFault_Exception, InsufficientQuantityFault_Exception {
        client.orderMenu(ok_id_menu,-2);
    }

    @Test(expected = InsufficientQuantityFault_Exception.class)
    public void insufficientQuantity() throws InsufficientQuantityFault_Exception, BadMenuIdFault_Exception, BadQuantityFault_Exception {
        non_existing_id_menu.setId("8");
        client.orderMenu(ok_id_menu,70);
    }

    @After
    public void tearDown() {
        client.ctrlClear();
    }

}






