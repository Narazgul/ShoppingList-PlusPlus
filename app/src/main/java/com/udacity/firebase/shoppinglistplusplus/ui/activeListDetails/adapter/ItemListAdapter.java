package com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails.adapter;

import android.graphics.Paint;
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

    public interface ItemBoughtClickListener {
        void onItemBoughtClicked(String itemId);
    }

    public interface ItemDeleteClickListener {
        void onItemDeleteClicked(String itemId);
    }

    public interface ItemLongClickListener {
        void onItemLongClicked(String itemId);
    }

    private ItemBoughtClickListener boughtListener;
    private ItemDeleteClickListener deleteListener;
    private ItemLongClickListener longListener;
    private String displayName;

    public ItemListAdapter(@NonNull FirestoreRecyclerOptions<Item> options,
                           ItemBoughtClickListener boughtListener,
                           ItemDeleteClickListener deleteListener,
                           ItemLongClickListener longListener,
                           String displayName) {
        super(options);
        this.boughtListener = boughtListener;
        this.deleteListener = deleteListener;
        this.longListener = longListener;
        this.displayName = displayName;
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
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        // TODO update when emptyscreen should be shown
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView name, boughtBy, boughtByUser;
        private ImageButton delete;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_view_active_list_item_name);
            boughtBy = itemView.findViewById(R.id.text_view_bought_by);
            boughtByUser = itemView.findViewById(R.id.text_view_bought_by_user);

            delete = itemView.findViewById(R.id.button_remove_item);
        }

        void bind(Item item, final String documentId) {
            name.setText(item.getItemName());
            if (item.getBought()) {
                name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                boughtBy.setVisibility(View.VISIBLE);
                boughtByUser.setVisibility(View.VISIBLE);
                boughtByUser.setText(checkBoughtByOwner(item.getBoughtBy()));
                delete.setVisibility(View.GONE);
            } else {
                name.setPaintFlags(name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                boughtBy.setVisibility(View.GONE);
                boughtByUser.setVisibility(View.GONE);
                delete.setVisibility(View.VISIBLE);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteListener.onItemDeleteClicked(documentId);
                    }
                });
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boughtListener.onItemBoughtClicked(documentId);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longListener.onItemLongClicked(documentId);
                    return true;
                }
            });
        }

        private String checkBoughtByOwner(String boughtBy) {
            if (displayName.equals(boughtBy)) {
                return "You";
            } else {
                return boughtBy;
            }
        }

        private boolean checkUserIsOwner() {
            return false;
        }
    }
}
