package com.forkexec.rst.domain;

import java.util.Objects;

public class Carte {
    private String _id;
    private String _entree;
    private String _plate;
    private String _dessert;
    private int _price;
    private int _preparationTime;
    private int _quantity;


    public Carte() {
    }

    public Carte(String id, String entree, String plate, String dessert, int price, int preparationTime, int quantity) {
        this._id = id;
        this._entree = entree;
        this._plate = plate;
        this._dessert = dessert;
        this._price = price;
        this._preparationTime = preparationTime;
        this._quantity = quantity;
    }

    public String get_id() {
        return this._id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_entree() {
        return this._entree;
    }

    public void set_entree(String _entree) {
        this._entree = _entree;
    }

    public String get_plate() {
        return this._plate;
    }

    public void set_plate(String _plate) {
        this._plate = _plate;
    }

    public String get_dessert() {
        return this._dessert;
    }

    public void set_dessert(String _dessert) {
        this._dessert = _dessert;
    }

    public int get_price() {
        return this._price;
    }

    public void set_price(int _price) {
        this._price = _price;
    }

    public int get_preparationTime() {
        return this._preparationTime;
    }

    public void set_preparationTime(int _preparationTime) {
        this._preparationTime = _preparationTime;
    }

    public int get_quantity() {
        return this._quantity;
    }

    public void set_quantity(int _quantity) {
        this._quantity = _quantity;
    }

    public Carte _id(String _id) {
        this._id = _id;
        return this;
    }

    public Carte _entree(String _entree) {
        this._entree = _entree;
        return this;
    }

    public Carte _plate(String _plate) {
        this._plate = _plate;
        return this;
    }

    public Carte _dessert(String _dessert) {
        this._dessert = _dessert;
        return this;
    }

    public Carte _price(int _price) {
        this._price = _price;
        return this;
    }

    public Carte _preparationTime(int _preparationTime) {
        this._preparationTime = _preparationTime;
        return this;
    }

    public Carte _quantity(int _quantity) {
        this._quantity = _quantity;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Carte)) {
            return false;
        }
        Carte carte = (Carte) o;
        return Objects.equals(_id, carte._id) && Objects.equals(_entree, carte._entree) && Objects.equals(_plate, carte._plate) && Objects.equals(_dessert, carte._dessert) && _price == carte._price && _preparationTime == carte._preparationTime && _quantity == carte._quantity;
    }

    @Override
    public String toString() {
        return "{" +
            " _id='" + get_id() + "'" +
            ", _entree='" + get_entree() + "'" +
            ", _plate='" + get_plate() + "'" +
            ", _dessert='" + get_dessert() + "'" +
            ", _price='" + get_price() + "'" +
            ", _preparationTime='" + get_preparationTime() + "'" +
            ", _quantity='" + get_quantity() + "'" +
            "}";
    }
    
}