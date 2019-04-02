package com.forkexec.rst.domain;

import java.util.Objects;

public class Order {
    private String _id;
    private String _menuId;
    private int _menuQuantity;


    public Order() {
    }

    public Order(String id, String menuId, int menuQuantity) {
        this._id = id;
        this._menuId = menuId;
        this._menuQuantity = menuQuantity;
    }

    public String get_id() {
        return this._id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_menuId() {
        return this._menuId;
    }

    public void set_menuId(String _menuId) {
        this._menuId = _menuId;
    }

    public int get_menuQuantity() {
        return this._menuQuantity;
    }

    public void set_menuQuantity(int _menuQuantity) {
        this._menuQuantity = _menuQuantity;
    }

    public Order _id(String _id) {
        this._id = _id;
        return this;
    }

    public Order _menuId(String _menuId) {
        this._menuId = _menuId;
        return this;
    }

    public Order _menuQuantity(int _menuQuantity) {
        this._menuQuantity = _menuQuantity;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Order)) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(_id, order._id) && Objects.equals(_menuId, order._menuId) && _menuQuantity == order._menuQuantity;
    }

    @Override
    public String toString() {
        return "{" +
            " _id='" + get_id() + "'" +
            ", _menuId='" + get_menuId() + "'" +
            ", _menuQuantity='" + get_menuQuantity() + "'" +
            "}";
    }

}