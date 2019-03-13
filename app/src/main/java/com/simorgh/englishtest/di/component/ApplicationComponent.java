package com.simorgh.englishtest.di.component;

import com.simorgh.englishtest.BaseActivity;
import com.simorgh.englishtest.BaseFragment;
import com.simorgh.englishtest.di.module.ApplicationModule;
import com.simorgh.englishtest.di.module.DataBaseModule;

import dagger.Component;

@Component(modules = {ApplicationModule.class, DataBaseModule.class})
public interface ApplicationComponent {
    void inject(BaseActivity baseActivity);

    void inject(BaseFragment baseFragment);
}
