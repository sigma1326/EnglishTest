package com.simorgh.englishtest.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.simorgh.englishtest.R;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 500);
    }
}
