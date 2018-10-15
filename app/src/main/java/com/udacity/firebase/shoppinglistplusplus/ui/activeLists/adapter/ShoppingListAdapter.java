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
import com.udacity.firebase.shoppinglistplusplus.utils.Utils;

public class ShoppingListAdapter extends FirestoreRecyclerAdapter<ShoppingList, ShoppingListAdapter.ShoppingListViewHolder> {

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

    class ShoppingListViewHolder extends RecyclerView.ViewHolder {

        private TextView listName;
        private TextView createdBy;
        private TextView editTime;

        ShoppingListViewHolder(@NonNull View itemView) {
            super(itemView);

            listName = itemView.findViewById(R.id.text_view_list_name);
            createdBy = itemView.findViewById(R.id.text_view_created_by_user);
            editTime = itemView.findViewById(R.id.text_view_edit_time);
        }

        void bind(ShoppingList list) {
            listName.setText(list.getListName());
            createdBy.setText(list.getOwner());
            String formattedDate = Utils.SIMPLE_DATE_FORMAT.format(list.getLastEdited().toDate());
            editTime.setText(formattedDate);
        }
    }
}
