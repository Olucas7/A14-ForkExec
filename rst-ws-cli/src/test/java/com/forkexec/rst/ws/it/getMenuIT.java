package com.forkexec.rst.ws.it;

import static org.junit.Assert.assertEquals;


import java.util.List;
import java.util.ArrayList;

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

    public void success() throws BadMenuIdFault_Exception, BadInitFault_Exception {
        ok_id_menu.setId("10");
        menu.setId(ok_id_menu);
        menu_init.setMenu(menu);
        menu_init.setQuantity(10);
        menuInitials.add(menu_init);
        client.ctrlInit(menuInitials);
        client.getMenu(ok_id_menu);

        assertEquals("10", menu.getId());

        assertEquals(10, menu_init.getQuantity());

    }

    @Test(expected = BadMenuIdFault_Exception.class)
    public void blankId() throws BadMenuIdFault_Exception {
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

}
