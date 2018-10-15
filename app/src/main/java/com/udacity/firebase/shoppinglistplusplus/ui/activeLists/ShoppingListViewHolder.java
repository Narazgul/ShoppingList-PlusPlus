package com.udacity.firebase.shoppinglistplusplus.ui.activeLists;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;
import com.udacity.firebase.shoppinglistplusplus.utils.Utils;

public class ShoppingListViewHolder extends RecyclerView.ViewHolder {

    private TextView listName;
    private TextView createdBy;
    private TextView editTime;

    public ShoppingListViewHolder(@NonNull View itemView) {
        super(itemView);

        listName = itemView.findViewById(R.id.text_view_list_name);
        createdBy = itemView.findViewById(R.id.text_view_created_by_user);
        editTime = itemView.findViewById(R.id.text_view_edit_time);
    }

    public void bind(ShoppingList list) {
        listName.setText(list.getListName());
        createdBy.setText(list.getOwner());
        String formattedDate = Utils.SIMPLE_DATE_FORMAT.format(list.getLastEdited().toDate());
        editTime.setText(formattedDate);
    }
}
