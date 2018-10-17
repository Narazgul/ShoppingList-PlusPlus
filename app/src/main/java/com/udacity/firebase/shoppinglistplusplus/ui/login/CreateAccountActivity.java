package com.udacity.firebase.shoppinglistplusplus.ui.login;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.udacity.firebase.shoppinglistplusplus.R;

import java.math.BigInteger;
import java.security.SecureRandom;

import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.PREFS_USER_EMAIL;

public class CreateAccountActivity extends BaseLoginActivity {
    private static final String TAG = CreateAccountActivity.class.getSimpleName();

    private EditText username, email;
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

        LinearLayout linearLayout = findViewById(R.id.linear_layout_create_account_activity);
        initializeBackground(linearLayout);

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        authProgressDialog = new ProgressDialog(this);
        authProgressDialog.setTitle(getResources().getString(R.string.progress_dialog_loading));
        authProgressDialog.setMessage(getResources().getString(R.string.progress_dialog_creating_user_with_firebase));
        authProgressDialog.setCancelable(false);
    }

    public void onSignInPressed(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void onCreateAccountPressed(View view) {
        final String user = username.getText().toString();
        String mail = email.getText().toString();
        String pw = generateInitialPassword();

        if (isUserNameValid(user) && isEmailValid(mail)) {
            authProgressDialog.show();
            auth.createUserWithEmailAndPassword(mail, pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    authProgressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser != null) {
                            setUsername(user, firebaseUser);
                            sendPasswordResetEmail(firebaseUser);
                        }
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        showErrorToast(task.getException().getMessage());
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

    private String generateInitialPassword() {
        SecureRandom r = new SecureRandom();
        return new BigInteger(130, r).toString(32);
    }

    private void setUsername(String username, FirebaseUser user) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        } else {
                            Log.d(TAG, task.getException().getMessage());
                        }
                    }
                });
    }

    private void sendPasswordResetEmail(final FirebaseUser user) {
        String userEmail = user.getEmail();

        if (userEmail != null) {
            auth.sendPasswordResetEmail(userEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                                saveEmailToSharedPreferences(user);
                                sendPasswordResetEmail(user);
                                startEmailApp();
                                finish();
                            } else {
                                Log.d(TAG, task.getException().getMessage());
                            }
                        }
                    });
        }
    }

    private void saveEmailToSharedPreferences(FirebaseUser user) {
        prefs.edit().putString(PREFS_USER_EMAIL, user.getEmail()).apply();
    }

    private void startEmailApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void showErrorToast(String message) {
        Toast.makeText(CreateAccountActivity.this, message, Toast.LENGTH_LONG).show();
    }
}