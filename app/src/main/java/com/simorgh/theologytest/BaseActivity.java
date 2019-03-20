package com.simorgh.theologytest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import com.simorgh.database.Repository;
import com.simorgh.theologytest.model.AppManager;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    @Inject
    protected Repository repository;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppManager.getDaggerApplicationComponent().inject(this);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
