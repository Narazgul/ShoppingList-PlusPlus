package com.udacity.firebase.shoppinglistplusplus.model;

public class Item {
    private String itemName;
    private String owner;
    private Boolean bought;
    private String boughtBy;

    public Item() {
    }

    public Item(String name, String owner) {
        this.itemName = name;
        this.owner = owner;
        this.bought = false;
        this.boughtBy = "";
    }

    public String getItemName() {
        return itemName;
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
