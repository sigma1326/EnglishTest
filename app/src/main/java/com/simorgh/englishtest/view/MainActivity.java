package com.simorgh.englishtest.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.simorgh.database.TestRepository;
import com.simorgh.englishtest.R;
import com.simorgh.englishtest.adapter.InnerData;
import com.simorgh.englishtest.adapter.OuterAdapter;
import com.simorgh.garlandview.TailLayoutManager;
import com.simorgh.garlandview.TailRecyclerView;
import com.simorgh.garlandview.TailSnapHelper;
import com.simorgh.garlandview.header.HeaderTransformer;

import java.util.ArrayList;
import java.util.List;
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

        final List<List<InnerData>> outerData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final List<InnerData> innerData = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                innerData.add(createInnerData());
            }
            outerData.add(innerData);
        }

        TestRepository testRepository = new TestRepository(getApplication());


        rv = findViewById(R.id.rv_main);
        ((TailLayoutManager) Objects.requireNonNull(rv.getLayoutManager())).setPageTransformer(new HeaderTransformer());
        new TailSnapHelper().attachToRecyclerView(rv);

        Runnable runnable = () -> rv.setAdapter(new OuterAdapter(testRepository.getYearMajorData()));
        runnable.run();

    }

    private InnerData createInnerData() {
        return new InnerData("سوالات زبان کنکور گروه ریاضی فنی", "تعداد سوالات: 75", "زمان پاسخگویی:50 دقیقه");
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
