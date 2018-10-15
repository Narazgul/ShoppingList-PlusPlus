package com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.Item;
import com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails.viewholder.ItemViewHolder;

public class ItemListAdapter extends FirestoreRecyclerAdapter<Item, ItemViewHolder> {

    public interface ItemClickListener {
        void onItemClicked(String documentId);
    }

    private ItemClickListener listener;

    public ItemListAdapter(@NonNull FirestoreRecyclerOptions<Item> options, ItemClickListener listener) {
        super(options);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_active_list_item, viewGroup, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull Item model) {
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
