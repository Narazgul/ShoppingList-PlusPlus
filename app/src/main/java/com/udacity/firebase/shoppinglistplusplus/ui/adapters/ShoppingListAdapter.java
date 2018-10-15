package com.udacity.firebase.shoppinglistplusplus.ui.adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;
import com.udacity.firebase.shoppinglistplusplus.ui.viewholders.ShoppingListViewHolder;

public class ShoppingListAdapter extends FirestoreRecyclerAdapter<ShoppingList, ShoppingListViewHolder> {

    public interface ShoppingListItemClickListener {
        void onItemClicked(String documentId);
    }

    private ShoppingListItemClickListener listener;


    public ShoppingListAdapter(@NonNull FirestoreRecyclerOptions<ShoppingList> options, ShoppingListItemClickListener listener) {
        super(options);
        this.listener = listener;
    }


    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ShoppingListViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_active_list, viewGroup, false));
    }


    @Override
    protected void onBindViewHolder(@NonNull final ShoppingListViewHolder holder, final int position, @NonNull ShoppingList model) {
        final String documentId = getSnapshots().getSnapshot(position).getId();
        holder.bind(model);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(documentId);
            }
        });
    }


    @Override
    public void onDataChanged() {
        super.onDataChanged();
        // TODO update when emptyscreen should be shown
    }
}
