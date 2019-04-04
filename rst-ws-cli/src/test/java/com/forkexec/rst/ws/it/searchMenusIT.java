package com.forkexec.rst.ws.it;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.After;

import com.forkexec.rst.ws.*;

import org.junit.Test;

public class searchMenusIT extends BaseIT {
    private List<MenuInit> menuInitials = new ArrayList<MenuInit>();

    private MenuInit menu_init = new MenuInit();

    private Menu menu = new Menu();

    private MenuId id_menu = new MenuId();

    @Before
    public void setUp() throws BadInitFault_Exception {
        id_menu.setId("5");
        menu.setId(id_menu);
        menu.setPrice(25);
        menu.setDessert("serradura");
        menu.setEntree("camarao");
        menu.setPlate("bitoque");
        menu.setPreparationTime(10);
        menu_init.setMenu(menu);
        menu_init.setQuantity(50);
        menuInitials.add(menu_init);
        client.ctrlInit(menuInitials);
    }

    @Test
    public void success() throws BadMenuIdFault_Exception, BadTextFault_Exception {
        assertEquals("5", menu.getId().getId());
        assertEquals(50, menu_init.getQuantity());
        assertEquals(25, menu.getPrice());
        assertEquals("serradura", menu.getDessert());
        assertEquals("camarao", menu.getEntree());
        assertEquals("bitoque", menu.getPlate());
        assertEquals(10, menu.getPreparationTime());
        assertEquals(50, menu_init.getQuantity());
        assertEquals("5", client.searchMenus("serradura").get(0).getId().getId());
    }

    @Test(expected = BadTextFault_Exception.class)
    public void nullDescription() throws BadTextFault_Exception {
        client.searchMenus(null);
    }

    @Test(expected = BadTextFault_Exception.class)
    public void emptyDescription() throws BadTextFault_Exception {
        client.searchMenus("");
    }

    @Test(expected = BadTextFault_Exception.class)
    public void spacedDescription() throws BadTextFault_Exception {
        client.searchMenus("bitoque com ovo");
    }

    @Test
    public void nonExistingDescription() throws BadTextFault_Exception {
        assertEquals(0, client.searchMenus("açorda").size());
    }

    @After
    public void tearDown() {
        client.ctrlClear();
    }

}
