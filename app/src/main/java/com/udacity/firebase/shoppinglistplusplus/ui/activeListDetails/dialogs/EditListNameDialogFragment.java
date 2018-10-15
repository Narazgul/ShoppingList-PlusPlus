package com.udacity.firebase.shoppinglistplusplus.ui.activeListDetails.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.model.ShoppingList;

import java.util.HashMap;

import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.ACTIVE_LISTS;
import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.CONSTANT_DOCUMENT_ID;
import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.CONSTANT_LIST_NAME;
import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.SHOPPING_LIST_LAST_EDITED;
import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.SHOPPING_LIST_NAME;

public class EditListNameDialogFragment extends DialogFragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText mEditTextListName;

    /**
     * Public static constructor that creates fragment and
     * passes a bundle with data into it when adapter is created
     */
    public static EditListNameDialogFragment newInstance(ShoppingList list, String documentId) {
        EditListNameDialogFragment editListNameDialogFragment = new EditListNameDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CONSTANT_LIST_NAME, list.getListName());
        bundle.putString(CONSTANT_DOCUMENT_ID, documentId);
        editListNameDialogFragment.setArguments(bundle);
        return editListNameDialogFragment;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Open the keyboard automatically when the dialog fragment is opened
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_edit_list_name, null);
        mEditTextListName = rootView.findViewById(R.id.edit_text_list_name);

        mEditTextListName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    editShoppingListName();
                }
                return true;
            }
        });

        String listname = "";
        if (getArguments() != null) {
            listname = getArguments().getString(CONSTANT_LIST_NAME);
        }
        mEditTextListName.setHint(listname);

        builder.setView(rootView)
                .setPositiveButton("Edit name", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        editShoppingListName();
                    }
                })
                .setNegativeButton(R.string.negative_button_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }

    /**
     * Add new active list
     */
    public void editShoppingListName() {
        String documentId = "";
        if (getArguments() != null) {
            documentId = getArguments().getString(CONSTANT_DOCUMENT_ID);
        }
        DocumentReference ref = db.collection(ACTIVE_LISTS).document(documentId);

        HashMap<String, Object> updates = new HashMap<>();
        updates.put(SHOPPING_LIST_NAME, mEditTextListName.getText().toString());
        updates.put(SHOPPING_LIST_LAST_EDITED, FieldValue.serverTimestamp());

        ref.update(updates);
    }
}

