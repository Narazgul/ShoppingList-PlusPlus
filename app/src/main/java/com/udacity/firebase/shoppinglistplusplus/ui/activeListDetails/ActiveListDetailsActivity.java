package com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.Item;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;
import com.udacity.firebase.shoppinglistplusplus.ui.BaseActivity;
import com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails.adapter.ItemListAdapter;
import com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails.dialogs.AddItemDialogFragment;
import com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails.dialogs.EditListItemNameDialogFragment;
import com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails.dialogs.EditListNameDialogFragment;
import com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails.dialogs.RemoveItemDialogFragment;
import com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails.dialogs.RemoveListDialogFragment;
import com.udacity.firebase.shoppinglistplusplus.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.ACTIVE_LISTS;
import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.ACTIVE_SHOPPERS;
import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.EXTRA_KEY_ID;
import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.ITEMS;
import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.ITEM_BOUGHT;
import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.ITEM_BOUGHT_BY;
import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.ITEM_NAME;
import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.PREFS_DISPLAY_NAME;
import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.USERES_SHOPPING;

public class ActiveListDetailsActivity extends BaseActivity implements
        ItemListAdapter.ItemBoughtClickListener,
        ItemListAdapter.ItemLongClickListener,
        ItemListAdapter.ItemDeleteClickListener {

    public static final String TAG = ActiveListDetailsActivity.class.getSimpleName();

    private Button shoppingMode;
    private Boolean userIsOwner;
    private Boolean isInShoppingMode = false;
    private String listId;
    private ShoppingList shoppingList;
    private CollectionReference usersShoppingReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_list_details);

        initializeScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        MenuItem edit = menu.findItem(R.id.action_edit);
        MenuItem delete = menu.findItem(R.id.action_delete);
        edit.setVisible(userIsOwner);
        delete.setVisible(userIsOwner);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_edit:
                showEditListNameDialog();
                return true;
            case R.id.action_delete:
                showDeleteListDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemBoughtClicked(String itemId) {
        if (!isInShoppingMode) {
            Toast.makeText(this, "Start shopping first!", Toast.LENGTH_SHORT).show();
        } else {
            final DocumentReference ref = db.collection(ACTIVE_LISTS).document(listId).collection(ITEMS).document(itemId);
            final Map<String, Object> updates = new HashMap<>();

            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(ref);
                    boolean isBought = snapshot.getBoolean(ITEM_BOUGHT);

                    updates.put(ITEM_BOUGHT, !isBought);
                    if (user != null && !TextUtils.isEmpty(user.getDisplayName())) {
                        updates.put(ITEM_BOUGHT_BY, prefs.getString(PREFS_DISPLAY_NAME, "Anonymous User"));
                    }

                    transaction.update(ref, updates);
                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "Transaction successful");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Transaction failure", e);
                    Toast.makeText(ActiveListDetailsActivity.this, "Couldn't update item", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onItemLongClicked(String itemId) {
        DialogFragment dialog = EditListItemNameDialogFragment.newInstance(listId, itemId);
        dialog.show(getSupportFragmentManager(), "EditListNameDialogFragment");
    }

    @Override
    public void onItemDeleteClicked(String itemId) {
        DialogFragment dialog = RemoveItemDialogFragment.newInstance(listId, itemId);
        dialog.show(getSupportFragmentManager(), "RemoveListDialogFragment");
    }

    private void initializeScreen() {
        Toolbar toolbar = findViewById(R.id.app_bar);
        if (getIntent() != null && getIntent().hasExtra(EXTRA_KEY_ID)) {
            listId = getIntent().getStringExtra(EXTRA_KEY_ID);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBarTitle();

        usersShoppingReference = db.collection(ACTIVE_LISTS).document(listId).collection(ACTIVE_SHOPPERS);
        shoppingMode = findViewById(R.id.button_shopping);
        setShoppingMode();

        RecyclerView recyclerView = findViewById(R.id.recycler_view_items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(newItemListsAdapter());
    }

    private void setSupportActionBarTitle() {
        db.collection(ACTIVE_LISTS).document(listId).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    shoppingList = snapshot.toObject(ShoppingList.class);
                    userIsOwner = Utils.checkIfOwner(shoppingList, userDisplayName);
                } else {
                    finish();
                    return;
                }
                getSupportActionBar().setTitle(shoppingList.getListName());
            }
        });
    }

    private void setShoppingMode() {
        usersShoppingReference.document(userUid).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    shoppingMode.setText(R.string.button_stop_shopping);
                    shoppingMode.setBackgroundColor(ContextCompat.getColor(ActiveListDetailsActivity.this, R.color.dark_grey));
                    isInShoppingMode = true;
                } else {
                    shoppingMode.setText(R.string.button_start_shopping);
                    shoppingMode.setBackgroundColor(ContextCompat.getColor(ActiveListDetailsActivity.this, R.color.primary_dark));
                    isInShoppingMode = false;
                }
            }
        });
    }

    public void toggleShoppingMode(View view) {
        DocumentReference shoppingUserRef = usersShoppingReference.document(userUid);

        if (isInShoppingMode) {
            shoppingUserRef.delete();
        } else {
            Map<String, Object> update = new HashMap<>();
            update.put(USERES_SHOPPING, userDisplayName);

            shoppingUserRef.set(update);
        }
    }

    private RecyclerView.Adapter newItemListsAdapter() {
        Query query = db
                .collection(ACTIVE_LISTS)
                .document(listId)
                .collection(ITEMS)
                .orderBy(ITEM_NAME);

        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .setLifecycleOwner(this)
                .build();

        return new ItemListAdapter(options, this, this, this, userDisplayName);
    }

    private void showEditListNameDialog() {
        DialogFragment dialog = EditListNameDialogFragment.newInstance(shoppingList, listId);
        dialog.show(getSupportFragmentManager(), "EditListNameDialogFragment");
    }

    private void showDeleteListDialog() {
        DialogFragment dialog = RemoveListDialogFragment.newInstance(shoppingList, listId);
        dialog.show(getSupportFragmentManager(), "RemoveListDialogFragment");
    }

    public void showAddItemDialog(View view) {
        android.app.DialogFragment dialog = AddItemDialogFragment.newInstance(listId);
        dialog.show(getFragmentManager(), "AddItemDialogFragment");
    }
}
