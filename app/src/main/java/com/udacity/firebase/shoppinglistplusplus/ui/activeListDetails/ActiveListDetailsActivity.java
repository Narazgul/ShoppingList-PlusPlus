package com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.Item;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;
import com.udacity.firebase.shoppinglistplusplus.ui.BaseActivity;
import com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails.adapter.ItemListAdapter;
import com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails.dialogs.AddItemDialogFragment;
import com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails.dialogs.EditListNameDialogFragment;
import com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails.dialogs.RemoveListDialogFragment;

import javax.annotation.Nullable;

import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.ACTIVE_LISTS;
import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.EXTRA_KEY_ID;
import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.ITEMS;
import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.ITEM_NAME;

public class ActiveListDetailsActivity extends BaseActivity implements ItemListAdapter.ItemClickListener {

    public static final String TAG = ActiveListDetailsActivity.class.getSimpleName();

    private String documentId;
    private ShoppingList shoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_list_details);

        initializeScreen();
    }

    private void initializeScreen() {
        Toolbar toolbar = findViewById(R.id.app_bar);
        if (getIntent() != null && getIntent().hasExtra(EXTRA_KEY_ID)) {
            documentId = getIntent().getStringExtra(EXTRA_KEY_ID);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setActionbarTitle();

        RecyclerView recyclerView = findViewById(R.id.recycler_view_items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(newItemListsAdapter());
    }

    private void setActionbarTitle() {
        db.collection(ACTIVE_LISTS).document(documentId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    shoppingList = snapshot.toObject(ShoppingList.class);
                } else {
                    finish();
                    return;
                }
                getSupportActionBar().setTitle(shoppingList.getListName());
            }
        });
    }

    private RecyclerView.Adapter newItemListsAdapter() {
        final FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        Query query = db
                .collection(ACTIVE_LISTS)
                .document(documentId)
                .collection(ITEMS)
                .orderBy(ITEM_NAME);

        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .setLifecycleOwner(this)
                .build();

        return new ItemListAdapter(options, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return true;
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

    private void showEditListNameDialog() {
        DialogFragment dialog = EditListNameDialogFragment.newInstance(shoppingList, documentId);
        dialog.show(getSupportFragmentManager(), "EditListNameDialogFragment");
    }

    private void showDeleteListDialog() {
        DialogFragment dialog = RemoveListDialogFragment.newInstance(shoppingList, documentId);
        dialog.show(getSupportFragmentManager(), "RemoveListDialogFragment");
    }

    public void showAddItemDialog(View view) {
        android.app.DialogFragment dialog = AddItemDialogFragment.newInstance(documentId);
        dialog.show(getFragmentManager(), "AddItemDialogFragment");
    }

    @Override
    public void onItemClicked(String documentId) {
        Toast.makeText(this, documentId, Toast.LENGTH_SHORT).show();
    }
}
