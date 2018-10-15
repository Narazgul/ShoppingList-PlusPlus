package com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.Item;

public class ItemListAdapter extends FirestoreRecyclerAdapter<Item, ItemListAdapter.ItemViewHolder> {

    public interface ItemDeleteClickListener {
        void onItemDeleteClicked(String itemId);
    }

    public interface ItemLongClickListener {
        void onItemLongClicked(String itemId);
    }

    private ItemDeleteClickListener listener;
    private ItemLongClickListener longListener;

    public ItemListAdapter(@NonNull FirestoreRecyclerOptions<Item> options,
                           ItemDeleteClickListener listener,
                           ItemLongClickListener longListener) {
        super(options);
        this.listener = listener;
        this.longListener = longListener;
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
        holder.bind(model, documentId);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longListener.onItemLongClicked(documentId);
                return true;
            }
        });
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        // TODO update when emptyscreen should be shown
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageButton delete;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_view_active_list_item_name);
            delete = itemView.findViewById(R.id.button_remove_item);
        }

        void bind(Item item, final String documentId) {
            name.setText(item.getItemName());
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemDeleteClicked(documentId);
                }
            });
        }
    }
}
