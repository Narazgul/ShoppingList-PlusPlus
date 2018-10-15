package com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.google.firebase.firestore.FirebaseFirestore;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;

import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.ACTIVE_LISTS;
import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.CONSTANT_DOCUMENT_ID;
import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.CONSTANT_LIST_NAME;

public class RemoveListDialogFragment extends DialogFragment {
    final static String LOG_TAG = RemoveListDialogFragment.class.getSimpleName();

    /**
     * Public static constructor that creates fragment and passes a bundle with data into it when adapter is created
     */
    public static RemoveListDialogFragment newInstance(ShoppingList shoppingList, String documentId) {
        RemoveListDialogFragment removeListDialogFragment = new RemoveListDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CONSTANT_LIST_NAME, shoppingList.getListName());
        bundle.putString(CONSTANT_DOCUMENT_ID, documentId);
        removeListDialogFragment.setArguments(bundle);
        return removeListDialogFragment;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog)
                .setTitle(getActivity().getResources().getString(R.string.action_remove_list))
                .setMessage(getString(R.string.dialog_message_are_you_sure_remove_list))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        removeList();
                        /* Dismiss the dialog */
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        /* Dismiss the dialog */
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);

        return builder.create();
    }

    private void removeList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String documentId = "";
        if (getArguments() != null) {
            documentId = getArguments().getString(CONSTANT_DOCUMENT_ID);
        }

        db.collection(ACTIVE_LISTS).document(documentId).delete();
    }
}

