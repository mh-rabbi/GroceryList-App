package com.quickgrocerylist.grocerylist;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashScActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_sc);

        Thread thread = new Thread() {
            public void run() {
                super.run();
                try {
                    sleep(3000);
                    startActivity(new Intent(SplashScActivity.this, MainActivity.class));
                    //finish();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        thread.start();
    }

    protected void onStop() {
        super.onStop();
        finish(); //for suiciding the splash activity when we use back
    }
}