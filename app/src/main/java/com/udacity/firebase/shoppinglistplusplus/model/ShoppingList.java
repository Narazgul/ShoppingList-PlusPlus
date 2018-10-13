package com.udacity.firebase.shoppinglistplusplus.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class ShoppingList {
    private String listName;
    private String owner;
    @ServerTimestamp
    private Date timestamp;

    public ShoppingList() {
    }

    public ShoppingList(String listName, String owner) {
        this.listName = listName;
        this.owner = owner;
    }

    public String getListName() {
        return listName;
    }
    public String getOwner() {
        return owner;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
