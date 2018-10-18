package com.udacity.firebase.shoppinglistplusplus.ui.activeLists.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;

public class ShoppingListAdapter extends FirestoreRecyclerAdapter<ShoppingList, ShoppingListAdapter.ShoppingListViewHolder> {

    public interface ShoppingListItemClickListener {
        void onItemClicked(String documentId);
    }

    private ShoppingListItemClickListener listener;
    private String userDisplayName;

    public ShoppingListAdapter(@NonNull FirestoreRecyclerOptions<ShoppingList> options,
                               ShoppingListItemClickListener listener,
                               String userDisplayName) {
        super(options);
        this.listener = listener;
        this.userDisplayName = userDisplayName;
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

    class ShoppingListViewHolder extends RecyclerView.ViewHolder {

        private TextView listName;
        private TextView createdBy;
        private TextView amountActualShoppers;

        ShoppingListViewHolder(@NonNull View itemView) {
            super(itemView);

            listName = itemView.findViewById(R.id.text_view_list_name);
            createdBy = itemView.findViewById(R.id.text_view_created_by_user);
            amountActualShoppers = itemView.findViewById(R.id.text_view_count_shopping_users);
        }

        void bind(ShoppingList list) {
            listName.setText(list.getListName());
            createdBy.setText(setYouOrOwnerText(list.getOwner()));
            amountActualShoppers.setText(setAmountOfPeopleShoppingOnListText(list.getUsersShoppingOnList()));
        }

        private String setYouOrOwnerText(String owner) {
            if (owner.equals(userDisplayName)) {
                return "You";
            } else {
                return owner;
            }
        }

        private String setAmountOfPeopleShoppingOnListText(int amount) {
            if (amount <= 0) {
                return "";
            } else if (amount == 1) {
                return "1 person shopping";
            } else {
                return amount + " people shopping";
            }
        }
    }
}
