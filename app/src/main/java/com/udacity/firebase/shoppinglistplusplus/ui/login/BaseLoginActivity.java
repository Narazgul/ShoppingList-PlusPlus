package com.udacity.firebase.shoppinglistplusplus.ui.login;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.udacity.firebase.shoppinglistplusplus.R;
import com.udacity.firebase.shoppinglistplusplus.ui.MainActivity;

public abstract class BaseLoginActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initializeBackground(LinearLayout linearLayout) {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            linearLayout.setBackgroundResource(R.drawable.background_loginscreen_land);
        } else {
            linearLayout.setBackgroundResource(R.drawable.background_loginscreen);
        }
    }
}
