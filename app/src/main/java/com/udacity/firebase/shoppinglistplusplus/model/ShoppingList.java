package com.udacity.firebase.shoppinglistplusplus.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class ShoppingList {
    private String listName;
    private String owner;
    private Timestamp createdAt;
    private Timestamp lastEdited;

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

    @ServerTimestamp
    public Timestamp getLastEdited() {
        return lastEdited;
    }

    @ServerTimestamp
    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
