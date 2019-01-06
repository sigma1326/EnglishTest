package com.simorgh.englishtest.model;

import android.app.Application;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;

public class AppManager extends Application {
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

        Stetho.initializeWithDefaults(this);

//        RoomAsset.databaseBuilder(this, , "test.db").build();
    }

}
