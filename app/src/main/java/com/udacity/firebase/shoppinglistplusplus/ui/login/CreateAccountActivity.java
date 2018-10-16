package com.udacity.firebase.shoppinglistplusplus.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.udacity.firebase.shoppinglistplusplus.R;

public class CreateAccountActivity extends BaseLoginActivity {
    private static final String TAG = CreateAccountActivity.class.getSimpleName();


    private EditText username, email, password;
    private ProgressDialog authProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        initializeScreen();
    }



    public void initializeScreen() {

        username = findViewById(R.id.edit_text_username_create);
        email = findViewById(R.id.edit_text_email_create);
        password = findViewById(R.id.edit_text_password_create);

        LinearLayout linearLayout = findViewById(R.id.linear_layout_create_account_activity);
        initializeBackground(linearLayout);

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        authProgressDialog = new ProgressDialog(this);
        authProgressDialog.setTitle(getResources().getString(R.string.progress_dialog_loading));
        authProgressDialog.setMessage(getResources().getString(R.string.progress_dialog_creating_user_with_firebase));
        authProgressDialog.setCancelable(false);
    }

    public void onSignInPressed(View view) {
        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void onCreateAccountPressed(View view) {
        String user = username.getText().toString();
        String mail = email.getText().toString();
        String pw = password.getText().toString();

        if (isUserNameValid(user) && isEmailValid(mail) && isPasswordValid(pw)) {
            authProgressDialog.show();
            auth.createUserWithEmailAndPassword(mail, pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    authProgressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        Toast.makeText(CreateAccountActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        showErrorToast("User could not be created: " + task.getException());
                    }
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        Boolean emailValidation = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (!emailValidation) {
            this.email.setError("Please type in a valid email address");
            return false;
        }
        return true;
    }

    private boolean isUserNameValid(String userName) {
        if (TextUtils.isEmpty(userName) || userName.length() < 3) {
            this.username.setError("Your username must have at least 3 characters");
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            this.password.setError("You must have at least 6 characters in your password");
            return false;
        }
        return true;
    }

    private void showErrorToast(String message) {
        Toast.makeText(CreateAccountActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
