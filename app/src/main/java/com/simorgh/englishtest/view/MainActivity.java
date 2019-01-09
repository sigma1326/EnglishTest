package com.simorgh.englishtest.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.simorgh.database.TestRepository;
import com.simorgh.englishtest.R;
import com.simorgh.englishtest.adapter.OuterAdapter;
import com.simorgh.garlandview.TailLayoutManager;
import com.simorgh.garlandview.TailRecyclerView;
import com.simorgh.garlandview.TailSnapHelper;
import com.simorgh.garlandview.header.HeaderTransformer;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends AppCompatActivity {
    TailRecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // this is a generic way of getting your root view element
        View rootView = findViewById(android.R.id.content);
        rootView.setBackgroundDrawable(getResources().getDrawable(R.drawable.background));



        rv = findViewById(R.id.rv_main);
        ((TailLayoutManager) Objects.requireNonNull(rv.getLayoutManager())).setPageTransformer(new HeaderTransformer());
        new TailSnapHelper().attachToRecyclerView(rv);

        Runnable runnable = () -> {
            TestRepository testRepository = new TestRepository(getApplication());
            rv.setAdapter(new OuterAdapter(testRepository.getYearMajorData()));
        };
        runnable.run();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

}
