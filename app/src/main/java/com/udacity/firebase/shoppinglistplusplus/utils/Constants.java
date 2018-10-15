package com.udacity.firebase.shoppinglistplusplus.utils;

/**
 * Constants class store most important strings and paths of the app
 */
public final class Constants {

    /**
     * Constants related to locations in Firebase, such as the name of the node
     * where active lists are stored (ie "activeLists")
     */
    public static final String ACTIVE_LISTS = "activeLists";
    public static final String ITEMS = "items";

    /*
     * Shopping List
     */
    public static final String SHOPPING_LIST_NAME = "listName";
    public static final String SHOPPING_LIST_OWNER = "owner";
    public static final String SHOPPING_LIST_CREATED_AT = "createdAt";
    public static final String SHOPPING_LIST_LAST_EDITED = "lastEdited";

    /*
     * Item
     */
    public static final String ITEM_NAME = "itemName";
    public static final String ITEM_OWNER = "owner";
    public static final String ITEM_BOUGHT = "bought";
    public static final String ITEM_BOUGHT_BY = "boughtBy";


    /**
     * Constants for bundles, extras and shared preferences keys
     */
    public static final String CONSTANT_LIST_NAME = "listname";
    public static final String CONSTANT_ITEM_NAME = "itemname";
    public static final String CONSTANT_LIST_ID = "listId";
    public static final String CONSTANT_DOCUMENT_ID = "documentId";
    public static final String EXTRA_KEY_ID = "id";


}
