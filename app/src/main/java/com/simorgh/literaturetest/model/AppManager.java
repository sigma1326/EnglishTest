package com.simorgh.literaturetest.model;

import android.annotation.SuppressLint;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;
import com.simorgh.literaturetest.BuildConfig;
import com.simorgh.literaturetest.R;
import com.simorgh.literaturetest.di.component.DaggerApplicationComponent;
import com.simorgh.literaturetest.di.module.ApplicationModule;
import com.simorgh.literaturetest.di.module.DataBaseModule;
import com.simorgh.threadutils.ThreadUtils;

import androidx.multidex.MultiDexApplication;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class AppManager extends MultiDexApplication {
    private static DaggerApplicationComponent daggerApplicationComponent;


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


        daggerApplicationComponent = (DaggerApplicationComponent) DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .dataBaseModule(new DataBaseModule())
                .build();


        ThreadUtils
                .getCompletable(() -> {
                    if (BuildConfig.DEBUG) {
                        Stetho.initializeWithDefaults(this);
                    }

                    ViewPump.init(ViewPump.builder()
                            .addInterceptor(new CalligraphyInterceptor(
                                    new CalligraphyConfig.Builder()
                                            .setDefaultFontPath("fonts/iransans_medium.ttf")
                                            .setFontAttrId(R.attr.fontPath)
                                            .build()))
                            .build());
                })
                .compose(ThreadUtils.applyIOCompletable())
                .subscribeWith(ThreadUtils.completableObserver);
    }

    public static DaggerApplicationComponent getDaggerApplicationComponent() {
        return daggerApplicationComponent;
    }

    @Override
    public void onTerminate() {
        ThreadUtils.finish();
        super.onTerminate();
    }
}
