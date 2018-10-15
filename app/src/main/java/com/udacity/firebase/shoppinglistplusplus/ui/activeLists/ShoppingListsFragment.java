package com.udacity.firebase.shoppinglistplusplus.ui.activeLists;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.DocumentSnapshot.ServerTimestampBehavior;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;
import com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails.ActiveListDetailsActivity;

import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.ACTIVE_LISTS;
import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.EXTRA_KEY_ID;
import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.SHOPPING_LIST_LAST_EDITED;

public class ShoppingListsFragment extends Fragment implements ShoppingListAdapter.ShoppingListItemClickListener {
    public static final String TAG = ShoppingListsFragment.class.getSimpleName();

    public ShoppingListsFragment() {
        /* Required empty public constructor */
    }

    /**
     * Create fragment and pass bundle with data as it's arguments
     * Right now there are not arguments...but eventually there will be.
     */
    public static ShoppingListsFragment newInstance() {
        ShoppingListsFragment fragment = new ShoppingListsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_shopping_lists, container, false);
        initializeScreen(rootView);


        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ActiveListDetailsActivity.class));
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * Link layout elements from XML
     */
    private void initializeScreen(View rootView) {
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view_active_lists);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(newShoppingListsAdapter());
    }

    private FirestoreRecyclerAdapter newShoppingListsAdapter() {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        final FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);

        Query query = firestore
                .collection(ACTIVE_LISTS)
                .orderBy(SHOPPING_LIST_LAST_EDITED);

        final SnapshotParser<ShoppingList> parser = new SnapshotParser<ShoppingList>() {
            @NonNull
            @Override
            public ShoppingList parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                return snapshot.toObject(ShoppingList.class, ServerTimestampBehavior.ESTIMATE);
            }
        };

        FirestoreRecyclerOptions<ShoppingList> options = new FirestoreRecyclerOptions.Builder<ShoppingList>()
                .setQuery(query, parser)
                .setLifecycleOwner(getActivity())
                .build();

        return new ShoppingListAdapter(options, this);
    }

    @Override
    public void onItemClicked(String documentId) {
        Intent intent = new Intent(getContext(), ActiveListDetailsActivity.class);
        intent.putExtra(EXTRA_KEY_ID, documentId);
        startActivity(intent);
    }
}
