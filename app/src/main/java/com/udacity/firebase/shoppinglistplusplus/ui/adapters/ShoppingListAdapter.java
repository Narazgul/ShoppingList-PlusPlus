package com.udacity.firebase.shoppinglistplusplus.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {

    private List<ShoppingList> shoppingLists;

    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ShoppingListViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_active_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListViewHolder shoppingListViewHolder, int position) {
        shoppingListViewHolder.bind(shoppingLists.get(position));
    }

    @Override
    public int getItemCount() {
        if (shoppingLists != null) {
            return 1;
        } else {
            return 0;
        }
    }

    public void updateShoppingList(List<ShoppingList> newShoppingList) {
        if (newShoppingList != null && newShoppingList != shoppingLists) {
            shoppingLists = newShoppingList;
        }
    }

    static class ShoppingListViewHolder extends RecyclerView.ViewHolder {

        TextView listName;
        TextView createdBy;

        ShoppingListViewHolder(@NonNull View itemView) {
            super(itemView);

            listName = itemView.findViewById(R.id.text_view_list_name);
            createdBy = itemView.findViewById(R.id.text_view_created_by_user);
        }

        void bind(ShoppingList list) {
            listName.setText(list.getListName());
            createdBy.setText(list.getOwner());
        }
    }

}
