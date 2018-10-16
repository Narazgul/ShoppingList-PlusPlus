package com.udacity.firebase.shoppinglistplusplus.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.ui.MainActivity;

public class LoginActivity extends BaseLoginActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private ProgressDialog mAuthProgressDialog;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeScreen();
    }

    public void initializeScreen() {

        email = findViewById(R.id.edit_text_email);
        password = findViewById(R.id.edit_text_password);

        LinearLayout linearLayoutLoginActivity = findViewById(R.id.linear_layout_login_activity);
        initializeBackground(linearLayoutLoginActivity);

        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getString(R.string.progress_dialog_authenticating_with_firebase));
        mAuthProgressDialog.setCancelable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    public void onSignInPressed(View view) {
        String mail = email.getText().toString();
        String pw = password.getText().toString();

        if (isEmailEntered(mail) && isPasswordEntered(pw)) {
            auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Log.d(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private boolean isEmailEntered(String email) {
        if (TextUtils.isEmpty(email)) {
            this.email.setError("No email entered");
            return false;
        }
        return true;
    }

    private boolean isPasswordEntered(String password) {
        if (TextUtils.isEmpty(password)) {
            this.password.setError("No password entered");
            return false;
        }
        return true;
    }

    public void onSignUpPressed(View view) {
        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        startActivity(intent);
    }
}
