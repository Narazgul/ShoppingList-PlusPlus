package com.udacity.firebase.shoppinglistplusplus.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.ui.login.LoginActivity;

import static com.udacity.firebase.shoppinglistplusplus.utils.Constants.PREFS_DISPLAY_NAME;

public abstract class BaseActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    public SharedPreferences prefs;
    public FirebaseAuth auth;
    public FirebaseUser user;
    public FirebaseFirestore db;
    public String userDisplayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        userDisplayName = prefs.getString(PREFS_DISPLAY_NAME, null);
        auth = FirebaseAuth.getInstance();
        auth.useAppLanguage();
        db = FirebaseFirestore.getInstance();
        setFirestoreSettings();
    }

    private void setFirestoreSettings() {
        final FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Log.d("AuthStateListener", "Logged in");
            saveUserToPreferences(user);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void saveUserToPreferences(FirebaseUser user) {
        prefs.edit().putString(PREFS_DISPLAY_NAME, user.getDisplayName()).apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.action_logout:
                logoutUser();
        }
        return super.onOptionsItemSelected(item);
    }

    public void logoutUser() {
        auth.signOut();
        prefs.edit().putString(PREFS_DISPLAY_NAME, null).apply();
    }
}
