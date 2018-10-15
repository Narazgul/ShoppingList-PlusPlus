package com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;
import com.udacity.firebase.shoppinglistplusplus.ui.BaseActivity;

import javax.annotation.Nullable;

import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.ACTIVE_LISTS;
import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.EXTRA_KEY_ID;

public class ActiveListDetailsActivity extends BaseActivity {

    public static final String TAG = ActiveListDetailsActivity.class.getSimpleName();

    private String documentId;
    private ShoppingList shoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_list_details);

        initializeScreen();
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    private void initializeScreen() {
        Toolbar toolbar = findViewById(R.id.app_bar);
        if (getIntent() != null && getIntent().hasExtra(EXTRA_KEY_ID)) {
            documentId = getIntent().getStringExtra(EXTRA_KEY_ID);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setActionbarTitle();
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
}
