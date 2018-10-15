package com.udacity.firebase.shoppinglistplusplus.model;

public class Item {
    private String name;
    private String owner;
    private Boolean bought;
    private String boughtBy;

    public Item() {
    }

    public Item(String name, String owner) {
        this.name = name;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public Boolean getBought() {
        return bought;
    }

    public String getBoughtBy() {
        return boughtBy;
    }
}
