package com.simorgh.englishtest.di.module;

import android.app.Application;

import com.simorgh.database.Repository;
import com.simorgh.database.TestDataBase;

import dagger.Module;
import dagger.Provides;

@Module
public class DataBaseModule {

    @Provides
    public Repository getRepository(Application application, TestDataBase dataBase) {
        return new Repository(application, dataBase);
    }

    @Provides
    public TestDataBase getDataBase(Application application) {
        return TestDataBase.getDatabase(application);
    }
}
