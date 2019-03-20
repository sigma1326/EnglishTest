package com.simorgh.theologytest.di.component;

import com.simorgh.theologytest.BaseActivity;
import com.simorgh.theologytest.BaseFragment;
import com.simorgh.theologytest.di.module.ApplicationModule;
import com.simorgh.theologytest.di.module.DataBaseModule;

import dagger.Component;

@Component(modules = {ApplicationModule.class, DataBaseModule.class})
public interface ApplicationComponent {
    void inject(BaseActivity baseActivity);

    void inject(BaseFragment baseFragment);
}
