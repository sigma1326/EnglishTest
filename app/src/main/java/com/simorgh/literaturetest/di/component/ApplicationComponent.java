package com.simorgh.literaturetest.di.component;

import com.simorgh.literaturetest.BaseActivity;
import com.simorgh.literaturetest.BaseFragment;
import com.simorgh.literaturetest.di.module.ApplicationModule;
import com.simorgh.literaturetest.di.module.DataBaseModule;

import dagger.Component;

@Component(modules = {ApplicationModule.class, DataBaseModule.class})
public interface ApplicationComponent {
    void inject(BaseActivity baseActivity);

    void inject(BaseFragment baseFragment);
}
