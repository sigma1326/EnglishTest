package com.simorgh.literaturetest.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.simorgh.literaturetest.BaseActivity;
import com.simorgh.literaturetest.R;

public class SplashActivity extends BaseActivity {

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
