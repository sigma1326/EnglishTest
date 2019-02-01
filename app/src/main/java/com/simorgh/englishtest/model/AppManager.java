package com.simorgh.englishtest.model;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;
import com.simorgh.database.TestRepository;
import com.simorgh.englishtest.R;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.multidex.MultiDexApplication;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class AppManager extends MultiDexApplication {
    public static volatile Handler applicationHandler;
    private static TestRepository testRepository;

    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            NUMBER_OF_CORES * 2,
            NUMBER_OF_CORES * 2,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>()
    );

    public static ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public static TestRepository getTestRepository() {
        return testRepository;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectAll()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());


        applicationHandler = new Handler(getApplicationContext().getMainLooper());


        testRepository = new TestRepository(this);
        testRepository.initDataBase(this);


        executor.execute(() -> {

            Stetho.initializeWithDefaults(this);

            ViewPump.init(ViewPump.builder()
                    .addInterceptor(new CalligraphyInterceptor(
                            new CalligraphyConfig.Builder()
                                    .setDefaultFontPath("fonts/iransans_medium.ttf")
                                    .setFontAttrId(R.attr.fontPath)
                                    .build()))
                    .build());
        });

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        executor.shutdown();
    }
}
