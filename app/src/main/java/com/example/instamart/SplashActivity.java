package com.example.instamart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    // User is already signed in, navigate to MainActivity
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                } else {
                    // User is not signed in, navigate to LoginActivity
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                }

                // close this activity
                finish();
            }
        }, 3000);
    }
}
