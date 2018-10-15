package com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails.viewholder;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.Item;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    private TextView name;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.text_view_active_list_item_name);
    }

    public void bind(Item item) {
        name.setText(item.getItemName());
    }
}
