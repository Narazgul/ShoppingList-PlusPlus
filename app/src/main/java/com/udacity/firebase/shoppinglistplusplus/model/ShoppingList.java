package com.udacity.firebase.shoppinglistplusplus.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class ShoppingList {
    private String listName;
    private String owner;
    private int usersShoppingOnList;
    @ServerTimestamp
    private Timestamp createdAt;
    @ServerTimestamp
    private Timestamp lastEdited;

    public ShoppingList() {
    }

    public ShoppingList(String listName, String owner) {
        this.listName = listName;
        this.owner = owner;
        this.usersShoppingOnList = 0;
    }

    public String getListName() {
        return listName;
    }
    public String getOwner() {
        return owner;
    }

    public int getUsersShoppingOnList() {
        return usersShoppingOnList;
    }
    public Timestamp getLastEdited() {
        return lastEdited;
    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
