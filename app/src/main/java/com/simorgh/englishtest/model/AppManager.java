package com.simorgh.englishtest.model;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.StrictMode;

import com.simorgh.database.Repository;
import com.simorgh.database.TestDataBase;
import com.simorgh.englishtest.R;
import com.simorgh.englishtest.di.component.DaggerApplicationComponent;
import com.simorgh.englishtest.di.module.ApplicationModule;
import com.simorgh.englishtest.di.module.DataBaseModule;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.multidex.MultiDexApplication;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class AppManager extends MultiDexApplication {
    public static volatile Handler applicationHandler;
    private static Repository repository;
    private static DaggerApplicationComponent daggerApplicationComponent;


    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            NUMBER_OF_CORES * 2,
            NUMBER_OF_CORES * 2,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>()
    );

    public static ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public static Repository getRepository() {
        return repository;
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


        repository = new Repository(this, TestDataBase.getDatabase(this));
        repository.initDataBase(this);

        daggerApplicationComponent = (DaggerApplicationComponent) DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .dataBaseModule(new DataBaseModule())
                .build();


        executor.execute(() -> {

//            if (BuildConfig.DEBUG) {
//                Stetho.initializeWithDefaults(this);
//            }

            ViewPump.init(ViewPump.builder()
                    .addInterceptor(new CalligraphyInterceptor(
                            new CalligraphyConfig.Builder()
                                    .setDefaultFontPath("fonts/iransans_medium.ttf")
                                    .setFontAttrId(R.attr.fontPath)
                                    .build()))
                    .build());
        });

    }
    public static DaggerApplicationComponent getDaggerApplicationComponent() {
        return daggerApplicationComponent;
    }

    @Override
    public void onTerminate() {
        executor.shutdown();
        super.onTerminate();
    }
}
