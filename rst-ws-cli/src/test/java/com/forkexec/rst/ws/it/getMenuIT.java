package com.forkexec.rst.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.After;

import com.forkexec.rst.ws.*;

import org.junit.Test;

public class getMenuIT extends BaseIT {

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
        menu.setDessert("serradura");
        menu.setEntree("camarao");
        menu.setPlate("bitoque");
        menu.setPreparationTime(10);
        menu_init.setMenu(menu);
        menu_init.setQuantity(10);
        menuInitials.add(menu_init);
        client.ctrlInit(menuInitials);
    }

    @Test
    public void success() throws BadMenuIdFault_Exception {
        assertEquals("10", menu.getId().getId());
        assertEquals(10, menu_init.getQuantity());
        assertEquals(5, menu.getPrice());
        assertEquals("serradura", menu.getDessert());
        assertEquals("camarao", menu.getEntree());
        assertEquals("bitoque", menu.getPlate());
        assertEquals(10, menu.getPreparationTime());
        assertEquals(10, menu_init.getQuantity());
        client.getMenu(ok_id_menu);
    }

    @Test(expected = BadMenuIdFault_Exception.class)
    public void blankId() throws BadMenuIdFault_Exception {
        empty_id_menu.setId(null);
        client.getMenu(empty_id_menu);

    }

    @Test(expected = BadMenuIdFault_Exception.class)
    public void null_id() throws BadMenuIdFault_Exception {
        null_id_menu.setId(null);
        client.getMenu(null_id_menu);
    }

    @Test(expected = BadMenuIdFault_Exception.class)
    public void spaced_id_menu() throws BadMenuIdFault_Exception {
        spaced_id_menu.setId("6 9");
        client.getMenu(spaced_id_menu);
    }

    @Test(expected = BadMenuIdFault_Exception.class)
    public void nonExistingId() throws BadMenuIdFault_Exception {
        non_existing_id_menu.setId("8");
        client.getMenu(non_existing_id_menu);
    }

    @After
    public void tearDown() {
        client.ctrlClear();
    }

}
