package com.simorgh.englishtest.view;

import android.os.Bundle;

import com.simorgh.database.TestRepository;
import com.simorgh.englishtest.R;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // this is a generic way of getting your root view element
//        View rootView = findViewById(android.R.id.content);
//        rootView.setBackgroundDrawable(getResources().getDrawable(R.drawable.background));

        TestRepository testRepository = new TestRepository(getApplication());

    }
}
